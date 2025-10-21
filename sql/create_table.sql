-- -----------------------------------------------
-- 表1: users - 用户表
-- -----------------------------------------------
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT COMMENT '用户ID，主键，自增',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT '邮箱，唯一，用于登录',
    username VARCHAR(50) NOT NULL COMMENT '用户名，可显示在帖子上',
    password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希值（使用 bcrypt 加密，包含内置 salt）',
    avatar VARCHAR(512) DEFAULT NULL COMMENT '用户头像图片的URL链接，可为空',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    is_active BOOLEAN DEFAULT TRUE COMMENT '账号是否启用（软删除用）',
    is_admin BOOLEAN DEFAULT FALSE COMMENT '是否为管理员',
    PRIMARY KEY (id),
    INDEX idx_email (email),
    INDEX idx_username (username)
) COMMENT='用户表，存储注册用户信息';


-- -----------------------------------------------
-- 表2: posts - 帖子表（快乐区 / 难过区 / 许愿池）
-- -----------------------------------------------
CREATE TABLE posts (
    id BIGINT AUTO_INCREMENT COMMENT '帖子ID，主键，自增',
    user_id BIGINT NOT NULL COMMENT '发布者用户ID，外键关联users.id',
    title VARCHAR(200) NOT NULL COMMENT '帖子标题',
    content TEXT NOT NULL COMMENT '帖子正文内容',
    post_type ENUM('happy', 'sad', 'wish') NOT NULL COMMENT '帖子类型：happy=快乐区，sad=难过区，wish=许愿池',
    is_anonymous BOOLEAN DEFAULT FALSE COMMENT '是否匿名发布',
    is_published BOOLEAN DEFAULT TRUE COMMENT '是否已发布（用于审核流程）',
    view_count INT UNSIGNED DEFAULT 0 COMMENT '浏览次数，用于热门排序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    deleted_at TIMESTAMP NULL DEFAULT NULL COMMENT '软删除时间，为空表示未删除',
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_post_type (post_type),
    INDEX idx_created_at (created_at),
    INDEX idx_deleted_at (deleted_at),
    INDEX idx_is_published (is_published),
    FULLTEXT idx_ft_title_content (title, content)
) COMMENT='帖子表，支持快乐区、难过区、许愿池';


-- -----------------------------------------------
-- 表3: tags - 标签表
-- -----------------------------------------------
CREATE TABLE tags (
    id BIGINT AUTO_INCREMENT COMMENT '标签ID，主键，自增',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称，如“焦虑”、“恋爱”、“美食”等',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_name (name)
) COMMENT='标签表，用于给帖子打标签';


-- -----------------------------------------------
-- 表4: post_tags - 帖子与标签的多对多关联表
-- -----------------------------------------------
CREATE TABLE post_tags (
    id BIGINT AUTO_INCREMENT COMMENT '主键ID，自增',
    post_id BIGINT NOT NULL COMMENT '帖子ID，外键',
    tag_id BIGINT NOT NULL COMMENT '标签ID，外键',
    PRIMARY KEY (id),
    UNIQUE KEY uk_post_tag (post_id, tag_id) COMMENT '确保帖子与标签的唯一关联',
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE,
    INDEX idx_post_id (post_id),
    INDEX idx_tag_id (tag_id)
) COMMENT='帖子与标签的关联表，实现多对多关系，使用代理主键 id 支持 MyBatis-Plus';


-- -----------------------------------------------
-- 表5: comments - 评论表
-- -----------------------------------------------
CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT COMMENT '评论ID，主键，自增',
    post_id BIGINT NOT NULL COMMENT '被评论的帖子ID',
    user_id BIGINT NOT NULL COMMENT '评论者用户ID',
    content TEXT NOT NULL COMMENT '评论内容',
    is_anonymous BOOLEAN DEFAULT FALSE COMMENT '是否匿名评论',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    deleted_at TIMESTAMP NULL DEFAULT NULL COMMENT '软删除时间',
    PRIMARY KEY (id),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_deleted_at (deleted_at)
) COMMENT='评论表，用户对帖子的评论';


-- -----------------------------------------------
-- 表6: likes - 点赞表（快乐区“同乐”）
-- -----------------------------------------------
CREATE TABLE likes (
    id BIGINT AUTO_INCREMENT COMMENT '点赞ID，主键，自增',
    post_id BIGINT NOT NULL COMMENT '被点赞的帖子ID',
    user_id BIGINT NOT NULL COMMENT '点赞用户ID',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否有效点赞，支持取消点赞',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    PRIMARY KEY (id),
    UNIQUE KEY unique_user_post (post_id, user_id) COMMENT '防止同一用户重复点赞',
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_is_active (is_active)
) COMMENT='点赞表，记录用户对快乐区帖子的“同乐”，支持取消点赞';


-- -----------------------------------------------
-- 表7: replies - 邮件回复安慰（难过区专用）
-- -----------------------------------------------
CREATE TABLE replies (
    id BIGINT AUTO_INCREMENT COMMENT '回复ID，主键，自增',
    post_id BIGINT NOT NULL COMMENT '被回复的难过区帖子ID',
    reply_content TEXT NOT NULL COMMENT '安慰邮件内容',
    sender_user_id BIGINT NULL COMMENT '发送者用户ID，NULL表示系统自动发送',
    sender_type ENUM('admin', 'system') NOT NULL DEFAULT 'admin' COMMENT '发送者类型：admin=管理员，system=系统自动',
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    status ENUM('sent', 'failed') DEFAULT 'sent' COMMENT '发送状态：sent=成功，failed=失败',
    PRIMARY KEY (id),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_user_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_post_id (post_id),
    INDEX idx_sender_user_id (sender_user_id)
) COMMENT='难过区邮件回复记录表，支持管理员或系统发送';


-- -----------------------------------------------
-- 表8: admin_actions - 管理员操作日志
-- -----------------------------------------------
CREATE TABLE admin_actions (
    id BIGINT AUTO_INCREMENT COMMENT '操作日志ID，主键，自增',
    admin_id BIGINT NOT NULL COMMENT '执行操作的管理员用户ID',
    action_type ENUM('publish', 'delete', 'review', 'edit') NOT NULL COMMENT '操作类型：publish=发布，delete=删除，review=审核，edit=编辑',
    target_type ENUM('post', 'comment', 'user', 'tag') NOT NULL COMMENT '目标类型',
    target_id BIGINT NOT NULL COMMENT '操作的目标ID（如帖子ID）',
    action_details TEXT COMMENT '操作详情（可记录修改前/后内容）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (id),
    FOREIGN KEY (admin_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_admin_id (admin_id),
    INDEX idx_target (target_type, target_id)
) COMMENT='管理员操作日志表，用于审计';