package com.shudong.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.user.entity.Devices;
import com.shudong.user.mapper.DevicesMapper;
import com.shudong.user.service.DevicesService;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author test
 * @description 针对表【devices(设备表，存储用户登录设备信息)】的数据库操作Service实现
 */
@Service
public class DevicesServiceImpl extends ServiceImpl<DevicesMapper, Devices>
    implements DevicesService {

    @Override
    public List<Devices> getDevicesByUserId(Long userId) {
        QueryWrapper<Devices> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.list(queryWrapper);
    }

    @Override
    public Devices registerDevices(Long userId, String deviceId, String deviceName, String userAgent, String ipAddress) {
        Devices device = new Devices();
        device.setUserId(userId);
        device.setDeviceId(deviceId);
        device.setDeviceName(deviceName);
        device.setUserAgent(userAgent);
        device.setIpAddress(ipAddress);
        device.setLastLoginAt(new Date());
        device.setDeviceStatus("ACTIVE");
        device.setCreatedAt(new Date());
        this.save(device);
        return device;
    }

    @Override
    public void deactivateDevices(Long deviceId) {
        Devices device = this.getById(deviceId);
        if (device != null) {
            device.setDeviceStatus("INACTIVE");
            this.updateById(device);
        }
    }

    @Override
    public void updateLastLogin(Long deviceId) {
        Devices device = this.getById(deviceId);
        if (device != null) {
            device.setLastLoginAt(new Date());
            this.updateById(device);
        }
    }
}
