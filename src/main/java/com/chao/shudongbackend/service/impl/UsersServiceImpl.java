package com.chao.shudongbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.shudongbackend.exception.BusinessException;
import com.chao.shudongbackend.model.dto.LoginRequestDTO;
import com.chao.shudongbackend.model.dto.RegisterRequestDTO;
import com.chao.shudongbackend.model.entity.Users;
import com.chao.shudongbackend.service.MailService;
import com.chao.shudongbackend.service.UsersService;
import com.chao.shudongbackend.mapper.UsersMapper;
import com.chao.shudongbackend.utils.PasswordUtil;
import com.chao.shudongbackend.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author test
* @description 针对表【users(用户表，存储注册用户信息)】的数据库操作Service实现
* @createDate 2025-10-05 23:04:30
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private MailService mailService;

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

        // 发送邮件
        String subject = "树洞 - 注册验证码";
        String content = "您的注册验证码是：" + code + "，有效期为5分钟。";
        
        try {
            mailService.sendHtmlEmail(email, subject, content, "noreply@shudong.com");
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

        // 发送邮件
        String subject = "树洞 - 重置密码验证码";
        String content = "您的重置密码验证码是：" + code + "，有效期为5分钟。";
        
        try {
            mailService.sendHtmlEmail(email, subject, content, "noreply@shudong.com");
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
}
