package com.shudong.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.post.entity.PostTags;
import com.shudong.post.mapper.PostTagsMapper;
import com.shudong.post.service.PostTagsService;

import org.springframework.stereotype.Service;

/**
* @author test
* @description 针对表【post_tags(帖子与标签的关联表，实现多对多关系，使用代理主键 id 支持 MyBatis-Plus)】的数据库操作Service实现
*/
@Service
public class PostTagsServiceImpl extends ServiceImpl<PostTagsMapper, PostTags>
    implements PostTagsService{

}




