package com.shudong.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shudong.post.entity.Collections;

import java.util.List;

/**
 * @author test
 * @description 针对表【collections(收藏表，存储用户收藏帖子的记录)】的数据库操作Service
 */
public interface CollectionService extends IService<Collections> {

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
    List<Collections> getCollectionsByUserId(Long userId);

    /**
     * 检查用户是否已收藏
     * @param userId 用户ID
     * @param postId 帖子ID
     * @return 是否已收藏
     */
    boolean hasCollected(Long userId, Long postId);
}
