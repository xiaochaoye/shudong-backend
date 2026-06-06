-- =====================================================
-- 树洞后端重构 - 数据库表结构 (基于重构计划 v2)
-- Spring Boot 3.5.0 + Java 21 + MyBatis-Plus
-- 注意：users 表数据已存在，但表结构需要更新以匹配新实体
-- =====================================================

-- =====================================================
-- 用户模块
-- =====================================================

-- -----------------------------------------------
-- 表: users - 用户表（更新结构以匹配新实体）
-- -----------------------------------------------
-- 注意：如果users表已存在，请根据需要调整字段
-- 新实体字段：id, email, username, password_hash, avatar, created_at, updated_at, is_active, is_admin
-- 计划中的字段：id, username, password, email, avatar, anonymous_name, anonymous_avatar, status, created_at, last_login_at, deleted_at
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT COMMENT '用户ID，主键，自增',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT '邮箱，唯一，用于登录',
    username VARCHAR(50) NOT NULL COMMENT '用户名，可显示在帖子上',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希值（使用 bcrypt 加密）',
    avatar VARCHAR(512) DEFAULT NULL COMMENT '用户头像图片的URL链接',
    anonymous_name VARCHAR(50) DEFAULT NULL COMMENT '匿名昵称',
    anonymous_avatar VARCHAR(512) DEFAULT NULL COMMENT '匿名头像URL',
    is_admin BOOLEAN DEFAULT FALSE COMMENT '是否管理员',
    record_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE=活跃，INACTIVE=非活跃，DELETED=已删除',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    last_login_at TIMESTAMP NULL DEFAULT NULL COMMENT '最后登录时间',
    deleted_at TIMESTAMP NULL DEFAULT NULL COMMENT '软删除时间',
    PRIMARY KEY (id),
    INDEX idx_email (email),
    INDEX idx_username (username),
    INDEX idx_record_status (record_status)
) COMMENT='用户表，存储注册用户信息';

-- -----------------------------------------------
-- 表: user_settings - 用户设置表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS user_settings (
    id BIGINT AUTO_INCREMENT COMMENT '设置ID，主键，自增',
    user_id BIGINT NOT NULL COMMENT '用户ID，关联users表',
    email_notifications BOOLEAN DEFAULT TRUE COMMENT '是否开启邮件通知',
    push_notifications BOOLEAN DEFAULT TRUE COMMENT '是否开启推送通知',
    ai_analysis_enabled BOOLEAN DEFAULT TRUE COMMENT '是否开启AI分析',
    daily_pick_limit INT DEFAULT 20 COMMENT '每日拾取上限',
    night_pick_limit INT DEFAULT 40 COMMENT '夜间拾取上限',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_user_id (user_id)
) COMMENT='用户设置表，存储用户个性化设置';

-- -----------------------------------------------
-- 表: devices - 设备表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS devices (
    id BIGINT AUTO_INCREMENT COMMENT '设备记录ID，主键，自增',
    user_id BIGINT NOT NULL COMMENT '用户ID，关联users表',
    device_id VARCHAR(255) NOT NULL COMMENT '设备唯一标识',
    device_name VARCHAR(100) DEFAULT NULL COMMENT '设备名称',
    user_agent TEXT DEFAULT NULL COMMENT '用户代理字符串',
    ip_address VARCHAR(45) DEFAULT NULL COMMENT 'IP地址',
    last_login_at TIMESTAMP NULL DEFAULT NULL COMMENT '最后登录时间',
    device_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '设备状态：ACTIVE=活跃，INACTIVE=非活跃',
    expires_at TIMESTAMP NULL DEFAULT NULL COMMENT '过期时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id),
    INDEX idx_device_id (device_id),
    INDEX idx_device_status (device_status)
) COMMENT='设备表，存储用户登录设备信息';

-- =====================================================
-- 帖子模块
-- =====================================================

