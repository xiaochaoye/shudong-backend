package com.shudong.mapper;

import com.shudong.model.entity.Replies;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author test
* @description 针对表【replies(难过区邮件回复记录表，支持管理员或系统发送)】的数据库操作Mapper
* @createDate 2025-10-05 23:04:29
* @Entity com.chao.shudongbackend.model.entity.Replies
*/
@Mapper
public interface RepliesMapper extends BaseMapper<Replies> {

}




