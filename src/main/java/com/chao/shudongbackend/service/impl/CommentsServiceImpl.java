package com.chao.shudongbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.shudongbackend.model.entity.Comments;
import com.chao.shudongbackend.service.CommentsService;
import com.chao.shudongbackend.mapper.CommentsMapper;
import org.springframework.stereotype.Service;

/**
* @author test
* @description 针对表【comments(评论表，用户对帖子的评论)】的数据库操作Service实现
* @createDate 2025-10-05 23:04:29
*/
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
    implements CommentsService{

}