-- -----------------------------------------------
-- 表: posts - 帖子表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS posts (
    id BIGINT AUTO_INCREMENT COMMENT '帖子ID，主键，自增',
    user_id BIGINT NOT NULL COMMENT '发布者用户ID',
    title VARCHAR(200) DEFAULT NULL COMMENT '帖子标题',
    post_body TEXT NOT NULL COMMENT '帖子正文内容',
    post_status VARCHAR(20) DEFAULT 'PUBLISHED' COMMENT '状态：PUBLISHED=已发布，DELETED=已删除',
    is_anonymous BOOLEAN DEFAULT FALSE COMMENT '是否匿名发布',
    is_private BOOLEAN DEFAULT FALSE COMMENT '是否私密',
    archived_at TIMESTAMP NULL DEFAULT NULL COMMENT '归档时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    deleted_at TIMESTAMP NULL DEFAULT NULL COMMENT '软删除时间',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    resonance_count INT DEFAULT 0 COMMENT '共鸣次数',
    comment_count INT DEFAULT 0 COMMENT '评论次数',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id),
    INDEX idx_post_status (post_status),
    INDEX idx_created_at (created_at),
    INDEX idx_deleted_at (deleted_at)
) COMMENT='帖子表';

-- -----------------------------------------------
-- 表: tags - 标签表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS tags (
    id BIGINT AUTO_INCREMENT COMMENT '标签ID，主键，自增',
    tag_name VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称',
    usage_count INT DEFAULT 0 COMMENT '使用次数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_tag_name (tag_name)
) COMMENT='标签表';

-- -----------------------------------------------
-- 表: post_tags - 帖子与标签的多对多关联表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS post_tags (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID，自增',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_post_tag (post_id, tag_id),
    INDEX idx_post_id (post_id),
    INDEX idx_tag_id (tag_id)
) COMMENT='帖子与标签的关联表';

-- -----------------------------------------------
-- 表: comments - 评论表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT COMMENT '评论ID，主键，自增',
    post_id BIGINT NOT NULL COMMENT '被评论的帖子ID',
    user_id BIGINT NOT NULL COMMENT '评论者用户ID',
    parent_id BIGINT NULL DEFAULT NULL COMMENT '父评论ID，用于回复评论',
    comment_body TEXT NOT NULL COMMENT '评论内容',
    comment_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE=活跃，DELETED=已删除',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    deleted_at TIMESTAMP NULL DEFAULT NULL COMMENT '软删除时间',
    PRIMARY KEY (id),
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_deleted_at (deleted_at)
) COMMENT='评论表';

-- -----------------------------------------------
-- 表: resonances - 共鸣表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS resonances (
    id BIGINT AUTO_INCREMENT COMMENT '共鸣ID，主键，自增',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    resonance_type VARCHAR(50) DEFAULT 'EMPATHY' COMMENT '共鸣类型',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_post_user (post_id, user_id),
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id)
) COMMENT='共鸣表';

-- -----------------------------------------------
-- 表: collections - 收藏表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS collections (
    id BIGINT AUTO_INCREMENT COMMENT '收藏ID，主键，自增',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    category VARCHAR(50) DEFAULT 'DEFAULT' COMMENT '收藏分类',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_post (user_id, post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_post_id (post_id)
) COMMENT='收藏表';

-- =====================================================
-- 拾取模块
-- =====================================================

-- -----------------------------------------------
-- 表: pick_records - 拾取记录表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS pick_records (
    id BIGINT AUTO_INCREMENT COMMENT '拾取记录ID，主键，自增',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    pick_type VARCHAR(50) DEFAULT 'CASUAL_VIEW' COMMENT '类型：CASUAL_VIEW= casual浏览，EXPRESS_RESONANCE=表达共鸣',
    picked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '拾取时间',
    resonanced_at TIMESTAMP NULL DEFAULT NULL COMMENT '共鸣时间',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id),
    INDEX idx_post_id (post_id),
    INDEX idx_picked_at (picked_at)
) COMMENT='拾取记录表';

