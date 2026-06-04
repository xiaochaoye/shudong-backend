package com.shudong.mapper;

import com.shudong.model.entity.PickRecord;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author test
 * @description 针对表【pick_records(拾取记录表)】的数据库操作Mapper
 * @createDate 2025-10-05 23:04:29
 * @Entity com.shudong.model.entity.PickRecord
 */
@Mapper
public interface PickRecordMapper extends BaseMapper<PickRecord> {

}
