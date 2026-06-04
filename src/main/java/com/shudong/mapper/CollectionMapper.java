package com.shudong.mapper;

import com.shudong.model.entity.Collection;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author test
 * @description 针对表【collections(收藏表，存储用户收藏帖子的记录)】的数据库操作Mapper
 * @createDate 2025-10-05 23:04:29
 * @Entity com.shudong.model.entity.Collection
 */
@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {

}
