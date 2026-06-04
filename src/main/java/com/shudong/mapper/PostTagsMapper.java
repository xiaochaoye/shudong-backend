package com.shudong.mapper;

import com.shudong.model.entity.PostTags;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author test
* @description 针对表【post_tags(帖子与标签的关联表，实现多对多关系，使用代理主键 id 支持 MyBatis-Plus)】的数据库操作Mapper
* @createDate 2025-10-07 21:44:42
* @Entity com.shudong.model.entity.PostTags
*/
@Mapper
public interface PostTagsMapper extends BaseMapper<PostTags> {

}




