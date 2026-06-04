package com.shudong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.mapper.SystemConfigMapper;
import com.shudong.model.entity.SystemConfig;
import com.shudong.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author test
 * @description 系统配置服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig>
    implements SystemConfigService {

    @Override
    public String getConfigValue(String configKey) {
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_key", configKey);
        SystemConfig config = this.getOne(queryWrapper);
        return config != null ? config.getConfigValue() : null;
    }

    @Override
    public String getConfigValue(String configKey, String defaultValue) {
        String value = getConfigValue(configKey);
        return value != null ? value : defaultValue;
    }

    @Override
    public SystemConfig setConfigValue(String configKey, String configValue, String configName) {
        QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_key", configKey);
        SystemConfig config = this.getOne(queryWrapper);
        Date now = new Date();
        if (config == null) {
            config = new SystemConfig();
            config.setConfigKey(configKey);
            config.setConfigName(configName != null ? configName : configKey);
            config.setConfigValue(configValue);
            config.setCreatedAt(now);
            config.setUpdatedAt(now);
            this.save(config);
        } else {
            config.setConfigValue(configValue);
            if (configName != null) {
                config.setConfigName(configName);
            }
            config.setUpdatedAt(now);
            this.updateById(config);
        }
        log.info("更新系统配置 {} = {}", configKey, configValue);
        return config;
    }

    @Override
    public List<SystemConfig> getAllConfigs() {
        return this.list();
    }
}
