package com.shudong.post.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shudong.post.entity.Resonances;

/**
 * @author test
 * @description 针对表【resonances(共鸣表，存储用户与帖子产生共鸣的记录)】的数据库操作Mapper
 * 
 */
@Mapper
public interface ResonancesMapper extends BaseMapper<Resonances> {

}
