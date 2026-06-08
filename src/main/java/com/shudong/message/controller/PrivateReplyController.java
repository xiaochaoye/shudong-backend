package com.shudong.message.controller;

import com.shudong.common.response.Result;
import com.shudong.message.dto.PrivateReplyRequest;
import com.shudong.message.service.PrivateReplyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/posts/{postId}/private-reply")
@RequiredArgsConstructor
public class PrivateReplyController {

    private final PrivateReplyService privateReplyService;

    @PostMapping
    public Result<Void> sendPrivateReply(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long postId,
            @Valid @RequestBody PrivateReplyRequest request) {
        try {
            privateReplyService.sendPrivateReply(userId, postId, request.getReplyBody());
            return Result.success("私信回复已发送");
        } catch (Exception e) {
            log.error("发送私信回复失败: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }
}
