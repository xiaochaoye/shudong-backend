package com.shudong.pick.service;

import com.shudong.pick.dto.PickResponse;

public interface PickService {

    PickResponse pickPost(Long userId, int limit);

    void markAsReplied(Long userId, Long postId);

    boolean isRateLimited(Long userId);

    int getTodayPickCount(Long userId);

    String testSaying();
}
