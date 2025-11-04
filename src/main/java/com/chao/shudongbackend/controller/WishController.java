package com.chao.shudongbackend.controller;

import com.chao.shudongbackend.model.dto.*;
import com.chao.shudongbackend.model.entity.Users;
import com.chao.shudongbackend.model.vo.View;
import com.chao.shudongbackend.model.vo.WishVO;
import com.chao.shudongbackend.service.UsersService;
import com.chao.shudongbackend.service.WishService;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 许愿池控制器
 * 
 */
@Slf4j
@RestController
@RequestMapping("/wishes")
@RequiredArgsConstructor
public class WishController {

    private final WishService wishService;
    private final UsersService usersService;
    
    /**
     * 查看我的愿望
     * 
     * @return 当前用户的所有愿望列表
     */
    @GetMapping("/my")
    @JsonView(View.My.class)
    public Result<List<WishVO>> getMyWishes() {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            List<WishVO> wishes = wishService.getMyWishes(userId);
            return Result.success(wishes);
        } catch (Exception e) {
            return Result.error("获取愿望列表失败");
        }
    }

    /**
     * 愿望编辑
     * 
     * @param wishId 愿望ID
     * @param updateDTO 愿望更新数据
     * @return 更新后的愿望信息
     */
    @PutMapping("/edit/{wishId}")
    public Result<WishVO> updateWish(@PathVariable Long wishId, @RequestBody WishUpdateDTO updateDTO) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            WishVO wishVO = wishService.updateWish(wishId, updateDTO, userId);
            return Result.success(wishVO);
        } catch (Exception e) {
            return Result.error("更新愿望失败");
        }
    }

    /**
     * 愿望删除
     * 
     * @param wishId 愿望ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{wishId}")
    public Result<Boolean> deleteWish(@PathVariable Long wishId) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            Boolean result = wishService.deleteWish(wishId, userId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("删除愿望失败");
        }
    }

    /**
     * 随机取一个愿望
     * 
     * @return 随机获取的未实现愿望
     */
    @GetMapping("/random")
    @JsonView(View.Basic.class)
    public Result<WishVO> getRandomWish() {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            WishVO wishVO = wishService.getRandomWish(userId);
            return Result.success(wishVO);
        } catch (Exception e) {
            return Result.error("随机获取愿望失败");
        }
    }

    /**
     * 发送心愿
     * 
     * @param createDTO 愿望创建数据
     * @return 创建后的愿望信息
     */
    @PostMapping
    public Result<Void> createWish(@RequestBody WishCreateDTO createDTO) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            WishVO wishVO = wishService.createWish(createDTO, userId);
            return Result.success("你的愿望标题：" + wishVO.getTitle());
        } catch (Exception e) {
            return Result.error("创建愿望失败");
        }
    }

    /**
     * 实现心愿后从许愿池捞出
     * 
     * @param completeDTO 愿望完成数据
     * @return 完成后的愿望信息
     */
    @PostMapping("/complete")
    public Result<Void> completeWish(@RequestBody WishCompleteDTO completeDTO) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return Result.error("用户未登录");
            }
            WishVO wishVO = wishService.completeWish(completeDTO, userId);
            return Result.success(wishVO.getTitle() + "：这个愿望已实现");
        } catch (Exception e) {
            return Result.error("完成愿望失败");
        }
    }

    /**
     * 愿望统计
     * 
     * @return 许愿池统计信息（总数、已实现数量、未实现数量）
     */
    @GetMapping("/stats")
    public Result<Object> getWishStats() {
        try {
            Object stats = wishService.getWishStats();
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取愿望统计失败");
        }
    }

    /**
     * 从Spring Security认证信息中获取当前用户ID
     * 
     * @return 当前用户ID，如果未登录返回null
     */
    private Long getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }
            
            String email = authentication.getName();
            Users user = usersService.lambdaQuery()
                    .eq(Users::getEmail, email)
                    .one();
            return user != null ? user.getId() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
