package com.shudong.admin.controller;

import com.shudong.admin.service.AdminStatsService;
import com.shudong.common.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverviewStats() {
        return Result.success(adminStatsService.getSystemOverview());
    }

    @GetMapping("/users")
    public Result<Map<String, Object>> getUserStats() {
        return Result.success(adminStatsService.getUserStats());
    }

    @GetMapping("/posts")
    public Result<Map<String, Object>> getPostStats() {
        return Result.success(adminStatsService.getPostStats());
    }

    @GetMapping("/interactions")
    public Result<Map<String, Object>> getInteractionStats() {
        return Result.success(adminStatsService.getInteractionStats());
    }
}
