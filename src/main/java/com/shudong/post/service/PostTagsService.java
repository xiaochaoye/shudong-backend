package com.shudong.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shudong.post.entity.PostTags;

/**
* @author test
* @description 针对表【post_tags(帖子与标签的关联表，实现多对多关系，使用代理主键 id 支持 MyBatis-Plus)】的数据库操作Service
*/
public interface PostTagsService extends IService<PostTags> {

}
