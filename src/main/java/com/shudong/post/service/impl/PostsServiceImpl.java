package com.shudong.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.common.exception.BusinessException;
import com.shudong.post.dto.CreatePostRequest;
import com.shudong.post.dto.PostResponse;
import com.shudong.post.dto.UpdatePostRequest;
import com.shudong.post.entity.Posts;
import com.shudong.post.mapper.PostsMapper;
import com.shudong.post.service.PostsService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts>
    implements PostsService {

    @Override
    @Transactional
    public PostResponse createPost(Long userId, CreatePostRequest request) {
        Posts post = new Posts();
        post.setUserId(userId);
        post.setTitle(request.getTitle());
        post.setPostBody(request.getPostBody());
        post.setPostStatus("PUBLISHED");
        post.setIsAnonymous(request.getIsAnonymous() != null ? request.getIsAnonymous() : 0);
        post.setIsPrivate(request.getIsPrivate() != null ? request.getIsPrivate() : 0);
        post.setViewCount(0);
        post.setResonanceCount(0);
        post.setCommentCount(0);
        post.setCreatedAt(new Date());

        this.save(post);

        return convertToResponse(post);
    }

    @Override
    public PostResponse getPost(Long postId) {
        Posts post = this.getById(postId);
        if (post == null || "DELETED".equals(post.getPostStatus())) {
            throw new BusinessException("帖子不存在");
        }
        // 浏览量+1
        post.setViewCount(post.getViewCount() + 1);
        this.updateById(post);

        return convertToResponse(post);
    }

    @Override
    @Transactional
    public PostResponse updatePost(Long userId, Long postId, UpdatePostRequest request) {
        Posts post = this.getById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        if (!post.getUserId().equals(userId)) {
            throw new BusinessException("无权编辑");
        }

        post.setTitle(request.getTitle());
        post.setPostBody(request.getPostBody());
        post.setUpdatedAt(new Date());
        this.updateById(post);

        return convertToResponse(post);
    }

    @Override
    @Transactional
    public void deletePost(Long userId, Long postId) {
        Posts post = this.getById(postId);
        if (post == null) {
            throw new BusinessException("帖子不存在");
        }
        if (!post.getUserId().equals(userId)) {
            throw new BusinessException("无权删除");
        }

        post.setPostStatus("DELETED");
        post.setDeletedAt(new Date());
        this.updateById(post);
    }

    @Override
    public List<PostResponse> getUserPosts(Long userId, int page, int size) {
        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                    .eq("post_status", "PUBLISHED")
                    .orderByDesc("created_at");

        List<Posts> posts = this.list(queryWrapper);

        return posts.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private PostResponse convertToResponse(Posts post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setPostBody(post.getPostBody());
        response.setIsAnonymous(post.getIsAnonymous());
        response.setIsPrivate(post.getIsPrivate());
        response.setViewCount(post.getViewCount());
        response.setResonanceCount(post.getResonanceCount());
        response.setCommentCount(post.getCommentCount());
        response.setCreatedAt(post.getCreatedAt());
        return response;
    }
}
