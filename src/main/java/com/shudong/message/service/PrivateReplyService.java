package com.shudong.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shudong.message.entity.PrivateReplies;

public interface PrivateReplyService extends IService<PrivateReplies> {

    void sendPrivateReply(Long senderId, Long postId, String replyBody);

    void approveReply(Long replyId);

    void rejectReply(Long replyId);
}
