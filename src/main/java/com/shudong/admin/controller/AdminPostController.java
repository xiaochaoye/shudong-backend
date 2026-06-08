package com.shudong.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.admin.service.AdminLogService;
import com.shudong.common.response.Result;
import com.shudong.post.entity.Posts;
import com.shudong.post.mapper.PostsMapper;
import com.shudong.user.entity.Users;
import com.shudong.user.mapper.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final PostsMapper postsMapper;
    private final UsersMapper usersMapper;
    private final AdminLogService adminLogService;

    @GetMapping
    public Result<Page<Posts>> getPostList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        LambdaQueryWrapper<Posts> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Posts::getTitle, keyword).or().like(Posts::getPostBody, keyword));
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Posts::getPostStatus, status);
        }
        wrapper.orderByDesc(Posts::getCreatedAt);
        return Result.success(postsMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @DeleteMapping("/{postId}")
    public Result<Void> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        Posts post = postsMapper.selectById(postId);
        if (post == null) {
            return Result.notFound();
        }
        post.setPostStatus("DELETED");
        post.setDeletedAt(new java.util.Date());
        postsMapper.updateById(post);
        adminLogService.logAction(getCurrentAdminId(), "DELETE_POST", "POST", postId, "删除帖子", getClientIp(request));
        return Result.success();
    }

    private Long getCurrentAdminId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        String email = auth.getName();
        Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("email", email));
        return user != null ? user.getId() : null;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
