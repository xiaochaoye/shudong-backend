# 树洞后端 (shudong-backend)

匿名倾诉社交平台后端，用户发布树洞、拾取帖子、共鸣/评论/私信互动，SSE 实时通知，含管理审核模块。

## 技术栈

Java 21 · Spring Boot 3.5 · MyBatis-Plus · MySQL · Redis · JWT · 腾讯 COS · SpringDoc + Knife4j

## 快速启动

```bash
# 1. 建库建表
mysql -u root -p123456 < sql/create_table_v2.sql
mysql -u root -p123456 shudong < sql/test_data_v2.sql   # 可选：测试数据

# 2. 配置本地密钥（复制并填写）
cp src/main/resources/application-local.yml.example src/main/resources/application-local.yml
# 填写：邮箱SMTP凭据、腾讯COS密钥

# 3. 启动
mvn spring-boot:run
# 服务运行于 http://localhost:8080/api
# API文档 http://localhost:8080/api/doc.html
```

## 项目结构

```
src/main/java/com/shudong/
├── ShudongApplication.java          # 启动类，@MapperScan 扫描5个mapper包
├── common/                          # 公共基础设施
│   ├── config/    SecurityConfig, JwtAuthenticationFilter, SseEmitterManager, RedisConfig, AsyncConfig
│   ├── exception/ BusinessException, GlobalExceptionHandler
│   ├── response/  Result<T>         # 统一响应 {code, message, data}
│   ├── utils/     JwtUtil, RedisUtil, PasswordUtil, CookieUtil, UploadUtil
│   └── View       JsonView层级: Basic → Detail → My
├── user/             用户模块       # 注册/登录/JWT刷新/设备管理/个人设置
├── post/             帖子模块       # 发帖/评论/共鸣/收藏/标签
├── pick/             拾取模块       # 智能拾取算法/日夜间限额/语录API
├── message/          消息模块       # 私信审核/SSE通知/异步邮件
└── admin/            管理模块       # 用户/帖子/评论管理·审核·统计·系统配置
```

## 数据库 (15张表)

| 模块 | 表 | 说明 |
|------|-----|------|
| 用户 | `users` `user_settings` `devices` | 软删除用户、个性化设置、登录设备 |
| 帖子 | `posts` `tags` `post_tags` `comments` `resonances` `collections` | 匿名/隐私帖、标签多对多、嵌套评论、共鸣(3种)、收藏分类 |
| 拾取 | `pick_records` `pick_configs` | 拾取记录(CASUAL_VIEW/EXPRESS_RESONANCE)、限额配置 |
| 消息 | `private_replies` `notifications` `email_templates` | 私信审核流(PENDING→APPROVED/REJECTED)、SSE通知、邮件模板 |
| 管理 | `admin_logs` `system_configs` | 操作审计、运行时配置(17项) |

## API 概览

所有接口前缀 `/api`，返回 `Result<T>` 统一格式。共 16 个 Controller、47 个端点。

### 用户模块

| 路径 | 方法 | 接口 |
|------|------|------|
| `/users/register/send-code` | POST | 发送注册验证码 |
| `/users/register` | POST | 用户注册 |
| `/users/login` | POST | 用户登录 |
| `/users/logout` | POST | 用户登出 |
| `/users/refresh` | POST | 刷新 Access Token |
| `/users/forgot-password/send-code` | POST | 发送重置密码验证码 |
| `/users/forgot-password/reset` | POST | 重置密码 |
| `/users/profile/update` | POST | 更新用户资料（名称/头像） |
| `/user-settings` | GET | 获取当前用户设置 |
| `/user-settings` | PUT | 更新用户设置 |
| `/user-settings/reset` | POST | 重置为默认设置 |
| `/devices` | GET | 获取当前用户设备列表 |
| `/devices/register` | POST | 注册设备 |
| `/devices/{deviceId}/deactivate` | PUT | 停用设备 |
| `/devices/{deviceId}/refresh-login` | PUT | 更新设备最后登录时间 |

### 帖子模块

