package com.shudong.post.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.common.response.Result;
import com.shudong.post.dto.CommentRequest;
import com.shudong.post.dto.CommentResponse;
import com.shudong.post.service.CommentsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentsService commentsService;

    @PostMapping
    public Result<CommentResponse> createComment(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request) {
        try {
            CommentResponse response = commentsService.createComment(userId, postId, request);
            return Result.success("评论成功", response);
        } catch (Exception e) {
            log.error("评论失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{commentId}")
    public Result<Void> deleteComment(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long commentId) {
        try {
            commentsService.deleteComment(userId, commentId);
            return Result.success("删除评论成功");
        } catch (Exception e) {
            log.error("删除评论失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @GetMapping
    public Result<Page<CommentResponse>> getPostComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<CommentResponse> comments = commentsService.getPostComments(postId, page, size);
            return Result.success(comments);
        } catch (Exception e) {
            log.error("获取评论列表失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
