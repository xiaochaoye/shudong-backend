package com.shudong.service;

import com.shudong.model.entity.Device;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author test
 * @description 针对表【devices(设备表，存储用户登录设备信息)】的数据库操作Service
 * @createDate 2025-10-05 23:04:30
 */
public interface DeviceService extends IService<Device> {

    /**
     * 获取用户设备列表
     * @param userId 用户ID
     * @return 设备列表
     */
    List<Device> getDevicesByUserId(Long userId);

    /**
     * 注册设备
     * @param userId 用户ID
     * @param deviceId 设备唯一标识
     * @param deviceName 设备名称
     * @param userAgent 用户代理字符串
     * @param ipAddress IP地址
     * @return 注册的设备信息
     */
    Device registerDevice(Long userId, String deviceId, String deviceName, String userAgent, String ipAddress);

    /**
     * 停用设备
     * @param deviceId 设备记录ID
     */
    void deactivateDevice(Long deviceId);

    /**
     * 更新最后登录时间
     * @param deviceId 设备记录ID
     */
    void updateLastLogin(Long deviceId);
}