| 路径 | 方法 | 接口 |
|------|------|------|
| `/posts` | POST | 发帖 |
| `/posts/{postId}` | GET | 获取帖子详情(+浏览量) |
| `/posts/{postId}` | PUT | 编辑帖子 |
| `/posts/{postId}` | DELETE | 软删除帖子 |
| `/posts/user/{userId}` | GET | 获取用户帖子列表(分页) |
| `/posts/{postId}/comments` | GET | 获取帖子评论列表(分页) |
| `/posts/{postId}/comments` | POST | 发表评论 |
| `/posts/{postId}/comments/{commentId}` | DELETE | 删除评论(级联子评论) |
| `/resonances` | POST | 添加共鸣 |
| `/resonances` | DELETE | 取消共鸣 |
| `/resonances/post/{postId}` | GET | 获取帖子的共鸣列表 |
| `/resonances/check` | GET | 检查当前用户是否已共鸣 |
| `/collections` | POST | 添加收藏 |
| `/collections` | DELETE | 取消收藏 |
| `/collections` | GET | 获取当前用户收藏列表 |
| `/collections/check` | GET | 检查当前用户是否已收藏 |

### 拾取模块

| 路径 | 方法 | 接口 |
|------|------|------|
| `/picks` | GET | 智能拾取帖子 |
| `/picks/replied` | POST | 标记帖子已回应 |
| `/picks/limit` | GET | 查询拾取频率限额 |
| `/picks/test-saying` | GET | 测试获取语录 |

### 消息模块

| 路径 | 方法 | 接口 |
|------|------|------|
| `/posts/{postId}/private-reply` | POST | 发送私信(进入PENDING审核) |
| `/notifications/subscribe` | GET | SSE订阅实时通知(长连接) |
| `/notifications` | GET | 获取通知列表(分页) |
| `/notifications/unread-count` | GET | 获取未读通知数量 |
| `/notifications/{notificationId}/read` | PUT | 标记单条通知已读 |
| `/notifications/read-all` | PUT | 标记所有通知已读 |

### 管理模块 (`/admin`)

| 路径 | 方法 | 接口 |
|------|------|------|
| `/admin/users` | GET | 用户列表(分页/关键词/状态筛选) |
| `/admin/users/{userId}` | GET | 用户详情 |
| `/admin/users/{userId}/status` | PUT | 更新用户状态(封禁/解封) |
| `/admin/users/{userId}` | DELETE | 删除用户 |
| `/admin/posts` | GET | 帖子列表(分页/关键词/状态筛选) |
| `/admin/posts/{postId}` | DELETE | 软删除帖子 |
| `/admin/comments` | GET | 评论列表(分页/按帖子筛选) |
| `/admin/comments/{commentId}` | DELETE | 软删除评论 |
| `/admin/reviews/pending` | GET | 待审核私信列表(分页) |
| `/admin/reviews/{reviewId}/approve` | POST | 审核通过(触发通知+邮件) |
| `/admin/reviews/{reviewId}/reject` | POST | 审核驳回(需提供原因) |
| `/admin/stats/overview` | GET | 系统概览统计 |
| `/admin/stats/users` | GET | 用户统计 |
| `/admin/stats/posts` | GET | 帖子统计 |
| `/admin/stats/interactions` | GET | 互动统计 |
| `/admin/configs` | GET | 获取所有系统配置 |
| `/admin/configs/{id}` | PUT | 更新系统配置项 |

## 认证机制

```
登录 → Access Token(2h, Header) + Refresh Token(7d, HttpOnly Cookie)
注销 → Redis黑名单Access Token + 清除Cookie
刷新 → Cookie中Refresh Token → 新Access Token
权限 → /admin/** 需ADMIN角色，其余认证端点需登录
```

## 关键设计

- **拾取算法**：未回复奖励 × 时间衰减 × 浏览量权重，Redis日/夜间限额计数
- **私信审核**：发送→PENDING → 管理员APPROVED(触发通知+邮件) / REJECTED
- **SSE推送**：ConcurrentHashMap<userId, SseEmitter> 实时通知
- **文件上传**：腾讯COS + 自动WebP转换(头像2MB/图片10MB)
- **验证码**：Redis存储5分钟TTL，每日5次上限，1分钟冷却
- **软删除**：users/posts/comments 均用 record_status + deleted_at
- **分页**：MyBatis-Plus Page<T>，所有列表接口支持

## 配置文件

| 文件 | 用途 | 是否提交 |
|------|------|----------|
| `application.yml` | 主配置(端口/数据库/Redis/JWT/CORS/文档) | ✅ |
| `application-local.yml` | 密钥(邮箱SMTP/COS) | ❌ gitignore |

## 开发须知

- 公共端点(免登录)：`/users/**`、`/users/login`、`/users/refresh`、`/users/forgot-password/**`、API文档路径
- CORS允许来源：`localhost:5173`、`localhost:3000`
- 异步线程池：core=2, max=4, queue=50（邮件发送用）
- 邮件模板：Thymeleaf渲染，支持数据库模板(EmailTemplates表)和静态模板
- 无单元测试，`src/test/` 为空
