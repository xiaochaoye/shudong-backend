package com.shudong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.model.entity.Collection;
import com.shudong.service.CollectionService;
import com.shudong.mapper.CollectionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author test
 * @description 针对表【collections(收藏表，存储用户收藏帖子的记录)】的数据库操作Service实现
 * @createDate 2025-10-05 23:04:29
 */
@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection>
    implements CollectionService {

    @Override
    public void addCollection(Long userId, Long postId, String category) {
        Collection collection = new Collection();
        collection.setUserId(userId);
        collection.setPostId(postId);
        collection.setCategory(category);
        this.save(collection);
    }

    @Override
    public void removeCollection(Long userId, Long postId) {
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("post_id", postId);
        this.remove(queryWrapper);
    }

    @Override
    public List<Collection> getCollectionsByUserId(Long userId) {
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.list(queryWrapper);
    }

    @Override
    public boolean hasCollected(Long userId, Long postId) {
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("post_id", postId);
        return this.count(queryWrapper) > 0;
    }
}
