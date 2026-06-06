package com.shudong.pick.controller;

import com.shudong.common.response.Result;
import com.shudong.pick.service.PickService;
import com.shudong.post.entity.Posts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 拾取管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/picks")
@RequiredArgsConstructor
public class PickController {

    private final PickService pickService;

    /**
     * 每日精选拾取
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param params 请求参数，包含 limit
     * @return 帖子列表
     */
    @GetMapping("/daily")
    public Result<List<Posts>> dailyPick(@RequestAttribute("userId") Long userId,
                                         @RequestParam Map<String, String> params) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            int limit = params.containsKey("limit") ? Integer.parseInt(params.get("limit")) : 5;
            List<Posts> posts = pickService.dailyPick(userId, limit);
            return Result.success("获取每日精选成功", posts);
        } catch (Exception e) {
            log.error("获取每日精选失败: {}", e.getMessage());
            return Result.error("获取每日精选失败");
        }
    }

    /**
     * 夜间精选拾取
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param params 请求参数，包含 limit
     * @return 帖子列表
     */
    @GetMapping("/night")
    public Result<List<Posts>> nightPick(@RequestAttribute("userId") Long userId,
                                         @RequestParam Map<String, String> params) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            int limit = params.containsKey("limit") ? Integer.parseInt(params.get("limit")) : 5;
            List<Posts> posts = pickService.nightPick(userId, limit);
            return Result.success("获取夜间精选成功", posts);
        } catch (Exception e) {
            log.error("获取夜间精选失败: {}", e.getMessage());
            return Result.error("获取夜间精选失败");
        }
    }

    /**
     * 随机拾取
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param params 请求参数，包含 limit
     * @return 帖子列表
     */
    @GetMapping("/random")
    public Result<List<Posts>> randomPick(@RequestAttribute("userId") Long userId,
                                          @RequestParam Map<String, String> params) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            int limit = params.containsKey("limit") ? Integer.parseInt(params.get("limit")) : 1;
            List<Posts> posts = pickService.randomPick(userId, limit);
            return Result.success("随机拾取成功", posts);
        } catch (Exception e) {
            log.error("随机拾取失败: {}", e.getMessage());
            return Result.error("随机拾取失败");
        }
    }

    /**
     * 标记拾取记录为已回应
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param params 请求参数，包含 postId
     * @return 操作结果
     */
    @PostMapping("/replied")
    public Result<Void> markAsReplied(@RequestAttribute("userId") Long userId,
                                      @RequestBody Map<String, String> params) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            Long postId = Long.valueOf(params.get("postId"));
            pickService.markAsReplied(userId, postId);
            return Result.success("标记已回应成功");
        } catch (Exception e) {
            log.error("标记已回应失败: {}", e.getMessage());
            return Result.error("标记已回应失败");
        }
    }

    /**
     * 检查拾取频率限制
     *
     * @param userId 当前用户ID（从JWT token解析）
     * @param pickType 拾取类型
     * @return 是否受限及今日拾取次数
     */
    @GetMapping("/limit")
    public Result<Map<String, Object>> checkRateLimit(@RequestAttribute("userId") Long userId,
                                                       @RequestParam String pickType) {
        try {
            if (userId == null) {
                return Result.error("用户未登录");
            }
            boolean limited = pickService.isRateLimited(userId, pickType);
            int count = pickService.getTodayPickCount(userId, pickType);
            Map<String, Object> result = Map.of(
                    "limited", limited,
                    "todayCount", count,
                    "pickType", pickType
            );
            return Result.success(result);
        } catch (Exception e) {
            log.error("检查频率限制失败: {}", e.getMessage());
            return Result.error("检查频率限制失败");
        }
    }
}
