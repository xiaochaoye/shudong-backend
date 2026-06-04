package com.shudong.mapper;

import com.shudong.model.entity.Comments;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author test
* @description 针对表【comments(评论表，用户对帖子的评论)】的数据库操作Mapper
* @createDate 2025-10-05 23:04:29
* @Entity com.shudong.model.entity.Comments
*/
@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {

}




