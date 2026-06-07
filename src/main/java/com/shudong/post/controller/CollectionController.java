package com.shudong.post.controller;

import com.shudong.common.response.Result;
import com.shudong.post.dto.CollectionRequest;
import com.shudong.post.entity.Collections;
import com.shudong.post.service.CollectionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public Result<Void> addCollection(@RequestAttribute("userId") Long userId,
                                      @Valid @RequestBody CollectionRequest request) {
        try {
            collectionService.addCollection(userId, request.getPostId(), request.getCategory());
            return Result.success("添加收藏成功");
        } catch (Exception e) {
            log.error("添加收藏失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping
    public Result<Void> removeCollection(@RequestAttribute("userId") Long userId,
                                         @RequestParam Long postId) {
        try {
            collectionService.removeCollection(userId, postId);
            return Result.success("取消收藏成功");
        } catch (Exception e) {
            log.error("取消收藏失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @GetMapping
    public Result<List<Collections>> getCollections(@RequestAttribute("userId") Long userId) {
        try {
            List<Collections> collections = collectionService.getCollectionsByUserId(userId);
            return Result.success(collections);
        } catch (Exception e) {
            log.error("获取收藏列表失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/check")
    public Result<Boolean> checkCollection(@RequestAttribute("userId") Long userId,
                                           @RequestParam Long postId) {
        try {
            boolean hasCollected = collectionService.hasCollected(userId, postId);
            return Result.success(hasCollected);
        } catch (Exception e) {
            log.error("检查收藏状态失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
