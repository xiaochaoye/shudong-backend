package com.shudong.post.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shudong.post.entity.Posts;

/**
* @author test
* @description 针对表【posts(帖子表)】的数据库操作Mapper
* 
*/
@Mapper
public interface PostsMapper extends BaseMapper<Posts> {

}




