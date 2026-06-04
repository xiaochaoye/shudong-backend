package com.shudong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.model.entity.PostTags;
import com.shudong.service.PostTagsService;
import com.shudong.mapper.PostTagsMapper;
import org.springframework.stereotype.Service;

/**
* @author test
* @description 针对表【post_tags(帖子与标签的关联表，实现多对多关系，使用代理主键 id 支持 MyBatis-Plus)】的数据库操作Service实现
* @createDate 2025-10-07 21:44:42
*/
@Service
public class PostTagsServiceImpl extends ServiceImpl<PostTagsMapper, PostTags>
    implements PostTagsService{

}




