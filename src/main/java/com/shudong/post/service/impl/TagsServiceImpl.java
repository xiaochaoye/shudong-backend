package com.shudong.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shudong.post.entity.Tags;
import com.shudong.post.mapper.TagsMapper;
import com.shudong.post.service.TagsService;

import org.springframework.stereotype.Service;

/**
* @author test
* @description 针对表【tags(标签表，用于给帖子打标签)】的数据库操作Service实现
*/
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags>
    implements TagsService{

}




