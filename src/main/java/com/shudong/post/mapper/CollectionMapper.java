package com.shudong.post.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shudong.post.entity.Collections;

/**
 * @author test
 * @description 针对表【collections(收藏表，存储用户收藏帖子的记录)】的数据库操作Mapper
 * 
 */
@Mapper
public interface CollectionMapper extends BaseMapper<Collections> {

}
