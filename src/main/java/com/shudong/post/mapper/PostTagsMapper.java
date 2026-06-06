package com.shudong.post.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shudong.post.entity.PostTags;

/**
* @author test
* @description 针对表【post_tags(帖子与标签的关联表，实现多对多关系，使用代理主键 id 支持 MyBatis-Plus)】的数据库操作Mapper
* 
*/
@Mapper
public interface PostTagsMapper extends BaseMapper<PostTags> {

}




