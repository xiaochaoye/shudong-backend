package com.shudong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.exception.BusinessException;
import com.shudong.model.dto.LoginRequestDTO;
import com.shudong.model.dto.RegisterRequestDTO;
import com.shudong.model.entity.Users;
import com.shudong.model.dto.UserRequestDTO;
import com.shudong.service.MailService;
import com.shudong.service.UsersService;
import com.shudong.mapper.UsersMapper;
import com.shudong.utils.PasswordUtil;
import com.shudong.utils.RedisUtil;
import com.shudong.utils.UploadUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author test
* @description 针对表【users(用户表，存储注册用户信息)】的数据库操作Service实现
* @createDate 2025-10-05 23:04:30
*/
@Service
@RequiredArgsConstructor
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

    private final RedisUtil redisUtil;

    private final PasswordUtil passwordUtil;

    private final MailService mailService;

    private final UploadUtil uploadUtil;

    @Override
    public boolean sendRegisterCode(String email) {
        // 检查邮箱是否已注册
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        Users existingUser = this.getOne(queryWrapper);
        if (existingUser != null) {
            throw new BusinessException("该邮箱已被注册");
        }

        // 生成验证码
        String code = passwordUtil.generateVerificationCode();
        
        // 存储验证码到Redis
        redisUtil.setVerificationCode(email, code, "register");
        
        try {
            mailService.sendVerificationEmail(email, "树洞 - 注册验证码", code);
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
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setIsActive(1);
        user.setIsAdmin(0);

        // 保存用户
        boolean saved = this.save(user);
        if (!saved) {
            throw new BusinessException("注册失败，请稍后重试");
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
        if (user.getIsActive() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        // 验证密码
        if (!passwordUtil.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("邮箱或密码错误");
        }

        // 更新最后登录时间
        user.setUpdatedAt(new Date());
        this.updateById(user);

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
        if (user.getIsActive() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        // 生成验证码
        String code = passwordUtil.generateVerificationCode();
        
        // 存储验证码到Redis
        redisUtil.setVerificationCode(email, code, "reset");
        
        try {
            mailService.sendVerificationEmail(email, "树洞 - 重置密码验证码", code);
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
        if (user.getIsActive() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        if (!passwordUtil.validatePasswordFormat(newPassword)) {
            throw new BusinessException("新密码格式不符合要求：长度8-20位，必须包含字母、特殊符号和数字");
        }

        // 更新密码
        user.setPasswordHash(passwordUtil.encodePassword(newPassword));
        user.setUpdatedAt(new Date());
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
        if (user.getIsActive() == 0) {
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
        user.setUpdatedAt(new Date());

        // 保存更新
        boolean updated = this.updateById(user);
        if (!updated) {
            throw new BusinessException("更新用户资料失败");
        }

        return user;
    }
}
