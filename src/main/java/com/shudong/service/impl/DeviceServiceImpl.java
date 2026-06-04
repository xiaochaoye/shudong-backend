package com.shudong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.mapper.DeviceMapper;
import com.shudong.model.entity.Device;
import com.shudong.service.DeviceService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author test
 * @description 针对表【devices(设备表，存储用户登录设备信息)】的数据库操作Service实现
 * @createDate 2025-10-05 23:04:30
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device>
    implements DeviceService {

    @Override
    public List<Device> getDevicesByUserId(Long userId) {
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.list(queryWrapper);
    }

    @Override
    public Device registerDevice(Long userId, String deviceId, String deviceName, String userAgent, String ipAddress) {
        Device device = new Device();
        device.setUserId(userId);
        device.setDeviceId(deviceId);
        device.setDeviceName(deviceName);
        device.setUserAgent(userAgent);
        device.setIpAddress(ipAddress);
        device.setLastLoginAt(new Date());
        device.setStatus("ACTIVE");
        device.setCreatedAt(new Date());
        this.save(device);
        return device;
    }

    @Override
    public void deactivateDevice(Long deviceId) {
        Device device = this.getById(deviceId);
        if (device != null) {
            device.setStatus("INACTIVE");
            this.updateById(device);
        }
    }

    @Override
    public void updateLastLogin(Long deviceId) {
        Device device = this.getById(deviceId);
        if (device != null) {
            device.setLastLoginAt(new Date());
            this.updateById(device);
        }
    }
}
