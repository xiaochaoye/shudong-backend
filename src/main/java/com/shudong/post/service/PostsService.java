package com.shudong.post.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shudong.post.dto.CreatePostRequest;
import com.shudong.post.dto.PostResponse;
import com.shudong.post.dto.UpdatePostRequest;
import com.shudong.post.entity.Posts;

public interface PostsService extends IService<Posts> {

    PostResponse createPost(Long userId, CreatePostRequest request);

    PostResponse getPost(Long postId);

    PostResponse updatePost(Long userId, Long postId, UpdatePostRequest request);

    void deletePost(Long userId, Long postId);

    Page<PostResponse> getUserPosts(Long userId, int page, int size);
}
