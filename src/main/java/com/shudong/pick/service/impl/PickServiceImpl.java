package com.shudong.pick.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shudong.common.utils.RedisUtil;
import com.shudong.pick.dto.PickResponse;
import com.shudong.pick.entity.PickConfigs;
import com.shudong.pick.entity.PickRecords;
import com.shudong.pick.mapper.PickConfigsMapper;
import com.shudong.pick.mapper.PickRecordsMapper;
import com.shudong.pick.service.PickService;
import com.shudong.post.dto.PostResponse;
import com.shudong.post.entity.Posts;
import com.shudong.post.mapper.PostsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PickServiceImpl implements PickService {

    private final PickRecordsMapper pickRecordMapper;
    private final PickConfigsMapper pickConfigsMapper;
    private final PostsMapper postsMapper;
    private final RedisUtil redisUtil;
    private final RestTemplate restTemplate;

    private static final String SAYING_API = "https://uapis.cn/api/v1/saying";
    private static final int NIGHT_START_HOUR = 23;
    private static final int NIGHT_END_HOUR = 6;
    private static final int MAX_PICK_LIMIT = 10;

    @Override
    public PickResponse pickPost(Long userId, int limit) {
        limit = Math.max(1, Math.min(limit, MAX_PICK_LIMIT));

        if (isRateLimited(userId)) {
            return createEmptyResponseWithSaying("今日拾取次数已达上限");
        }
        List<Posts> posts = pickPosts(userId, limit);
        if (posts.isEmpty()) {
            return createEmptyResponseWithSaying(null);
        }
        List<PickRecords> records = recordPick(userId, posts);
        return convertToResponse(records, posts);
    }

    @Override
    public void markAsReplied(Long userId, Long postId) {
        QueryWrapper<PickRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("post_id", postId);
        PickRecords record = pickRecordMapper.selectOne(queryWrapper);
        if (record != null && record.getResonancedAt() == null) {
            record.setResonancedAt(new Date());
            pickRecordMapper.updateById(record);

            Posts post = postsMapper.selectById(postId);
            if (post != null) {
                post.setResonanceCount(post.getResonanceCount() == null ? 1 : post.getResonanceCount() + 1);
                postsMapper.updateById(post);
            }
        }
    }

    @Override
    public boolean isRateLimited(Long userId) {
        int limit = getPickLimit();
        int count = getTodayPickCount(userId);
        return count >= limit;
    }

    @Override
    public int getTodayPickCount(Long userId) {
        String key = "pick:" + userId + ":" + LocalDate.now();
        Object count = redisUtil.get(key);
        if (count == null) {
            Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            QueryWrapper<PickRecords> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId)
                    .ge("picked_at", today);
            Long dbCount = pickRecordMapper.selectCount(queryWrapper);
            int result = dbCount != null ? dbCount.intValue() : 0;
            redisUtil.set(key, result, getSecondsUntilEndOfDay());
            return result;
        }
        return Integer.parseInt(count.toString());
    }

    @Override
    public String testSaying() {
        return fetchSaying();
    }

    private int getPickLimit() {
        PickConfigs config = pickConfigsMapper.selectById(1);
        boolean isNight = isNightTime();
        if (isNight) {
            return config != null && config.getNightLimit() != null ? config.getNightLimit() : 40;
        }
        return config != null && config.getDailyLimit() != null ? config.getDailyLimit() : 20;
    }

    private boolean isNightTime() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        return hour >= NIGHT_START_HOUR || hour < NIGHT_END_HOUR;
    }

    private List<Posts> pickPosts(Long userId, int limit) {
        Set<Long> pickedPostIds = getPickedPostIds(userId);

        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("deleted_at")
                .eq("post_status", "PUBLISHED")
                .ne("user_id", userId)
                .notIn("id", pickedPostIds.isEmpty() ? Collections.singletonList(-1L) : pickedPostIds)
                .orderByDesc("created_at");
        List<Posts> allPosts = postsMapper.selectList(queryWrapper);

        if (allPosts.isEmpty()) {
            return Collections.emptyList();
        }

        List<PostScore> scoredPosts = allPosts.stream()
                .map(post -> new PostScore(post, calculateScore(post, userId)))
                .sorted(Comparator.comparingDouble(PostScore::getScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());

        return scoredPosts.stream()
                .map(PostScore::getPost)
                .collect(Collectors.toList());
    }

    private double calculateScore(Posts post, Long userId) {
        double score = 0.0;

        // 未被回应优先
        QueryWrapper<PickRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("post_id", post.getId())
                .isNull("resonanced_at");
        Long unrepliedCount = pickRecordMapper.selectCount(queryWrapper);
        if (unrepliedCount != null && unrepliedCount > 0) {
            score += 100.0;
        }

        // 时间衰减
        Date createdAt = post.getCreatedAt();
        if (createdAt != null) {
            long daysSinceCreated = (System.currentTimeMillis() - createdAt.getTime()) / (1000 * 60 * 60 * 24);
            double timeDecay = Math.exp(-daysSinceCreated / 7.0);
            score += timeDecay * 50.0;
        }

        // 浏览数权重
        if (post.getViewCount() != null) {
            score += Math.min(post.getViewCount() / 10.0, 20.0);
        }

        // 共鸣数权重：共鸣越多说明越打动人，适当提升优先级
        if (post.getResonanceCount() != null) {
            score += Math.min(post.getResonanceCount() * 5.0, 30.0);
        }

        return score;
    }

    private Set<Long> getPickedPostIds(Long userId) {
        QueryWrapper<PickRecords> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .isNull("resonanced_at");
        List<PickRecords> records = pickRecordMapper.selectList(queryWrapper);
        return records.stream()
                .map(PickRecords::getPostId)
                .collect(Collectors.toSet());
    }

    private List<PickRecords> recordPick(Long userId, List<Posts> posts) {
        Date now = new Date();
        String pickType = isNightTime() ? "night" : "daily";
        List<PickRecords> records = new ArrayList<>();

        for (Posts post : posts) {
            PickRecords record = new PickRecords();
            record.setUserId(userId);
            record.setPostId(post.getId());
            record.setPickType(pickType);
            record.setPickedAt(now);
            pickRecordMapper.insert(record);
            records.add(record);
        }

        String key = "pick:" + userId + ":" + LocalDate.now();
        Object count = redisUtil.get(key);
        int newCount = (count == null) ? posts.size() : Integer.parseInt(count.toString()) + posts.size();
        redisUtil.set(key, newCount, getSecondsUntilEndOfDay());

        return records;
    }

    private long getSecondsUntilEndOfDay() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return (calendar.getTimeInMillis() - now.getTime()) / 1000;
    }

    private PickResponse createEmptyResponseWithSaying(String message) {
        PickResponse response = new PickResponse();
        response.setEmpty(true);
        response.setMessage(message);
        String saying = fetchSaying();
        if (saying != null) {
            response.setSaying(saying);
        }
        return response;
    }

    private String fetchSaying() {
        try {
            String json = restTemplate.getForObject(SAYING_API, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);
            if (node.has("text")) {
                return node.get("text").asText();
            }
        } catch (Exception e) {
            log.warn("获取语录失败: {}", e.getMessage());
        }
        return null;
    }

    private PickResponse convertToResponse(List<PickRecords> records, List<Posts> posts) {
        PickResponse response = new PickResponse();
        response.setEmpty(false);

        List<PostResponse> postResponses = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            Posts post = posts.get(i);
            PostResponse pr = new PostResponse();
            pr.setId(post.getId());
            pr.setTitle(post.getTitle());
            pr.setPostBody(post.getPostBody());
            pr.setIsAnonymous(post.getIsAnonymous());
            pr.setIsPrivate(post.getIsPrivate());
            pr.setViewCount(post.getViewCount());
            pr.setResonanceCount(post.getResonanceCount());
            pr.setCommentCount(post.getCommentCount());
            pr.setCreatedAt(post.getCreatedAt());
            postResponses.add(pr);
        }
        response.setPosts(postResponses);

        // 单条时填充 pick 级别字段
        if (records.size() == 1) {
            PickRecords record = records.get(0);
            response.setPickId(record.getId());
            response.setPostId(record.getPostId());
            response.setPickType(record.getPickType());
            response.setPickedAt(record.getPickedAt());
            response.setResonancedAt(record.getResonancedAt());
        }

        return response;
    }

    private static class PostScore {
        private final Posts post;
        private final double score;

        PostScore(Posts post, double score) {
            this.post = post;
            this.score = score;
        }

        Posts getPost() {
            return post;
        }

        double getScore() {
            return score;
        }
    }
}
