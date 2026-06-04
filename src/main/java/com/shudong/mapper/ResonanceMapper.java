package com.shudong.mapper;

import com.shudong.model.entity.Resonance;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author test
 * @description 针对表【resonances(共鸣表，存储用户与帖子产生共鸣的记录)】的数据库操作Mapper
 * @createDate 2025-10-05 23:04:29
 * @Entity com.shudong.model.entity.Resonance
 */
@Mapper
public interface ResonanceMapper extends BaseMapper<Resonance> {

}
