package com.shudong.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shudong.user.entity.Devices;

/**
* @author test
* @description 针对表【devices(设备表，存储用户登录设备信息)】的数据库操作Mapper
* 
*/
@Mapper
public interface DevicesMapper extends BaseMapper<Devices> {

}
