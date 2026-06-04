package com.shudong.service;

import com.shudong.model.entity.SystemConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author test
 * @description 系统配置服务
 */
public interface SystemConfigService extends IService<SystemConfig> {

    /**
     * 根据配置键获取配置值
     * @param configKey 配置键
     * @return 配置值
     */
    String getConfigValue(String configKey);

    /**
     * 根据配置键获取配置值，如果不存在则返回默认值
     * @param configKey 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    String getConfigValue(String configKey, String defaultValue);

    /**
     * 设置配置值
     * @param configKey 配置键
     * @param configValue 配置值
     * @param configName 配置名称
     * @return 更新后的配置
     */
    SystemConfig setConfigValue(String configKey, String configValue, String configName);

    /**
     * 获取所有配置
     * @return 配置列表
     */
    List<SystemConfig> getAllConfigs();
}
