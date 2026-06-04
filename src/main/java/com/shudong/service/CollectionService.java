package com.shudong.service;

import com.shudong.model.entity.Collection;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author test
 * @description 针对表【collections(收藏表，存储用户收藏帖子的记录)】的数据库操作Service
 * @createDate 2025-10-05 23:04:29
 */
public interface CollectionService extends IService<Collection> {

    /**
     * 添加收藏
     * @param userId 用户ID
     * @param postId 帖子ID
     * @param category 收藏分类
     */
    void addCollection(Long userId, Long postId, String category);

    /**
     * 取消收藏
     * @param userId 用户ID
     * @param postId 帖子ID
     */
    void removeCollection(Long userId, Long postId);

    /**
     * 获取用户收藏列表
     * @param userId 用户ID
     * @return 收藏列表
     */
    List<Collection> getCollectionsByUserId(Long userId);

    /**
     * 检查用户是否已收藏
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否已收藏
     */
    boolean hasCollected(Long userId, Long postId);
}
