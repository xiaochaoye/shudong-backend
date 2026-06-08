package com.shudong.post.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.common.response.Result;
import com.shudong.post.dto.CreatePostRequest;
import com.shudong.post.dto.PostResponse;
import com.shudong.post.dto.UpdatePostRequest;
import com.shudong.post.service.PostsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostsService postsService;

    @PostMapping
    public Result<PostResponse> createPost(
            @RequestAttribute("userId") Long userId,
            @Valid @RequestBody CreatePostRequest request) {
        try {
            PostResponse response = postsService.createPost(userId, request);
            return Result.success("发帖成功", response);
        } catch (Exception e) {
            log.error("发帖失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{postId}")
    public Result<PostResponse> getPost(@PathVariable Long postId) {
        try {
            PostResponse response = postsService.getPost(postId);
            return Result.success(response);
        } catch (Exception e) {
            log.error("获取帖子失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{postId}")
    public Result<PostResponse> updatePost(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request) {
        try {
            PostResponse response = postsService.updatePost(userId, postId, request);
            return Result.success("更新成功", response);
        } catch (Exception e) {
            log.error("更新帖子失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{postId}")
    public Result<Void> deletePost(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long postId) {
        try {
            postsService.deletePost(userId, postId);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除帖子失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public Result<Page<PostResponse>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<PostResponse> posts = postsService.getUserPosts(userId, page, size);
            return Result.success(posts);
        } catch (Exception e) {
            log.error("获取用户帖子列表失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
