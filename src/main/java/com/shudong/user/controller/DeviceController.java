package com.shudong.user.controller;

import com.shudong.common.response.Result;
import com.shudong.user.entity.Devices;
import com.shudong.user.service.DevicesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 设备管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DevicesService deviceService;

    /**
     * 获取当前用户设备列表
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @return 设备列表
     */
    @GetMapping
    public Result<List<Devices>> getDevices(@RequestAttribute("userId") Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            List<Devices> devices = deviceService.getDevicesByUserId(userId);
            return Result.success(devices);
        } catch (Exception e) {
            log.error("获取设备列表失败: {}", e.getMessage());
            return Result.error("获取设备列表失败");
        }
    }

    /**
     * 注册设备
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param params 设备信息，包含 deviceId, deviceName, userAgent, ipAddress
     * @return 注册的设备信息
     */
    @PostMapping("/register")
    public Result<Devices> registerDevice(@RequestAttribute("userId") Long userId,
                                         @RequestBody Map<String, String> params) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            String deviceId = params.get("deviceId");
            String deviceName = params.get("deviceName");
            String userAgent = params.get("userAgent");
            String ipAddress = params.get("ipAddress");

            Devices device = deviceService.registerDevices(userId, deviceId, deviceName, userAgent, ipAddress);
            return Result.success("设备注册成功", device);
        } catch (Exception e) {
            log.error("设备注册失败: {}", e.getMessage());
            return Result.error("设备注册失败");
        }
    }

    /**
     * 停用设备
     *
     * @param userId   当前用户ID（从JWT token解析）
     * @param deviceId 设备记录ID
     * @return 停用结果
     */
    @PutMapping("/{deviceId}/deactivate")
    public Result<Void> deactivateDevice(@RequestAttribute("userId") Long userId,
                                       @PathVariable Long deviceId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            deviceService.deactivateDevices(deviceId);
            return Result.success("设备已停用");
        } catch (Exception e) {
            log.error("停用设备失败: {}", e.getMessage());
            return Result.error("停用设备失败");
        }
    }

    /**
     * 更新最后登录时间
     *
     * @param userId   当前用户ID（从JWT token解析）
     * @param deviceId 设备记录ID
     * @return 更新结果
     */
    @PutMapping("/{deviceId}/refresh-login")
    public Result<Void> refreshLogin(@RequestAttribute("userId") Long userId,
                                     @PathVariable Long deviceId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            deviceService.updateLastLogin(deviceId);
            return Result.success("登录时间已更新");
        } catch (Exception e) {
            log.error("更新登录时间失败: {}", e.getMessage());
            return Result.error("更新登录时间失败");
        }
    }
}