-- -----------------------------------------------
-- 表: pick_configs - 拾取配置表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS pick_configs (
    id BIGINT AUTO_INCREMENT COMMENT '配置ID，主键，自增',
    daily_limit INT DEFAULT 20 COMMENT '每日上限',
    night_limit INT DEFAULT 40 COMMENT '夜间上限',
    cooldown_hours INT DEFAULT 1 COMMENT '冷却时间（小时）',
    archive_days INT DEFAULT 30 COMMENT '归档天数',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (id)
) COMMENT='拾取配置表';

-- =====================================================
-- 消息模块
-- =====================================================

-- -----------------------------------------------
-- 表: private_replies - 私信回复表（邮件回复）
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS private_replies (
    id BIGINT AUTO_INCREMENT COMMENT '私信回复ID，主键，自增',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    sender_id BIGINT NOT NULL COMMENT '发送者用户ID',
    receiver_id BIGINT NOT NULL COMMENT '接收者用户ID',
    reply_body TEXT NOT NULL COMMENT '内容',
    reply_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING=待审核，APPROVED=已通过，REJECTED=已拒绝',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    reviewed_at TIMESTAMP NULL DEFAULT NULL COMMENT '审核时间',
    sent_at TIMESTAMP NULL DEFAULT NULL COMMENT '发送时间',
    email_content TEXT DEFAULT NULL COMMENT '邮件内容',
    PRIMARY KEY (id),
    INDEX idx_post_id (post_id),
    INDEX idx_sender_id (sender_id),
    INDEX idx_receiver_id (receiver_id),
    INDEX idx_reply_status (reply_status)
) COMMENT='私信回复表';

-- -----------------------------------------------
-- 表: notifications - 站内通知表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT COMMENT '通知ID，主键，自增',
    user_id BIGINT NOT NULL COMMENT '接收者用户ID',
    notice_type VARCHAR(50) NOT NULL COMMENT '类型：PRIVATE_REPLY=私信回复，SYSTEM=系统通知',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    notice_body TEXT NOT NULL COMMENT '内容',
    is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id),
    INDEX idx_notice_type (notice_type),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
) COMMENT='站内通知表';

-- -----------------------------------------------
-- 表: email_templates - 邮件模板表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS email_templates (
    id BIGINT AUTO_INCREMENT COMMENT '模板ID，主键，自增',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    email_subject VARCHAR(200) NOT NULL COMMENT '邮件主题',
    html_content TEXT NOT NULL COMMENT 'HTML内容',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_template_name (template_name)
) COMMENT='邮件模板表';

-- =====================================================
-- 管理员模块
-- =====================================================

-- -----------------------------------------------
-- 表: admin_logs - 管理员日志表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS admin_logs (
    id BIGINT AUTO_INCREMENT COMMENT '日志ID，主键，自增',
    admin_id BIGINT NOT NULL COMMENT '管理员用户ID',
    action_type VARCHAR(50) NOT NULL COMMENT '操作',
    target_type VARCHAR(50) NOT NULL COMMENT '目标类型',
    target_id BIGINT NOT NULL COMMENT '目标ID',
    extra_data TEXT DEFAULT NULL COMMENT '详情',
    ip_address VARCHAR(45) DEFAULT NULL COMMENT 'IP地址',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_admin_id (admin_id),
    INDEX idx_target (target_type, target_id),
    INDEX idx_created_at (created_at)
) COMMENT='管理员日志表';

-- -----------------------------------------------
-- 表: system_configs - 系统配置表
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS system_configs (
    id BIGINT AUTO_INCREMENT COMMENT '配置ID，主键，自增',
    config_key VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    config_value VARCHAR(500) NOT NULL COMMENT '配置值',
    brief_info TEXT DEFAULT NULL COMMENT '描述',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    PRIMARY KEY (id),
    INDEX idx_config_key (config_key)
) COMMENT='系统配置表';
