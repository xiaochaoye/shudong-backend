package com.shudong.pick.controller;

import com.shudong.common.response.Result;
import com.shudong.pick.dto.PickResponse;
import com.shudong.pick.service.PickService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/picks")
@RequiredArgsConstructor
public class PickController {

    private final PickService pickService;

    @GetMapping
    public Result<PickResponse> pickPost(@RequestAttribute("userId") Long userId) {
        PickResponse response = pickService.pickPost(userId, 1);
        return Result.success("拾取成功", response);
    }

    @PostMapping("/replied")
    public Result<Void> markAsReplied(@RequestAttribute("userId") Long userId,
                                      @RequestBody Map<String, Long> params) {
        Long postId = params.get("postId");
        if (postId == null) {
            return Result.error("postId 不能为空");
        }
        pickService.markAsReplied(userId, postId);
        return Result.success("标记已回应成功");
    }

    @GetMapping("/limit")
    public Result<Map<String, Object>> checkRateLimit(@RequestAttribute("userId") Long userId) {
        boolean limited = pickService.isRateLimited(userId);
        int count = pickService.getTodayPickCount(userId);
        Map<String, Object> result = Map.of(
                "limited", limited,
                "todayCount", count
        );
        return Result.success(result);
    }

    @GetMapping("/test-saying")
    public Result<String> testSaying() {
        String saying = pickService.testSaying();
        if (saying != null) {
            return Result.success("获取语录成功", saying);
        }
        return Result.error("获取语录失败");
    }
}
