package com.shudong.post.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shudong.post.entity.Comments;

/**
* @author test
* @description 针对表【comments(评论表，用户对帖子的评论)】的数据库操作Mapper
* 
*/
@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {

}




