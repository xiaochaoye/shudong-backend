package com.chao.shudongbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.shudongbackend.model.entity.Posts;
import com.chao.shudongbackend.service.PostsService;
import com.chao.shudongbackend.mapper.PostsMapper;
import org.springframework.stereotype.Service;

/**
* @author test
* @description 针对表【posts(帖子表，支持快乐区、难过区、许愿池)】的数据库操作Service实现
* @createDate 2025-10-05 23:04:29
*/
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts>
    implements PostsService{

}




