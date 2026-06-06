package com.shudong.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shudong.post.entity.Resonances;

import java.util.List;

/**
 * @author test
 * @description 针对表【resonances(共鸣表，存储用户与帖子产生共鸣的记录)】的数据库操作Service
 */
public interface ResonancesService extends IService<Resonances> {

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
    List<Resonances> getResonancesByPostId(Long postId);

    /**
     * 检查用户是否已共鸣
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否已共鸣
     */
    boolean hasResonanced(Long userId, Long postId);
}
