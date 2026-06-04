package com.shudong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.model.entity.Resonance;
import com.shudong.service.ResonanceService;
import com.shudong.mapper.ResonanceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author test
 * @description 针对表【resonances(共鸣表，存储用户与帖子产生共鸣的记录)】的数据库操作Service实现
 * @createDate 2025-10-05 23:04:29
 */
@Service
public class ResonanceServiceImpl extends ServiceImpl<ResonanceMapper, Resonance>
    implements ResonanceService {

    @Override
    public void addResonance(Long userId, Long postId, String type) {
        Resonance resonance = new Resonance();
        resonance.setUserId(userId);
        resonance.setPostId(postId);
        resonance.setType(type);
        this.save(resonance);
    }

    @Override
    public void removeResonance(Long userId, Long postId) {
        QueryWrapper<Resonance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("post_id", postId);
        this.remove(queryWrapper);
    }

    @Override
    public List<Resonance> getResonancesByPostId(Long postId) {
        QueryWrapper<Resonance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        return this.list(queryWrapper);
    }

    @Override
    public boolean hasResonanced(Long userId, Long postId) {
        QueryWrapper<Resonance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("post_id", postId);
        return this.count(queryWrapper) > 0;
    }
}
