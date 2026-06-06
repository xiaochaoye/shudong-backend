package com.shudong.post.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shudong.post.entity.Tags;

/**
* @author test
* @description 针对表【tags(标签表，用于给帖子打标签)】的数据库操作Mapper
* 
*/
@Mapper
public interface TagsMapper extends BaseMapper<Tags> {

}




