package com.shudong.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.common.exception.BusinessException;
import com.shudong.post.dto.CommentRequest;
import com.shudong.post.dto.CommentResponse;
import com.shudong.post.entity.Comments;
import com.shudong.post.mapper.CommentsMapper;
import com.shudong.post.service.CommentsService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
    implements CommentsService {

    @Override
    @Transactional
    public CommentResponse createComment(Long userId, Long postId, CommentRequest request) {
        Comments comment = new Comments();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setParentId(request.getParentId());
        comment.setCommentBody(request.getCommentBody());
        comment.setCommentStatus("ACTIVE");
        comment.setCreatedAt(new Date());

        this.save(comment);

        return convertToResponse(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comments comment = this.getById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException("无权删除");
        }

        // 级联删除子评论
        deleteChildrenComments(commentId);

        comment.setCommentStatus("DELETED");
        comment.setDeletedAt(new Date());
        this.updateById(comment);
    }

    private void deleteChildrenComments(Long parentId) {
        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId).eq("comment_status", "ACTIVE");

        List<Comments> children = this.list(queryWrapper);
        for (Comments child : children) {
            child.setCommentStatus("DELETED");
            child.setDeletedAt(new Date());
            this.updateById(child);
        }
    }

    @Override
    public Page<CommentResponse> getPostComments(Long postId, int page, int size) {
        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId)
                    .eq("comment_status", "ACTIVE")
                    .orderByDesc("created_at");

        Page<Comments> commentPage = this.page(new Page<>(page, size), queryWrapper);

        Page<CommentResponse> responsePage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        responsePage.setRecords(commentPage.getRecords().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()));
        return responsePage;
    }

    private CommentResponse convertToResponse(Comments comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setPostId(comment.getPostId());
        response.setUserId(comment.getUserId());
        response.setParentId(comment.getParentId());
        response.setCommentBody(comment.getCommentBody());
        response.setCreatedAt(comment.getCreatedAt());
        return response;
    }
}
