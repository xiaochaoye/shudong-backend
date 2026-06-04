package com.shudong.controller;

import com.shudong.model.dto.Result;
import com.shudong.model.entity.Collection;
import com.shudong.service.CollectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 收藏管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    /**
     * 添加收藏
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param params 请求参数，包含 postId, category
     * @return 添加结果
     */
    @PostMapping
    public Result<Void> addCollection(@RequestAttribute("userId") Long userId,
                                     @RequestBody Map<String, String> params) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            Long postId = Long.valueOf(params.get("postId"));
            String category = params.get("category");
            collectionService.addCollection(userId, postId, category);
            return Result.success("添加收藏成功");
        } catch (Exception e) {
            log.error("添加收藏失败: {}", e.getMessage());
            return Result.error("添加收藏失败");
        }
    }

    /**
     * 取消收藏
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param postId 帖子ID
     * @return 取消结果
     */
    @DeleteMapping
    public Result<Void> removeCollection(@RequestAttribute("userId") Long userId,
                                         @RequestParam Long postId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            collectionService.removeCollection(userId, postId);
            return Result.success("取消收藏成功");
        } catch (Exception e) {
            log.error("取消收藏失败: {}", e.getMessage());
            return Result.error("取消收藏失败");
        }
    }

    /**
     * 获取当前用户收藏列表
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @return 收藏列表
     */
    @GetMapping
    public Result<List<Collection>> getCollections(@RequestAttribute("userId") Long userId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            List<Collection> collections = collectionService.getCollectionsByUserId(userId);
            return Result.success(collections);
        } catch (Exception e) {
            log.error("获取收藏列表失败: {}", e.getMessage());
            return Result.error("获取收藏列表失败");
        }
    }

    /**
     * 检查当前用户是否已收藏某帖子
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param postId 帖子ID
     * @return 是否已收藏
     */
    @GetMapping("/check")
    public Result<Boolean> checkCollection(@RequestAttribute("userId") Long userId,
                                           @RequestParam Long postId) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            boolean hasCollected = collectionService.hasCollected(userId, postId);
            return Result.success(hasCollected);
        } catch (Exception e) {
            log.error("检查收藏状态失败: {}", e.getMessage());
            return Result.error("检查收藏状态失败");
        }
    }
}
