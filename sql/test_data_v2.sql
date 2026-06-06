-- =====================================================
-- 树洞后端重构 - 测试数据 (基于重构计划 v2)
-- Spring Boot 3.5.0 + Java 21 + MyBatis-Plus
-- 注意：users 表数据已存在，此处提供users表结构更新语句和测试数据
-- =====================================================

-- =====================================================
-- 用户模块测试数据
-- =====================================================

-- -----------------------------------------------
-- users 表测试数据（如果表为空则插入）
-- -----------------------------------------------
INSERT INTO users (email, username, password_hash, avatar, anonymous_name, anonymous_avatar, is_admin, record_status, created_at, last_login_at) VALUES
('test1@example.com', '小明', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'https://robohash.org/xiaoming?set=set4', '匿名小明', 'https://tdesign.gtimg.com/site/avatar.jpg', FALSE, 'ACTIVE', DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),
('test2@example.com', '小红', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'https://robohash.org/xiaohong?set=set4', '匿名小红', 'https://tdesign.gtimg.com/site/avatar.jpg', FALSE, 'ACTIVE', DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
('test3@example.com', '阿强', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'https://robohash.org/qiang?set=set4', '匿名阿强', 'https://tdesign.gtimg.com/site/avatar.jpg', FALSE, 'ACTIVE', DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
('test4@example.com', '小丽', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'https://robohash.org/xiaoli?set=set4', '匿名小丽', 'https://tdesign.gtimg.com/site/avatar.jpg', FALSE, 'ACTIVE', DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),
('test5@example.com', '大树', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'https://robohash.org/dashu?set=set4', '匿名大树', 'https://tdesign.gtimg.com/site/avatar.jpg', FALSE, 'ACTIVE', DATE_SUB(NOW(), INTERVAL 10 DAY), NOW()),
('admin@shudong.com', '管理员', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', 'https://robohash.org/admin?set=set4', NULL, NULL, TRUE, 'ACTIVE', DATE_SUB(NOW(), INTERVAL 60 DAY), NOW());

-- -----------------------------------------------
-- user_settings 表测试数据
-- -----------------------------------------------
INSERT INTO user_settings (user_id, email_notifications, push_notifications, ai_analysis_enabled, daily_pick_limit, night_pick_limit, updated_at) VALUES
(1, TRUE, TRUE, TRUE, 20, 40, NOW()),
(2, TRUE, FALSE, TRUE, 20, 40, NOW()),
(3, FALSE, TRUE, FALSE, 15, 30, NOW()),
(4, TRUE, TRUE, TRUE, 20, 40, NOW()),
(5, FALSE, FALSE, TRUE, 25, 50, NOW()),
(6, TRUE, TRUE, TRUE, 50, 100, NOW());

-- -----------------------------------------------
-- devices 表测试数据
-- -----------------------------------------------
INSERT INTO devices (user_id, device_id, device_name, user_agent, ip_address, last_login_at, device_status, expires_at, created_at) VALUES
(1, 'device_001_abc123', 'iPhone 15 Pro', 'Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X)', '192.168.1.100', NOW(), 'ACTIVE', DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
(1, 'device_002_def456', 'MacBook Pro', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)', '192.168.1.100', NOW(), 'ACTIVE', DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY)),
(2, 'device_003_ghi789', 'Samsung Galaxy S24', 'Mozilla/5.0 (Linux; Android 14; SM-S921B)', '192.168.1.101', NOW(), 'ACTIVE', DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),
(3, 'device_004_jkl012', 'iPad Air', 'Mozilla/5.0 (iPad; CPU OS 17_0 like Mac OS X)', '192.168.1.102', DATE_SUB(NOW(), INTERVAL 7 DAY), 'INACTIVE', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY)),
(4, 'device_005_mno345', 'Windows PC', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)', '192.168.1.103', NOW(), 'ACTIVE', DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(5, 'device_006_pqr678', 'Android Tablet', 'Mozilla/5.0 (Linux; Android 13; Tablet)', '192.168.1.104', NOW(), 'ACTIVE', DATE_ADD(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));

-- =====================================================
-- 帖子模块测试数据
-- =====================================================

-- -----------------------------------------------
-- posts 表测试数据
-- -----------------------------------------------
INSERT INTO posts (user_id, title, post_body, post_status, is_anonymous, is_private, view_count, resonance_count, comment_count, created_at, updated_at) VALUES
-- 用户1的帖子
(1, '今天终于拿到了心仪的offer！', '经过三个月的努力面试，终于拿到了心仪公司的offer！薪资比预期还高，团队氛围也超级好。感谢这段时间坚持下来的自己，也感谢树洞这个温暖的角落。希望还在求职的大家也能早日上岸！', 'PUBLISHED', FALSE, FALSE, 128, 5, 3, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, '感觉最近压力好大，快要喘不过气了', '工作、生活、感情，好像所有事情都堆在一起了。每天失眠到凌晨，早上又要强撑着去上班。不知道还能坚持多久...', 'PUBLISHED', TRUE, FALSE, 45, 2, 3, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, '希望明年能去冰岛看极光', '一直梦想着能亲眼看到极光，在冰岛的蓝湖温泉里仰望星空。希望明年能攒够钱，实现这个愿望。', 'PUBLISHED', FALSE, FALSE, 234, 8, 2, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY)),
-- 用户2的帖子
(2, '养了一年的猫终于让我抱了', '从领养回来就一直高冷，今天居然主动跳到我腿上睡觉！这种被信任的感觉太幸福了，虽然它可能只是把我当暖炉...', 'PUBLISHED', TRUE, FALSE, 256, 9, 2, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, '和最好的朋友闹掰了', '十年的友谊，因为一件小事彻底破裂。明明之前无话不谈，现在连对方的朋友圈都不敢看。成年人的友谊为什么这么脆弱？', 'PUBLISHED', FALSE, FALSE, 78, 3, 2, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, '许愿能找到一份满意的工作', '投了几十份简历，面试了十几家公司，还是没有合适的。希望能尽快找到一份薪资合适、氛围好的工作。', 'PUBLISHED', TRUE, FALSE, 156, 5, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
-- 用户3的帖子
(3, '考研上岸了！', '二战终于成功，分数比预期高20分。这一年真的太不容易了，每天图书馆打卡，咖啡当水喝。现在回头看，所有的付出都值得。', 'PUBLISHED', FALSE, FALSE, 512, 15, 3, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY)),
(3, '面试又被拒了，第7次了', '每次面试都精心准备，但结果总是不尽人意。开始怀疑自己的能力，是不是我真的不适合这个行业？', 'PUBLISHED', TRUE, FALSE, 34, 1, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, '希望家人身体健康', '爸妈年纪越来越大了，希望他们身体健健康康的，能多陪他们几年。', 'PUBLISHED', FALSE, FALSE, 89, 4, 1, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY)),
-- 用户4的帖子
(4, '周末去爬山看到了绝美日出', '凌晨四点起床，摸黑爬了两个小时的山，当看到太阳从地平线升起的那一刻，所有的疲惫都消失了。大自然的治愈力真的无与伦比。', 'PUBLISHED', TRUE, FALSE, 89, 4, 1, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
(4, '奶奶走了，我却没能见上最后一面', '因为疫情封控，连最后一面都没见到。现在每次想到这个，心就像被撕裂一样。树洞，我想她了。', 'PUBLISHED', FALSE, FALSE, 123, 6, 2, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY)),
(4, '许愿能遇到那个对的人', '单身三年了，不是不想谈，只是没遇到合适的。希望能遇到一个懂我、包容我的人。', 'PUBLISHED', TRUE, FALSE, 345, 12, 2, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
-- 用户5的帖子
(5, '学会了做提拉米苏', '跟着视频学了一周，失败了三次，终于做出完美的提拉米苏！分给室友吃，她们都说比甜品店的还好吃。', 'PUBLISHED', FALSE, FALSE, 167, 7, 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(5, '感觉自己像个废物', '同龄人都在进步，只有我还在原地踏步。没有目标，没有方向，每天浑浑噩噩。有时候真想放弃一切...', 'PUBLISHED', TRUE, FALSE, 67, 2, 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(5, '希望考研顺利上岸', '还有三个月就要考试了，压力好大。希望能考上心仪的学校，不辜负这一年的努力。', 'PUBLISHED', FALSE, FALSE, 178, 7, 1, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY));

-- -----------------------------------------------
-- tags 表测试数据
-- -----------------------------------------------
INSERT INTO tags (tag_name, usage_count, created_at) VALUES
('职场', 5, NOW()),
('情感', 4, NOW()),
('学习', 3, NOW()),
('生活', 6, NOW()),
('旅行', 2, NOW()),
('美食', 2, NOW()),
('宠物', 1, NOW()),
('健康', 1, NOW()),
('梦想', 4, NOW()),
('治愈', 3, NOW());

-- -----------------------------------------------
-- post_tags 表测试数据
-- -----------------------------------------------
INSERT INTO post_tags (post_id, tag_id) VALUES
(1, 1), (1, 4), (1, 9),    -- 拿到offer -> 职场、生活、梦想
(2, 1), (2, 4), (2, 10),   -- 压力大 -> 职场、生活、治愈
(3, 5), (3, 9),            -- 冰岛 -> 旅行、梦想
(4, 4), (4, 7), (4, 10),   -- 养猫 -> 生活、宠物、治愈
(5, 2), (5, 4),            -- 朋友 -> 情感、生活
(6, 1), (6, 9),            -- 工作 -> 职场、梦想
(7, 3), (7, 9),            -- 考研 -> 学习、梦想
(8, 1), (8, 4),            -- 面试 -> 职场、生活
(9, 4), (9, 8), (9, 9),    -- 健康 -> 生活、健康、梦想
(10, 4), (10, 5), (10, 10), -- 爬山 -> 生活、旅行、治愈
(11, 2), (11, 4), (11, 10), -- 奶奶 -> 情感、生活、治愈
(12, 2), (12, 9),           -- 对的人 -> 情感、梦想
(13, 4), (13, 6),           -- 提拉米苏 -> 生活、美食
(14, 1), (14, 4), (14, 10), -- 废物 -> 职场、生活、治愈
(15, 3), (15, 9);           -- 考研 -> 学习、梦想

-- -----------------------------------------------
-- comments 表测试数据
-- -----------------------------------------------
INSERT INTO comments (post_id, user_id, parent_id, comment_body, comment_status, created_at) VALUES
-- 帖子1（拿到offer）的评论
(1, 2, NULL, '恭喜恭喜！沾沾喜气！', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 3, NULL, '太厉害了，求分享面经！', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 4, NULL, '恭喜上岸！我也在等待offer，希望好运降临。', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
-- 帖子2（压力大）的评论
(2, 2, NULL, '抱抱你，我也经历过这样的时期。试着给自己放个假，哪怕只是请一天假在家睡觉也好。', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(2, 3, NULL, '压力太大的时候，试试深呼吸和冥想，真的有用。', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 8 HOUR)),
(2, 5, NULL, '你不是一个人在战斗，树洞里的大家都在。', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
-- 帖子3（冰岛）的评论
(3, 2, NULL, '我也想去！一起攒钱吧！', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 4, NULL, '冰岛真的绝美，我去过一次，终生难忘。', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 1 DAY)),
-- 帖子7（考研上岸）的评论
(7, 1, NULL, '恭喜恭喜！太厉害了！', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(7, 2, NULL, '沾沾喜气，希望我明年也能上岸！', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(7, 5, NULL, '太不容易了，为你开心！', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
-- 帖子11（奶奶）的评论
(11, 1, NULL, '抱抱你，奶奶一定希望你好好的。', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(11, 3, NULL, '节哀，她在天上一定也在守护着你。', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 2 DAY)),
-- 帖子12（对的人）的评论
(12, 1, NULL, '会遇到的，只是时间问题。先好好爱自己。', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(12, 4, NULL, '同许愿，希望今年能脱单。', 'ACTIVE', DATE_SUB(NOW(), INTERVAL 3 HOUR));

-- -----------------------------------------------
-- resonances 表测试数据
-- -----------------------------------------------
INSERT INTO resonances (post_id, user_id, resonance_type, created_at) VALUES
-- 帖子1的共鸣
(1, 2, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 3, 'SAME_FEELING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 4, 'EMPATHY', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(1, 5, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(1, 6, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
-- 帖子2的共鸣
(2, 2, 'SAME_FEELING', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(2, 3, 'EMPATHY', DATE_SUB(NOW(), INTERVAL 8 HOUR)),
-- 帖子3的共鸣
(3, 2, 'SAME_FEELING', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 4, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 5, 'SAME_FEELING', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
-- 帖子4的共鸣
(4, 1, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(4, 3, 'EMPATHY', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(4, 5, 'SAME_FEELING', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
-- 帖子7的共鸣
(7, 1, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(7, 2, 'SAME_FEELING', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(7, 4, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(7, 5, 'SAME_FEELING', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(7, 6, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
-- 帖子10的共鸣
(10, 2, 'SAME_FEELING', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(10, 3, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 2 DAY)),
-- 帖子11的共鸣
(11, 1, 'EMPATHY', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(11, 2, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(11, 4, 'EMPATHY', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(11, 5, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(11, 6, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
-- 帖子12的共鸣
(12, 1, 'SUPPORT', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(12, 2, 'SAME_FEELING', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(12, 3, 'SAME_FEELING', DATE_SUB(NOW(), INTERVAL 1 HOUR));

-- -----------------------------------------------
-- collections 表测试数据
-- -----------------------------------------------
INSERT INTO collections (user_id, post_id, category, created_at) VALUES
(1, 2, 'FAVORITE', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 7, 'FAVORITE', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 1, 'FAVORITE', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 3, 'FAVORITE', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(3, 4, 'FAVORITE', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 10, 'LATER', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(4, 3, 'FAVORITE', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(4, 11, 'LATER', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(5, 1, 'FAVORITE', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(5, 12, 'FAVORITE', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- =====================================================
-- 拾取模块测试数据
-- =====================================================

-- -----------------------------------------------
-- pick_records 表测试数据
-- -----------------------------------------------
INSERT INTO pick_records (user_id, post_id, pick_type, picked_at, resonanced_at) VALUES
(1, 2, 'CASUAL_VIEW', DATE_SUB(NOW(), INTERVAL 1 DAY), NULL),
(1, 7, 'EXPRESS_RESONANCE', DATE_SUB(NOW(), INTERVAL 12 HOUR), DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(1, 11, 'CASUAL_VIEW', DATE_SUB(NOW(), INTERVAL 3 HOUR), NULL),
(2, 1, 'EXPRESS_RESONANCE', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 8, 'CASUAL_VIEW', DATE_SUB(NOW(), INTERVAL 1 DAY), NULL),
(2, 12, 'CASUAL_VIEW', DATE_SUB(NOW(), INTERVAL 6 HOUR), NULL),
(3, 4, 'EXPRESS_RESONANCE', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(3, 9, 'CASUAL_VIEW', DATE_SUB(NOW(), INTERVAL 2 DAY), NULL),
(4, 3, 'EXPRESS_RESONANCE', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY)),
(4, 10, 'CASUAL_VIEW', DATE_SUB(NOW(), INTERVAL 1 DAY), NULL),
(5, 5, 'CASUAL_VIEW', DATE_SUB(NOW(), INTERVAL 1 DAY), NULL),
(5, 6, 'EXPRESS_RESONANCE', DATE_SUB(NOW(), INTERVAL 12 HOUR), DATE_SUB(NOW(), INTERVAL 6 HOUR));

-- -----------------------------------------------
-- pick_configs 表测试数据
-- -----------------------------------------------
INSERT INTO pick_configs (daily_limit, night_limit, cooldown_hours, archive_days, updated_at) VALUES
(20, 40, 1, 30, NOW());

-- =====================================================
-- 消息模块测试数据
-- =====================================================

-- -----------------------------------------------
-- private_replies 表测试数据
-- -----------------------------------------------
INSERT INTO private_replies (post_id, sender_id, receiver_id, reply_body, reply_status, created_at, reviewed_at, sent_at, email_content) VALUES
-- 待审核的私信
(2, 2, 1, '看到你的帖子，想给你一些建议。压力太大的时候可以试试运动，跑步或者瑜伽都很有效。另外，不要给自己太大压力，慢慢来。', 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, NULL, NULL),
(6, 3, 2, '我去年也经历过求职的低谷期，投了上百份简历才找到工作。不要放弃，你的机会一定会来的！', 'PENDING', DATE_SUB(NOW(), INTERVAL 2 DAY), NULL, NULL, NULL),
(8, 4, 3, '面试被拒不代表你不够好，只是不合适。每次面试都是一次学习的机会，总结经验，下次一定会更好！', 'PENDING', DATE_SUB(NOW(), INTERVAL 1 DAY), NULL, NULL, NULL),
-- 已通过的私信
(11, 1, 4, '节哀顺变。失去亲人的痛需要时间慢慢愈合，但请记住，奶奶一定希望你好好的。如果你需要倾诉，我随时在。', 'APPROVED', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), '<html><body><p>节哀顺变。失去亲人的痛需要时间慢慢愈合...</p></body></html>'),
(14, 2, 5, '考研确实不容易，但你的努力一定会有回报的。建议制定一个详细的复习计划，每天按部就班地执行。加油！', 'APPROVED', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 12 HOUR), '<html><body><p>考研确实不容易，但你的努力一定会有回报的...</p></body></html>'),
-- 已拒绝的私信
(2, 5, 1, '你这个帖子写得什么乱七八糟的，别在这里传播负能量了。', 'REJECTED', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 12 HOUR), NULL, NULL);

-- -----------------------------------------------
-- notifications 表测试数据
-- -----------------------------------------------
INSERT INTO notifications (user_id, notice_type, title, notice_body, is_read, created_at) VALUES
-- 用户1的通知
(1, 'SYSTEM', '欢迎来到树洞', '感谢你加入树洞社区，这里是一个温暖、安全的倾诉空间。', TRUE, DATE_SUB(NOW(), INTERVAL 30 DAY)),
(1, 'PRIVATE_REPLY', '有人给你发送了私信回复', '用户"小红"给你的帖子《感觉最近压力好大，快要喘不过气了》发送了私信回复，请查收。', FALSE, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 'SYSTEM', '今日拾取提醒', '你今天还没有拾取帖子哦，去看看有什么温暖的分享吧！', FALSE, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
-- 用户2的通知
(2, 'SYSTEM', '欢迎来到树洞', '感谢你加入树洞社区，这里是一个温暖、安全的倾诉空间。', TRUE, DATE_SUB(NOW(), INTERVAL 25 DAY)),
(2, 'PRIVATE_REPLY', '有人给你发送了私信回复', '用户"阿强"给你的帖子《许愿能找到一份满意的工作》发送了私信回复，请查收。', TRUE, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 'SYSTEM', '你的帖子收到了新的共鸣', '你的帖子《养了一年的猫终于让我抱了》收到了3个新的共鸣。', FALSE, DATE_SUB(NOW(), INTERVAL 6 HOUR)),
-- 用户3的通知
(3, 'SYSTEM', '欢迎来到树洞', '感谢你加入树洞社区，这里是一个温暖、安全的倾诉空间。', FALSE, DATE_SUB(NOW(), INTERVAL 20 DAY)),
(3, 'PRIVATE_REPLY', '有人给你发送了私信回复', '用户"小丽"给你的帖子《面试又被拒了，第7次了》发送了私信回复，请查收。', FALSE, DATE_SUB(NOW(), INTERVAL 1 DAY)),
-- 用户4的通知
(4, 'SYSTEM', '欢迎来到树洞', '感谢你加入树洞社区，这里是一个温暖、安全的倾诉空间。', TRUE, DATE_SUB(NOW(), INTERVAL 15 DAY)),
(4, 'PRIVATE_REPLY', '有人给你发送了私信回复', '用户"小明"给你的帖子《奶奶走了，我却没能见上最后一面》发送了私信回复，请查收。', TRUE, DATE_SUB(NOW(), INTERVAL 2 DAY)),
-- 用户5的通知
(5, 'SYSTEM', '欢迎来到树洞', '感谢你加入树洞社区，这里是一个温暖、安全的倾诉空间。', TRUE, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(5, 'PRIVATE_REPLY', '有人给你发送了私信回复', '用户"小红"给你的帖子《希望考研顺利上岸》发送了私信回复，请查收。', FALSE, DATE_SUB(NOW(), INTERVAL 12 HOUR));

-- -----------------------------------------------
-- email_templates 表测试数据
-- -----------------------------------------------
INSERT INTO email_templates (template_name, email_subject, html_content, created_at) VALUES
('welcome_email', '欢迎来到树洞社区', '<html><body><h1>欢迎加入树洞！</h1><p>感谢你注册树洞社区，这里是一个温暖、安全的倾诉空间。</p><p>你可以在这里：</p><ul><li>分享你的快乐和烦恼</li><li>拾取他人的故事</li><li>找到共鸣和安慰</li></ul><p>祝你在树洞度过美好的时光！</p></body></html>', NOW()),
('private_reply_notification', '有人给你回复了私信', '<html><body><h1>新的私信回复</h1><p>你好，{{username}}！</p><p>有人给你的帖子《{{post_title}}》发送了私信回复：</p><blockquote>{{reply_content}}</blockquote><p>点击<a href="{{link}}">这里</a>查看完整内容。</p></body></html>', NOW()),
('password_reset', '密码重置请求', '<html><body><h1>密码重置</h1><p>你好，{{username}}！</p><p>我们收到了你的密码重置请求。点击以下链接重置密码：</p><p><a href="{{reset_link}}">重置密码</a></p><p>如果这不是你发起的请求，请忽略此邮件。</p></body></html>', NOW()),
('daily_pick_reminder', '今日拾取提醒', '<html><body><h1>今日拾取提醒</h1><p>你好，{{username}}！</p><p>你今天还没有拾取帖子哦，去看看有什么温暖的分享吧！</p><p><a href="{{link}}">去拾取</a></p></body></html>', NOW());

-- =====================================================
-- 管理员模块测试数据
-- =====================================================

-- -----------------------------------------------
-- admin_logs 表测试数据
-- -----------------------------------------------
INSERT INTO admin_logs (admin_id, action_type, target_type, target_id, extra_data, ip_address, created_at) VALUES
(6, 'REVIEW', 'POST', 1, '审核通过帖子《今天终于拿到了心仪的offer！》，内容符合社区规范。', '192.168.1.200', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(6, 'REVIEW', 'POST', 2, '审核通过帖子《感觉最近压力好大，快要喘不过气了》，内容符合社区规范。', '192.168.1.200', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(6, 'REVIEW', 'COMMENT', 1, '审核通过评论：恭喜恭喜！沾沾喜气！', '192.168.1.200', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(6, 'DELETE', 'POST', 16, '删除违规帖子，原因：包含广告信息。', '192.168.1.200', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(6, 'UPDATE_STATUS', 'USER', 3, '修改用户状态为活跃，原因：用户申诉通过。', '192.168.1.200', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(6, 'REVIEW', 'PRIVATE_REPLY', 4, '审核通过私信回复，内容符合社区规范。', '192.168.1.200', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(6, 'REJECT', 'PRIVATE_REPLY', 6, '拒绝私信回复，原因：包含不当言论。', '192.168.1.200', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(6, 'REVIEW', 'POST', 11, '审核通过帖子《奶奶走了，我却没能见上最后一面》，内容符合社区规范。', '192.168.1.200', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- -----------------------------------------------
-- system_configs 表测试数据
-- -----------------------------------------------
INSERT INTO system_configs (config_key, config_value, brief_info, updated_at) VALUES
('site.name', '树洞', '网站显示名称', NOW()),
('site.description', '一个温暖、安全的倾诉空间', '网站描述', NOW()),
('user.register.enabled', 'true', '是否开放用户注册', NOW()),
('post.review.required', 'true', '新发帖是否需要审核', NOW()),
('comment.review.required', 'false', '评论是否需要审核', NOW()),
('post.daily.limit', '10', '用户每日发帖最大数量', NOW()),
('comment.daily.limit', '50', '用户每日评论最大数量', NOW()),
('ai.analysis.enabled', 'true', '是否启用AI情绪分析', NOW()),
('ai.review.enabled', 'true', '是否启用AI内容审核', NOW()),
('email.notification.enabled', 'true', '是否启用邮件通知', NOW()),
('system.maintenance', 'false', '系统是否处于维护模式', NOW()),
('system.maintenance.message', '系统维护中，请稍后再试', '维护模式提示信息', NOW()),
('jwt.expiration.hours', '2', 'JWT Token过期时间（小时）', NOW()),
('jwt.refresh.expiration.days', '7', 'JWT刷新令牌过期时间（天）', NOW()),
('pick.daily.limit', '20', '每日拾取上限', NOW()),
('pick.night.limit', '40', '夜间拾取上限', NOW()),
('pick.cooldown.minutes', '5', '拾取冷却时间（分钟）', NOW());
