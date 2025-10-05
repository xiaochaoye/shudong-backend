package com.chao.shudongbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chao.shudongbackend.model.entity.Tags;
import com.chao.shudongbackend.service.TagsService;
import com.chao.shudongbackend.mapper.TagsMapper;
import org.springframework.stereotype.Service;

/**
* @author test
* @description 针对表【tags(标签表，用于给帖子打标签)】的数据库操作Service实现
* @createDate 2025-10-05 23:04:30
*/
@Service
public class TagsServiceImpl extends ServiceImpl<TagsMapper, Tags>
    implements TagsService{

}




