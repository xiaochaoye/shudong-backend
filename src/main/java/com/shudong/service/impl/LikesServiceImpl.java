package com.shudong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.model.entity.Likes;
import com.shudong.service.LikesService;
import com.shudong.mapper.LikesMapper;
import org.springframework.stereotype.Service;

/**
* @author test
* @description 针对表【likes(点赞表，记录用户对快乐区帖子的“同乐”，支持取消点赞)】的数据库操作Service实现
* @createDate 2025-10-05 23:04:29
*/
@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes>
    implements LikesService{

}




