package com.shudong.mapper;

import com.shudong.model.entity.Posts;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author test
* @description 针对表【posts(帖子表，支持快乐区、难过区、许愿池)】的数据库操作Mapper
* @createDate 2025-10-05 23:04:29
* @Entity com.shudong.model.entity.Posts
*/
@Mapper
public interface PostsMapper extends BaseMapper<Posts> {

}




