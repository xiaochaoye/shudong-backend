package com.shudong.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shudong.post.dto.CommentRequest;
import com.shudong.post.dto.CommentResponse;
import com.shudong.post.entity.Comments;

import java.util.List;

public interface CommentsService extends IService<Comments> {

    CommentResponse createComment(Long userId, Long postId, CommentRequest request);

    void deleteComment(Long userId, Long commentId);

    List<CommentResponse> getPostComments(Long postId);
}
