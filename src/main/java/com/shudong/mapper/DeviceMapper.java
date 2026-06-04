package com.shudong.mapper;

import com.shudong.model.entity.Device;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author test
* @description 针对表【devices(设备表，存储用户登录设备信息)】的数据库操作Mapper
* @createDate 2025-10-05 23:04:30
* @Entity com.shudong.model.entity.Device
*/
@Mapper
public interface DeviceMapper extends BaseMapper<Device> {

}
