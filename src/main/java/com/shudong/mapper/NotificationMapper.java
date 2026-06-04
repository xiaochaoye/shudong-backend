package com.shudong.mapper;

import com.shudong.model.entity.Notification;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author test
 * @description 针对表【notifications(站内通知表)】的数据库操作Mapper
 * @createDate 2025-10-05 23:04:29
 * @Entity com.shudong.model.entity.Notification
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {

}
