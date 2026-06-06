package com.shudong.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.post.entity.Resonances;
import com.shudong.post.mapper.ResonancesMapper;
import com.shudong.post.service.ResonancesService;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author test
 * @description 针对表【resonances(共鸣表，存储用户与帖子产生共鸣的记录)】的数据库操作Service实现
 */
@Service
public class ResonancesServiceImpl extends ServiceImpl<ResonancesMapper, Resonances>
    implements ResonancesService {

    @Override
    public void addResonance(Long userId, Long postId, String type) {
        Resonances resonance = new Resonances();
        resonance.setUserId(userId);
        resonance.setPostId(postId);
        resonance.setResonanceType(type);;
        this.save(resonance);
    }

    @Override
    public void removeResonance(Long userId, Long postId) {
        QueryWrapper<Resonances> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("post_id", postId);
        this.remove(queryWrapper);
    }

    @Override
    public List<Resonances> getResonancesByPostId(Long postId) {
        QueryWrapper<Resonances> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        return this.list(queryWrapper);
    }

    @Override
    public boolean hasResonanced(Long userId, Long postId) {
        QueryWrapper<Resonances> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("post_id", postId);
        return this.count(queryWrapper) > 0;
    }
}
