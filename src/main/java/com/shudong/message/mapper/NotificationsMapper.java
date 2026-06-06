package com.shudong.message.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shudong.message.entity.Notifications;

/**
 * @author test
 * @description 针对表【notifications(站内通知表)】的数据库操作Mapper
 * 
 */
@Mapper
public interface NotificationsMapper extends BaseMapper<Notifications> {

}
