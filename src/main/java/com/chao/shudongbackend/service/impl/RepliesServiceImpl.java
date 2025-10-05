package com.chao.shudongbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.shudongbackend.model.entity.Replies;
import com.chao.shudongbackend.service.RepliesService;
import com.chao.shudongbackend.mapper.RepliesMapper;
import org.springframework.stereotype.Service;

/**
* @author test
* @description 针对表【replies(难过区邮件回复记录表，支持管理员或系统发送)】的数据库操作Service实现
* @createDate 2025-10-05 23:04:29
*/
@Service
public class RepliesServiceImpl extends ServiceImpl<RepliesMapper, Replies>
    implements RepliesService{

}




