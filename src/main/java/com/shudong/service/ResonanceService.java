package com.shudong.service;

import com.shudong.model.entity.Resonance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author test
 * @description 针对表【resonances(共鸣表，存储用户与帖子产生共鸣的记录)】的数据库操作Service
 * @createDate 2025-10-05 23:04:29
 */
public interface ResonanceService extends IService<Resonance> {

    /**
     * 添加共鸣
     * @param userId 用户ID
     * @param postId 帖子ID
     * @param type 共鸣类型
     */
    void addResonance(Long userId, Long postId, String type);

    /**
     * 取消共鸣
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void removeResonance(Long userId, Long postId);

    /**
     * 获取帖子的共鸣列表
     * @param postId 帖子ID
     * @return 共鸣列表
     */
    List<Resonance> getResonancesByPostId(Long postId);

    /**
     * 检查用户是否已共鸣
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否已共鸣
     */
    boolean hasResonanced(Long userId, Long postId);
}
