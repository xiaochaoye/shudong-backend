package com.shudong.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.user.dto.LoginRequestDTO;
import com.shudong.user.dto.RegisterRequestDTO;
import com.shudong.user.dto.UserRequestDTO;
import com.shudong.user.entity.Devices;
import com.shudong.user.entity.Users;
import com.shudong.user.mapper.UsersMapper;
import com.shudong.user.service.UsersService;
import com.shudong.user.service.UserSettingsService;
import com.shudong.user.service.DevicesService;
import com.shudong.common.exception.BusinessException;
import com.shudong.common.utils.PasswordUtil;
import com.shudong.common.utils.RedisUtil;
import com.shudong.common.utils.UploadUtil;
import com.shudong.message.service.MailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* @author test
* @description 针对表【users(用户表，存储注册用户信息)】的数据库操作Service实现
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

    private final RedisUtil redisUtil;

    private final PasswordUtil passwordUtil;

    private final MailService mailService;

    private final UploadUtil uploadUtil;

    private final UserSettingsService userSettingsService;

    private final DevicesService devicesService;

    @Override
    public boolean sendRegisterCode(String email) {
        // 检查邮箱是否已注册
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        Users existingUser = this.getOne(queryWrapper);
        if (existingUser != null) {
            throw new BusinessException("该邮箱已被注册");
        }

        // 频率限制：同一邮箱每天最多获取5次验证码（先检查，更严格的限制在前）
        String dailyCountKey = "register:daily:" + email + ":" + java.time.LocalDate.now();
        long dailyCount = redisUtil.incr(dailyCountKey, 1);
        if (dailyCount == 1) {
            redisUtil.expire(dailyCountKey, 86400);
        }
        if (dailyCount > 5) {
            redisUtil.decr(dailyCountKey, 1);
            throw new BusinessException("今日获取验证码次数已达上限，请明天再试");
        }

        // 频率限制：1分钟内不能重复获取（原子操作，SET NX EX）
        String lastRequestKey = "register:limit:" + email;
        Boolean isFirstRequest = redisUtil.setIfAbsent(lastRequestKey, "1", 60);
        if (!isFirstRequest) {
            // 分钟冷却被触发，回退每日计数
            redisUtil.decr(dailyCountKey, 1);
            throw new BusinessException("操作过于频繁，请1分钟后再试");
        }

        // 生成验证码
        String code = passwordUtil.generateVerificationCode();

        // 存储验证码到Redis
        redisUtil.setVerificationCode(email, code, "register");

        try {
            mailService.sendVerificationEmail(email, code);
            return true;
        } catch (Exception e) {
            throw new BusinessException("邮件发送失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Users register(RegisterRequestDTO request) {
        // 验证验证码
        if (!redisUtil.verifyCode(request.getEmail(), request.getCode(), "register")) {
            throw new BusinessException("验证码错误或已过期");
        }

        // 检查邮箱是否已注册
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", request.getEmail());
        Users existingUser = this.getOne(queryWrapper);
        if (existingUser != null) {
            throw new BusinessException("该邮箱已被注册");
        }

        if (!passwordUtil.validatePasswordFormat(request.getPassword())) {
            throw new BusinessException("密码格式不符合要求：长度8-20位，必须包含字母、特殊符号和数字");
        }

        // 创建用户
        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setUsername(passwordUtil.generateUsername(request.getEmail()));
        user.setPasswordHash(passwordUtil.encodePassword(request.getPassword()));
        user.setAnonymousAvatar("https://tdesign.gtimg.com/site/avatar.jpg");
        user.setCreatedAt(new Date());
        user.setLastLoginAt(new Date());
        user.setIsAdmin(0);

        // 保存用户
        boolean saved = this.save(user);
        if (!saved) {
            throw new BusinessException("注册失败，请稍后重试");
        }

        // 创建默认用户设置
        try {
            userSettingsService.createDefaultSettings(user.getId());
        } catch (Exception e) {
            // 设置创建失败不影响注册流程，记录日志即可
            log.warn("创建用户默认设置失败，userId={}: {}", user.getId(), e.getMessage());
        }

        // 删除已使用的验证码
        redisUtil.deleteVerificationCode(request.getEmail(), "register");

        return user;
    }

    @Override
    public Users login(LoginRequestDTO request) {
        // 查询用户
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", request.getEmail());
        Users user = this.getOne(queryWrapper);

        if (user == null) {
            throw new BusinessException("邮箱或密码错误");
        }

        // 检查账号是否启用
        if ("INACTIVE".equals(user.getRecordStatus())) {
            throw new BusinessException("账号已被禁用");
        }

        // 验证密码
        if (!passwordUtil.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("邮箱或密码错误");
        }

        // 更新最后登录时间
        user.setLastLoginAt(new Date());
        this.updateById(user);

        // 注册或更新设备信息（仅当客户端提供了有效 deviceId 时）
        String deviceId = request.getDeviceId();
        if (deviceId != null && !deviceId.trim().isEmpty()) {
            try {
                String deviceName = request.getDeviceName() != null ? request.getDeviceName() : "未知设备";
                String userAgent = request.getUserAgent() != null ? request.getUserAgent() : "";
                String ipAddress = request.getIpAddress() != null ? request.getIpAddress() : "";

                List<Devices> existingDevices = devicesService.getDevicesByUserId(user.getId());
                boolean deviceExists = existingDevices.stream()
                    .anyMatch(d -> deviceId.equals(d.getDeviceId()) && "ACTIVE".equals(d.getDeviceStatus()));

                if (deviceExists) {
                    Devices existingDevice = existingDevices.stream()
                        .filter(d -> deviceId.equals(d.getDeviceId()))
                        .findFirst()
                        .orElse(null);
                    if (existingDevice != null) {
                        devicesService.updateLastLogin(existingDevice.getId());
                    }
                } else {
                    devicesService.registerDevices(user.getId(), deviceId, deviceName, userAgent, ipAddress);
                }
            } catch (Exception e) {
                log.warn("用户登录时设备注册失败，userId={}: {}", user.getId(), e.getMessage());
            }
        }

        return user;
    }

    @Override
    public boolean sendResetPasswordCode(String email) {
        // 检查邮箱是否存在
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        Users user = this.getOne(queryWrapper);
        if (user == null) {
            throw new BusinessException("该邮箱未注册");
        }

        // 检查账号是否启用
        if ("INACTIVE".equals(user.getRecordStatus())) {
            throw new BusinessException("账号已被禁用");
        }

        // 生成验证码
        String code = passwordUtil.generateVerificationCode();
        
        // 存储验证码到Redis
        redisUtil.setVerificationCode(email, code, "reset");
        
        try {
            mailService.sendVerificationEmail(email, code);
            return true;
        } catch (Exception e) {
            throw new BusinessException("邮件发送失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean resetPassword(String email, String code, String newPassword) {
        // 验证验证码
        if (!redisUtil.verifyCode(email, code, "reset")) {
            throw new BusinessException("验证码错误或已过期");
        }

        // 查询用户
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        Users user = this.getOne(queryWrapper);
        
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查账号是否启用
        if ("INACTIVE".equals(user.getRecordStatus())) {
            throw new BusinessException("账号已被禁用");
        }

        if (!passwordUtil.validatePasswordFormat(newPassword)) {
            throw new BusinessException("新密码格式不符合要求：长度8-20位，必须包含字母、特殊符号和数字");
        }

        // 更新密码
        user.setPasswordHash(passwordUtil.encodePassword(newPassword));
        user.setLastLoginAt(new Date());
        boolean updated = this.updateById(user);

        if (updated) {
            // 删除已使用的验证码
            redisUtil.deleteVerificationCode(email, "reset");
        }

        return updated;
    }

    @Override
    @Transactional
    public Users updateProfile(Long userId, UserRequestDTO request) {
        // 查询用户
        Users user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 检查账号是否启用
        if ("INACTIVE".equals(user.getRecordStatus())) {
            throw new BusinessException("账号已被禁用");
        }

        boolean hasUpdate = false;

        // 更新用户名（如果提供）
        if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
            String newUsername = request.getUsername().trim();
            // 检查用户名长度
            if (newUsername.length() < 1 || newUsername.length() > 50) {
                throw new BusinessException("用户名长度必须在1-50个字符之间");
            }
            user.setUsername(newUsername);
            hasUpdate = true;
        }

        // 更新头像（如果提供）
        if (request.getAvatarFile() != null && !request.getAvatarFile().isEmpty()) {
            try {
                String avatarUrl = uploadUtil.uploadAvatar(request.getAvatarFile());
                user.setAvatar(avatarUrl);
                hasUpdate = true;
            } catch (Exception e) {
                throw new BusinessException("头像上传失败: " + e.getMessage());
            }
        }

        // 如果没有提供任何更新内容
        if (!hasUpdate) {
            throw new BusinessException("请提供要更新的用户名或头像");
        }

        // 更新最后修改时间
        user.setLastLoginAt(new Date());

        // 保存更新
        boolean updated = this.updateById(user);
        if (!updated) {
            throw new BusinessException("更新用户资料失败");
        }

        return user;
    }
}
