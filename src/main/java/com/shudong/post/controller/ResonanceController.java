package com.shudong.post.controller;

import com.shudong.common.response.Result;
import com.shudong.post.dto.ResonanceRequest;
import com.shudong.post.entity.Resonances;
import com.shudong.post.service.ResonancesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/resonances")
@RequiredArgsConstructor
public class ResonanceController {

    private final ResonancesService resonanceService;

    @PostMapping
    public Result<Void> addResonance(@RequestAttribute("userId") Long userId,
                                     @Valid @RequestBody ResonanceRequest request) {
        try {
            resonanceService.addResonance(userId, request.getPostId(), request.getResonanceType());
            return Result.success("添加共鸣成功");
        } catch (Exception e) {
            log.error("添加共鸣失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping
    public Result<Void> removeResonance(@RequestAttribute("userId") Long userId,
                                        @RequestParam Long postId) {
        try {
            resonanceService.removeResonance(userId, postId);
            return Result.success("取消共鸣成功");
        } catch (Exception e) {
            log.error("取消共鸣失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/post/{postId}")
    public Result<List<Resonances>> getResonancesByPostId(@PathVariable Long postId) {
        try {
            List<Resonances> resonances = resonanceService.getResonancesByPostId(postId);
            return Result.success(resonances);
        } catch (Exception e) {
            log.error("获取共鸣列表失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/check")
    public Result<Boolean> checkResonance(@RequestAttribute("userId") Long userId,
                                          @RequestParam Long postId) {
        try {
            boolean hasResonanced = resonanceService.hasResonanced(userId, postId);
            return Result.success(hasResonanced);
        } catch (Exception e) {
            log.error("检查共鸣状态失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
