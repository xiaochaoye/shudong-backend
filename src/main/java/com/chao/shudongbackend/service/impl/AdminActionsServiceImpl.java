package com.chao.shudongbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.shudongbackend.model.entity.AdminActions;
import com.chao.shudongbackend.service.AdminActionsService;
import com.chao.shudongbackend.mapper.AdminActionsMapper;
import org.springframework.stereotype.Service;

/**
* @author test
* @description 针对表【admin_actions(管理员操作日志表，用于审计)】的数据库操作Service实现
* @createDate 2025-10-05 23:04:29
*/
@Service
public class AdminActionsServiceImpl extends ServiceImpl<AdminActionsMapper, AdminActions>
    implements AdminActionsService{

}




