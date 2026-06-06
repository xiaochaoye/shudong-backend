package com.shudong.post.controller;

import com.shudong.common.response.Result;
import com.shudong.post.entity.Resonances;
import com.shudong.post.service.ResonancesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 共鸣管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/resonances")
@RequiredArgsConstructor
public class ResonanceController {

    private final ResonancesService resonanceService;

    /**
     * 添加共鸣
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param params 请求参数，包含 postId, type
     * @return 添加结果
     */
    @PostMapping
    public Result<Void> addResonance(@RequestAttribute("userId") Long userId,
                                     @RequestBody Map<String, String> params) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            Long postId = Long.valueOf(params.get("postId"));
            String type = params.get("type");
            resonanceService.addResonance(userId, postId, type);
            return Result.success("添加共鸣成功");
        } catch (Exception e) {
            log.error("添加共鸣失败: {}", e.getMessage());
            return Result.error("添加共鸣失败");
        }
    }

    /**
     * 取消共鸣
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param postId 帖子ID
     * @return 取消结果
     */
    @DeleteMapping
    public Result<Void> removeResonance(@RequestAttribute("userId") Long userId,
                                        @RequestParam Long postId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            resonanceService.removeResonance(userId, postId);
            return Result.success("取消共鸣成功");
        } catch (Exception e) {
            log.error("取消共鸣失败: {}", e.getMessage());
            return Result.error("取消共鸣失败");
        }
    }

    /**
     * 获取帖子的共鸣列表
     *
     * @param postId 帖子ID
     * @return 共鸣列表
     */
    @GetMapping("/post/{postId}")
    public Result<List<Resonances>> getResonancesByPostId(@PathVariable Long postId) {
        try {
            List<Resonances> resonances = resonanceService.getResonancesByPostId(postId);
            return Result.success(resonances);
        } catch (Exception e) {
            log.error("获取共鸣列表失败: {}", e.getMessage());
            return Result.error("获取共鸣列表失败");
        }
    }

    /**
     * 检查当前用户是否已共鸣某帖子
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param postId 帖子ID
     * @return 是否已共鸣
     */
    @GetMapping("/check")
    public Result<Boolean> checkResonance(@RequestAttribute("userId") Long userId,
                                          @RequestParam Long postId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            boolean hasResonanced = resonanceService.hasResonanced(userId, postId);
            return Result.success(hasResonanced);
        } catch (Exception e) {
            log.error("检查共鸣状态失败: {}", e.getMessage());
            return Result.error("检查共鸣状态失败");
        }
    }
}
