package com.shudong.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.post.entity.Posts;
import com.shudong.post.mapper.PostsMapper;
import com.shudong.post.service.PostsService;

import org.springframework.stereotype.Service;

/**
* @author test
* @description 针对表【posts(帖子表)】的数据库操作Service实现
*/
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts>
    implements PostsService {
}
