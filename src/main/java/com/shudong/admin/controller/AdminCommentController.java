package com.shudong.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shudong.admin.service.AdminLogService;
import com.shudong.common.response.Result;
import com.shudong.post.entity.Comments;
import com.shudong.post.mapper.CommentsMapper;
import com.shudong.user.entity.Users;
import com.shudong.user.mapper.UsersMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentsMapper commentsMapper;
    private final UsersMapper usersMapper;
    private final AdminLogService adminLogService;

    @GetMapping
    public Result<Page<Comments>> getCommentList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Long postId) {
        LambdaQueryWrapper<Comments> wrapper = new LambdaQueryWrapper<>();
        if (postId != null) {
            wrapper.eq(Comments::getPostId, postId);
        }
        wrapper.orderByDesc(Comments::getCreatedAt);
        return Result.success(commentsMapper.selectPage(new Page<>(page, size), wrapper));
    }

    @DeleteMapping("/{commentId}")
    public Result<Void> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        Comments comment = commentsMapper.selectById(commentId);
        if (comment == null) {
            return Result.notFound();
        }
        comment.setCommentStatus("DELETED");
        comment.setDeletedAt(new java.util.Date());
        commentsMapper.updateById(comment);
        adminLogService.logAction(getCurrentAdminId(), "DELETE_COMMENT", "COMMENT", commentId, "删除评论", getClientIp(request));
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
