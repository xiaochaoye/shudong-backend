package com.shudong.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.post.entity.Comments;
import com.shudong.post.mapper.CommentsMapper;
import com.shudong.post.service.CommentsService;

import org.springframework.stereotype.Service;

/**
* @author test
* @description 针对表【comments(评论表，用户对帖子的评论)】的数据库操作Service实现
*/
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
    implements CommentsService{

}




