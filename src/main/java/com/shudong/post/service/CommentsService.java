package com.shudong.post.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shudong.post.dto.CommentRequest;
import com.shudong.post.dto.CommentResponse;
import com.shudong.post.entity.Comments;

public interface CommentsService extends IService<Comments> {

    CommentResponse createComment(Long userId, Long postId, CommentRequest request);

    void deleteComment(Long userId, Long commentId);

    Page<CommentResponse> getPostComments(Long postId, int page, int size);
}
