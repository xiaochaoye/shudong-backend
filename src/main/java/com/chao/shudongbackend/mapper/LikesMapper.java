package com.chao.shudongbackend.mapper;

import com.chao.shudongbackend.model.entity.Likes;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author test
* @description 针对表【likes(点赞表，记录用户对快乐区帖子的“同乐”，支持取消点赞)】的数据库操作Mapper
* @createDate 2025-10-05 23:04:29
* @Entity com.chao.shudongbackend.model.entity.Likes
*/
@Mapper
public interface LikesMapper extends BaseMapper<Likes> {

}




