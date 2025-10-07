package com.chao.shudongbackend.service;

import com.chao.shudongbackend.model.entity.PostTags;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author test
* @description 针对表【post_tags(帖子与标签的关联表，实现多对多关系，使用代理主键 id 支持 MyBatis-Plus)】的数据库操作Service
* @createDate 2025-10-07 21:44:42
*/
public interface PostTagsService extends IService<PostTags> {

}
