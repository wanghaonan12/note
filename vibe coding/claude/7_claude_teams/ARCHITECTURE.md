# 设计服务平台 — 系统架构文档

> **版本**: v2.0-完整版  
> **日期**: 2026-07-08  
> **产出方**: 8 位系统架构师辩论团队  
> **架构师**: 雷恩(分布式)、陈默(一致性)、Alex(云原生)、林薇(安全)、老周(务实)、张量(扩容)、叶岚(业务扩展/DDD)、铁壁(高可用)

---

## 目录

1. [项目概述](#1-项目概述)
2. [架构决策记录](#2-架构决策记录)
3. [系统架构总览](#3-系统架构总览)
4. [模块设计](#4-模块设计)
5. [数据设计](#5-数据设计)
6. [安全架构](#6-安全架构)
7. [部署架构](#7-部署架构)
8. [动态扩容方案](#8-动态扩容方案)
9. [业务扩展架构](#9-业务扩展架构)
10. [高可用与容灾](#10-高可用与容灾)
11. [演进路线图](#11-演进路线图)
12. [AI 辅助开发指南](#12-ai-辅助开发指南)
13. [附录：辩论纪要](#13-附录辩论纪要)

---

## 1. 项目概述

### 1.1 业务描述

设计服务平台，用户通过以下方式使用：

1. 浏览平台预置的图片模板（按领域/风格分类的海报和缩略图）
2. 选择满意的模板类型
3. 描述自己的图片需求（基于模板预设的提示词框架）
4. 调用 AI 图片模型生成个性化图片
5. 生成的图片保存到用户账户，可查看/下载

### 1.2 关键约束

| 维度 | 约束 |
|------|------|
| 团队规模 | 1 人开发 + AI 辅助编码 |
| MVP 周期 | 1 个月 |
| 日活用户 | 10w |
| 峰值 QPS | 10,000 |
| 总用户量 | 5,000w |
| 安全级别 | 金融级 |
| Token 预算 | 1,000 元 |
| 技术栈 | 后端 Spring Boot / 前端 Vue / 部署 Docker |
| 扩展性 | 业务会不断扩展 |

---

## 2. 架构决策记录

### ADR-001: 采用模块化单体架构

| 属性 | 值 |
|------|-----|
| **状态** | 已接受 |
| **决策者** | 5 位架构师共识 |
| **背景** | 单体 vs 微服务争论 |
| **方案** | MVP 采用模块化单体，模块间通过接口通信，预留微服务拆分点 |
| **理由** | 1) 单人开发 + 1个月周期无法支撑微服务运维成本 2) 模块化单体代码组织清晰，未来拆分只需提取 Service 3) 10000 QPS 单体可扛（配合连接池优化和异步处理） |
| **后果** | 初期部署简单，但单体 JVM 内存和 GC 压力需监控；未来拆分时需确保模块接口稳定 |

### ADR-002: 图片生成采用异步线程池

| 属性 | 值 |
|------|-----|
| **状态** | 已接受 |
| **决策者** | 雷恩让步，老周主导 |
| **背景** | 同步等待 vs 消息队列 vs 线程池 |
| **方案** | MVP 使用 Spring @Async + 固定大小线程池（核心 20，最大 50），图片生成结果通过轮询或 WebSocket 返回 |
| **理由** | 1) Kafka 引入增加运维复杂度，单人难以维护 2) 线程池 + 合理队列长度可满足 10000 QPS 峰值 3) 未来可无缝替换为消息队列 |
| **后果** | 高峰期线程池满时会拒绝请求（需配合限流）；不如消息队列健壮 |

### ADR-003: 数据一致性采用本地事务 + 幂等表

| 属性 | 值 |
|------|-----|
| **状态** | 已接受 |
| **决策者** | 陈默让步，老周主导 |
| **背景** | 强一致性 vs 最终一致性 |
| **方案** | 本地事务保证扣费和生成的一致性，幂等表防止重复扣费 |
| **理由** | 1) MVP 阶段数据量不大，本地事务足够 2) 幂等表保证金融级安全性 3) 事件溯源可在后期引入 |
| **后果** | 跨服务一致性需自行处理；未来拆分服务时需引入分布式事务 |

### ADR-004: 分库分表策略 — MVP 单表，预留分片键

| 属性 | 值 |
|------|-----|
| **状态** | 已接受 |
| **决策者** | 全体共识 |
| **背景** | 5000w 用户是否需要立即分库分表 |
| **方案** | MVP 使用单表，所有表包含 `user_id` 列并建立索引；图片生成任务表预留分片键 |
| **理由** | 1) 5000w 行在 MySQL 8.0 下约 2-3GB，加索引查询 < 10ms 2) 分库分表增加复杂度，MVP 阶段不值得 3) 预留分片键确保未来迁移成本最低 |
| **后果** | 中期需关注单表性能；迁移时需停机或双写 |

### ADR-005: 认证授权采用 JWT + RBAC

| 属性 | 值 |
|------|-----|
| **状态** | 已接受 |
| **决策者** | 林薇主导，全体同意 |
| **背景** | 金融级安全要求的接口级权限控制 |
| **方案** | Access Token (JWT, 2h 过期) + Refresh Token (7d 过期)，RBAC 接口级权限 |
| **理由** | 1) JWT 无状态，适合单体部署 2) RBAC 满足接口级权限 3) 双令牌模式平衡安全性和用户体验 |
| **后果** | JWT 黑名单需 Redis 支持；未来微服务需统一认证中心 |

### ADR-006: 部署采用 Docker Compose

| 属性 | 值 |
|------|-----|
| **状态** | 已接受 |
| **决策者** | Alex 让步，老周主导 |
| **背景** | K8s vs Docker Compose |
| **方案** | MVP 使用 Docker Compose 部署，包含 Spring Boot、Vue、MySQL、Redis、OSS 客户端 |
| **理由** | 1) 单人开发无需 K8s 学习曲线 2) Docker Compose 一键启动所有服务 3) 未来可无缝迁移到 K8s |
| **后果** | 缺少自动扩缩容；部署灵活性低于 K8s |

---

## 3. 系统架构总览

```
                                    ┌─────────────────────┐
                                    │      Nginx          │
                                    │  反向代理 + 限流     │
                                    │  SSL 终止           │
                                    └─────────┬───────────┘
                                              │
                    ┌─────────────────────────┼─────────────────────────┐
                    │                         │                         │
             ┌──────▼──────┐          ┌───────▼────────┐        ┌────────▼────────┐
             │  Vue SPA    │          │  Spring Boot   │        │   AI Image API  │
             │  (前端)     │◄────────►│  Monolith      │◄──────►│  (第三方)       │
             │  CDN 部署   │  HTTP    │                │  HTTP  │               │
             └─────────────┘          └───┬────┬───────┘        └─────────────────┘
                                         │    │
                                   ┌─────▼┐  ┌▼──────────┐
                                   │Redis │  │   MySQL    │
                                   │限流/ │  │  主从复制  │
                                   │缓存 │  └────────────┘
                                   └─────┘
                                         │
                                   ┌─────▼─────┐
                                   │  OSS/S3   │
                                   │ 图片存储   │
                                   └───────────┘
```

### 3.1 组件说明

| 组件 | 用途 | 技术选型 |
|------|------|----------|
| Nginx | 反向代理、SSL 终止、网关限流 | Nginx 1.25+ |
| Vue SPA | 用户界面 | Vue 3 + Vite + Element Plus |
| Spring Boot | 后端单体服务 | Spring Boot 3.2 + JDK 21 |
| MySQL | 持久化存储 | MySQL 8.0 (主从) |
| Redis | 限流、缓存、会话 | Redis 7.x |
| OSS/S3 | 图片存储 | 阿里云 OSS / AWS S3 |
| AI Image API | 图片生成 | 通义万相 / DALL-E API |

---

## 4. 模块设计

### 4.1 项目结构

```
src/main/java/com/designplatform/
├── config/                    # 配置类
│   ├── AsyncConfig.java       # 异步线程池配置
│   ├── JwtConfig.java         # JWT 配置
│   ├── RedisConfig.java       # Redis 配置
│   └── SecurityConfig.java    # 安全配置
├── common/                    # 公共组件
│   ├── ApiResponse.java       # 统一响应
│   ├── BusinessException.java # 业务异常
│   ├── IdempotentUtil.java    # 幂等工具
│   └── RateLimitUtil.java     # 限流工具
├── auth/                      # 认证授权模块
│   ├── controller/AuthController.java
│   ├── service/AuthService.java
│   ├── model/User.java
│   ├── model/Role.java
│   ├── model/Permission.java
│   └── filter/JwtAuthenticationFilter.java
├── image/                     # 图片生成模块
│   ├── controller/ImageController.java
│   ├── service/ImageGenerationService.java
│   ├── service/ImageUrlService.java
│   ├── model/ImageTask.java
│   ├── model/ImageResult.java
│   └── async/ImageGeneratorWorker.java
├── template/                  # 模板管理模块
│   ├── controller/TemplateController.java
│   ├── service/TemplateService.java
│   ├── model/TemplateCategory.java
│   └── model/ImageTemplate.java
├── billing/                   # 次数计费模块
│   ├── controller/BillingController.java
│   ├── service/BillingService.java
│   ├── model/UserAccount.java
│   ├── model/QuotaTransaction.java
│   └── model/IdempotentKey.java
└── platform/                  # 平台管理模块 (后台)
    ├── controller/AdminController.java
    ├── service/PlatformService.java
    └── model/               # 后台管理相关实体
```

### 4.2 核心模块职责

#### Auth 模块
- 用户注册/登录
- JWT Token 签发和验证
- RBAC 权限控制
- 密码加密存储 (BCrypt)

#### Image 模块
- 接收用户图片生成请求
- 调用 AI 图片模型 API（异步）
- 管理图片生成任务状态
- 图片上传到 OSS
- 图片 URL 管理和 CDN 加速

#### Template 模块
- 后台管理图片模板分类
- 后台管理提示词模板
- 前台展示模板列表（分页 + 搜索）
- 模板缩略图管理

#### Billing 模块
- 用户次数扣减（原子操作）
- 流水记录（不可变审计）
- 幂等控制（防重复扣费）
- 余额查询和管理

---

## 5. 数据设计

### 5.1 ER 关系图

```
┌──────────┐     ┌──────────────┐     ┌─────────────────┐
│  user    │────<│ image_task   │     │ template        │
│──────────│     │──────────────│     │─────────────────│
│ id       │     │ id            │     │ id               │
│ username │     │ user_id (FK)  │     │ category_id (FK) │
│ email    │     │ prompt        │     │ name             │
│ password │     │ template_id   │     │ prompt_template  │
│ role     │     │ status        │     │ thumbnail_url    │
│ balance  │     │ result_url    │     │ sort_order       │
│ created_at│    │ created_at    │     │ created_at       │
└──────────┘     │ updated_at    │     └─────────────────┘
                 └──────────────┘              │
                 ┌──────────────┐              │
                 │ quota_txn    │<─────────────┘
                 │──────────────│
                 │ id            │
                 │ user_id (FK)  │
                 │ amount        │
                 │ order_id      │
                 │ biz_type      │
                 │ status        │
                 │ created_at    │
                 └──────────────┘

                 ┌──────────────────┐
                 │ idempotent_key   │
                 │──────────────────│
                 │ biz_id (PK)      │
                 │ user_id (FK)     │
                 │ biz_type         │
                 │ created_at       │
                 └──────────────────┘
```

### 5.2 核心表结构

#### user_account (用户账户表)

```sql
CREATE TABLE user_account (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,       -- BCrypt 加密
    phone VARCHAR(20),                         -- 可选，加密存储
    balance INT NOT NULL DEFAULT 0,            -- 剩余生成次数
    total_generated INT NOT NULL DEFAULT 0,    -- 累计生成次数
    status TINYINT NOT NULL DEFAULT 1,         -- 1:正常 0:禁用
    version INT NOT NULL DEFAULT 0,            -- 乐观锁
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### image_task (图片生成任务表)

```sql
CREATE TABLE image_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    order_id VARCHAR(64) NOT NULL UNIQUE,      -- 幂等键
    template_id BIGINT NOT NULL,               -- 关联模板
    user_prompt TEXT NOT NULL,                 -- 用户描述
    status TINYINT NOT NULL DEFAULT 0,         -- 0:pending 1:processing 2:success 3:failed
    result_url VARCHAR(512),                   -- 生成结果 URL
    ai_model VARCHAR(32),                      -- 使用的 AI 模型
    error_message VARCHAR(512),                -- 失败原因
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(user_id),
    FOREIGN KEY (template_id) REFERENCES image_template(id),
    INDEX idx_user_status (user_id, status),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### quota_transaction (次数流水表)

```sql
CREATE TABLE quota_transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    amount INT NOT NULL,                       -- 负数表示扣除
    order_id VARCHAR(64) NOT NULL,             -- 关联订单号
    biz_type VARCHAR(32) NOT NULL,             -- IMAGE_GEN / REFUND / ADJUST
    status TINYINT NOT NULL DEFAULT 1,         -- 1:成功 2:失败 3:回滚
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(user_id),
    INDEX idx_user_time (user_id, created_at),
    INDEX idx_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### idempotent_key (幂等表)

```sql
CREATE TABLE idempotent_key (
    biz_id VARCHAR(64) PRIMARY KEY,            -- 业务唯一ID
    user_id BIGINT NOT NULL,
    biz_type VARCHAR(32) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### image_template (图片模板表)

```sql
CREATE TABLE image_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    prompt_template TEXT NOT NULL,             -- 提示词模板
    thumbnail_url VARCHAR(512) NOT NULL,       -- 缩略图 URL
    preview_url VARCHAR(512),                  -- 预览大图 URL
    sort_order INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,         -- 1:上架 0:下架
    created_by BIGINT,                         -- 后台管理员ID
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES template_category(id),
    INDEX idx_category_status (category_id, status),
    INDEX idx_sort (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

#### template_category (模板分类表)

```sql
CREATE TABLE template_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    icon_url VARCHAR(512),
    sort_order INT NOT NULL DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_sort (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 5.3 5000w 用户数据存储方案

| 阶段 | 用户量 | 方案 | 预估数据量 |
|------|--------|------|-----------|
| MVP | 0-500w | 单库单表 | user_account: ~200MB<br>image_task: ~5GB (每人10张) |
| 增长期 | 500w-2000w | 单库 + 读写分离 | MySQL 主库写入，从库查询 |
| 成熟期 | 2000w-5000w+ | ShardingSphere 分表 | 按 user_id 哈希分 64 表 |

**关键原则**：
1. 所有表包含 `user_id` 列，预留分片键
2. 图片文件存 OSS，数据库只存 URL（元数据）
3. 流水表按年分区，历史数据归档
4. 热点模板数据用 Redis 缓存

---

## 6. 安全架构

### 6.1 金融级安全三层防护

```
┌─────────────────────────────────────────────────────────────┐
│  第一层: 网关安全 (Nginx + WAF)                               │
│  · DDoS 防护                                                  │
│  · SQL 注入防护                                               │
│  · XSS 防护                                                   │
│  · IP 黑白名单                                                 │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│  第二层: 应用安全 (Spring Security)                           │
│  · JWT 认证 + RBAC 权限                                      │
│  · 接口级限流 (Redis + Lua)                                  │
│  · 次数精确控制 (数据库行锁)                                  │
│  · 幂等控制 (防重复请求)                                     │
│  · 输入验证 + 参数绑定                                       │
│  · CORS 策略                                                 │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│  第三层: 数据安全 (MySQL + OSS)                               │
│  · 密码 BCrypt 加密                                          │
│  · 敏感字段 AES-256 加密 (手机号等)                          │
│  · 传输 TLS 1.3                                             │
│  · 操作审计日志 (不可变)                                     │
│  · 每日对账 (流水 vs 余额)                                   │
└─────────────────────────────────────────────────────────────┘
```

### 6.2 认证授权流程

```
用户登录
    │
    ▼
验证用户名密码 (BCrypt)
    │
    ▼
签发 JWT (Access Token, 2h)
签发 Refresh Token (7d, 存 Redis)
    │
    ▼
后续请求携带 Access Token
    │
    ▼
JwtAuthenticationFilter 验证 Token
    │
    ├─ 有效 → 注入用户信息到 SecurityContext
    │          → RBAC 权限检查
    │          → 路由到对应 Controller
    │
    └─ 无效/过期 → 返回 401
                   → 尝试用 Refresh Token 续签
                      ├─ 成功 → 签发新 Token
                      └─ 失败 → 要求重新登录
```

### 6.3 限流控制

**用户级限流** (Redis + Lua 原子脚本):

```lua
-- 每秒限流脚本
local key = KEYS[1]          -- user:{userId}:request:minute
local limit = tonumber(ARGV[1])  -- 每分钟最大请求数 (如 60)
local now = tonumber(ARGV[2])
local window = 60            -- 1 分钟窗口

-- 清理过期记录
redis.call('ZREMRANGEBYSCORE', key, 0, now - window)
local count = redis.call('ZCARD', key)

if count < limit then
    redis.call('ZADD', key, now, now .. ':' .. math.random(1000000))
    redis.call('EXPIRE', key, window + 1)
    return 1  -- 允许
else
    return 0  -- 拒绝
end
```

**全局限流** (Nginx limit_req):

```nginx
limit_req_zone $binary_remote_addr zone=global:10m rate=200r/s;

location /api/ {
    limit_req zone=global burst=100 nodelay;
    proxy_pass http://springboot;
}
```

### 6.4 次数控制 (金融级精确扣减)

```java
@Transactional(rollbackFor = Exception.class)
public void deductQuota(Long userId, int amount, String orderId) {
    // 1. 幂等检查
    IdempotentKey key = idempotentMapper.selectByBizId(orderId);
    if (key != null) {
        throw new DuplicateRequestException("重复请求");
    }

    // 2. 行锁查询 + 余额校验
    UserAccount account = accountMapper.selectForUpdate(userId);
    if (account.getBalance() < amount) {
        throw new InsufficientBalanceException("次数不足");
    }

    // 3. 原子扣减 (乐观锁)
    int rows = accountMapper.deductWithVersion(userId, amount, account.getVersion());
    if (rows == 0) {
        throw new ConcurrencyException("并发冲突，请重试");
    }

    // 4. 写入流水
    quotaTransactionMapper.insert(buildTransaction(userId, amount, orderId));

    // 5. 写入幂等标记
    idempotentKeyMapper.insert(new IdempotentKey(orderId, userId));
}
```

### 6.5 数据加密

| 数据类型 | 加密方式 | 说明 |
|----------|---------|------|
| 用户密码 | BCrypt | 单向哈希，不可逆 |
| 手机号 | AES-256-GCM | 对称加密，密钥存 KMS |
| 图片 URL | 签名 URL (OSS) | 有时效性，防盗链 |
| 传输通道 | TLS 1.3 | HTTPS 强制 |
| JWT Secret | 环境变量 / KMS | 定期轮换 |

---

## 7. 部署架构

### 7.1 Docker Compose (MVP)

```yaml
# docker-compose.yml
version: '3.8'

services:
  # 前端
  vue-frontend:
    build: ./frontend
    ports:
      - "80:80"
    depends_on:
      - nginx
    networks:
      - platform-net

  # 反向代理
  nginx:
    image: nginx:1.25-alpine
    ports:
      - "443:443"
      - "80:80"
    volumes:
      - ./nginx/conf.d:/etc/nginx/conf.d
      - ./nginx/ssl:/etc/nginx/ssl
    depends_on:
      - spring-boot
    networks:
      - platform-net

  # 后端
  spring-boot:
    build: ./backend
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - DB_HOST=mysql
      - REDIS_HOST=redis
      - OSS_BUCKET=${OSS_BUCKET}
      - AI_API_KEY=${AI_API_KEY}
    depends_on:
      - mysql
      - redis
    networks:
      - platform-net

  # 数据库
  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - MYSQL_DATABASE=design_platform
      - MYSQL_USER=app
      - MYSQL_PASSWORD=${MYSQL_PASSWORD}
    volumes:
      - mysql-data:/var/lib/mysql
      - ./db/init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "3306:3306"
    networks:
      - platform-net

  # 缓存
  redis:
    image: redis:7-alpine
    command: redis-server --requirepass ${REDIS_PASSWORD}
    volumes:
      - redis-data:/data
    ports:
      - "6379:6379"
    networks:
      - platform-net

volumes:
  mysql-data:
  redis-data:

networks:
  platform-net:
    driver: bridge
```

### 7.2 部署步骤

```bash
# 1. 克隆代码
git clone <repo-url> && cd design-platform

# 2. 配置环境变量
cp .env.example .env
# 编辑 .env 填入数据库密码、OSS 密钥、AI API Key 等

# 3. 构建镜像
docker-compose build

# 4. 启动服务
docker-compose up -d

# 5. 验证
curl http://localhost:8080/api/health
```

### 7.3 前端部署 (Vue SPA)

```dockerfile
# frontend/Dockerfile
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

---

## 8. 动态扩容方案

> **产出方**: 架构师·张量 (弹性伸缩专家)  
> **用户确认**: 流量模式 = D (混合流量: 读多写少 + 突发脉冲)

### 8.1 流量特征分析

| 流量类型 | 占比 | 特征 | 应对策略 |
|----------|------|------|----------|
| 模板浏览 (读) | 70-80% | 稳定，有波峰波谷 | Redis 缓存 + CDN |
| 图片下载 (读) | 10-15% | 稳定 | CDN 直传 OSS |
| 图片生成 (写) | 5-10% | 突发脉冲，延迟敏感 | 异步线程池 + 弹性扩容 |
| 后台管理 (写) | <1% | 极低频 | 无特殊处理 |

**波峰波谷规律**：
- 白天 (9:00-21:00): 日均 QPS 6000-8000
- 夜间 (21:00-次日9:00): 日均 QPS 1000-3000
- 突发脉冲: 营销活动/热门模板引爆，瞬时 QPS 可达 10000+

### 8.2 MVP 阶段扩容策略 (Month 1-3)

**核心原则**: 不引入 K8s，用 Docker Compose + 配置优化应对扩容

#### 8.2.1 连接池调优

```yaml
# application-docker.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 50           # 默认 10 → 50，应对 10000 QPS
      minimum-idle: 10
      connection-timeout: 3000        # 3 秒超时，快速失败
      idle-timeout: 600000            # 10 分钟空闲回收
      max-lifetime: 1800000           # 30 分钟连接生命周期
      connection-test-query: SELECT 1 # 连接有效性检测

  jackson:
    serialization:
      write-durations-as-timestamps: true # 减少序列化开销

server:
  tomcat:
    threads:
      max: 200                      # 默认 200，应对并发
      min-spare: 20
    max-connections: 8192           # 最大连接数
    accept-count: 100               # 等待队列长度
```

#### 8.2.2 线程池配置

```java
@Configuration
public class AsyncConfig {

    @Bean("imageGenExecutor")
    public Executor imageGenerationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);        // 常驻 20 个线程处理图片生成
        executor.setMaxPoolSize(50);         // 高峰期最多 50 个
        executor.setQueueCapacity(500);      // 队列最多排队 500 个任务
        executor.setThreadNamePrefix("img-gen-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // CallerRunsPolicy: 队列满时由调用线程自己执行，天然背压
        executor.initialize();
        return executor;
    }

    @Bean("httpCallExecutor")
    public Executor httpCallExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("http-call-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // 丢弃策略: 非核心 HTTP 调用在极端情况下可丢弃
        executor.initialize();
        return executor;
    }
}
```

#### 8.2.3 Redis 缓存策略

```yaml
# 缓存命中率目标: > 90%
spring:
  cache:
    type: redis
    redis:
      time-to-live: 3600000        # 模板数据缓存 1 小时
      cache-null-values: false     # 不缓存 null 值
      key-prefix: "dp:"            # 统一 key 前缀
```

**缓存分层设计**:

| 缓存层 | 数据 | TTL | 命中目标 |
|--------|------|-----|----------|
| L1: Caffeine (本地) | 热门模板列表 | 5 分钟 | > 80% |
| L2: Redis | 模板详情、用户信息 | 1 小时 | > 90% |
| L3: CDN | 图片缩略图、静态资源 | 24 小时 | > 95% |
| L4: 源站 (MySQL) | 兜底 | - | < 5% |

**Caffeine 本地缓存配置**:
```java
@Bean
public Cache<String, ImageTemplate> templateLocalCache() {
    return Caffeine.newBuilder()
        .maximumSize(5000)              // 最多缓存 5000 个模板
        .expireAfterWrite(Duration.ofMinutes(5))
        .recordStats()                  // 统计命中率
        .build();
}
```

### 8.3 增长期扩容策略 (Month 4-12)

**触发条件**: 日活 > 10w 且 QPS > 10000 持续 1 个月

#### 8.3.1 水平扩容 (Docker Compose → 多实例)

```yaml
# docker-compose.prod.yml
version: '3.8'

services:
  spring-boot:
    deploy:
      replicas: 3                     # 3 个实例分担负载
      resources:
        limits:
          cpus: '2.0'
          memory: 4G
        reservations:
          cpus: '0.5'
          memory: 1G

  mysql:
    deploy:
      resources:
        limits:
          cpus: '4.0'
          memory: 8G

  redis:
    command: redis-server --requirepass ${REDIS_PASSWORD} --maxmemory 2gb --maxmemory-policy allkeys-lru
```

#### 8.3.2 数据库读写分离

```yaml
# 主库: 所有写操作
spring.datasource.master.jdbc-url: jdbc:mysql://mysql-master:3306/design_platform
spring.datasource.master.username: app
spring.datasource.master.password: ${MYSQL_PASSWORD}

# 从库: 读操作 (3 个从库负载均衡)
spring.datasource.slave.jdbc-url: jdbc:mysql://mysql-slave:3306/design_platform
spring.datasource.slave.read-hosts: slave1:3306,slave2:3306,slave3:3306
```

#### 8.3.3 预测性扩容规则

| 指标 | 阈值 | 动作 | 执行时机 |
|------|------|------|----------|
| CPU 使用率 | > 70% 持续 5 分钟 | 启动新 Spring Boot 实例 | 每小时检查 |
| 线程池队列长度 | > 400 (500 的 80%) | 紧急扩容 | 实时检查 |
| MySQL 连接池使用率 | > 80% | 增加连接池上限 | 每小时检查 |
| Redis 内存使用率 | > 70% | 增加 maxmemory 或扩容 | 每小时检查 |
| 图片生成平均延迟 | > 15 秒 | 增加线程池 maxPoolSize | 实时检查 |

### 8.4 成熟期扩容策略 (Year 2+)

**触发条件**: 用户量 > 1亿

| 维度 | 策略 | 目标 |
|------|------|------|
| 应用层 | 迁移到 K8s + HPA | CPU > 60% 自动扩 Pod |
| 数据库层 | ShardingSphere 分库分表 | 单表 < 2000w 行 |
| 缓存层 | Redis Cluster (6 节点) | 单节点 < 50% 内存 |
| 存储层 | OSS 生命周期管理 | 热数据 SSD，冷数据归档 |
| 消息层 | Kafka 集群 (3 broker) | 替代线程池 |

---

## 9. 业务扩展架构

> **产出方**: 架构师·叶岚 (DDD + 插件化架构专家)  
> **用户确认**: 业务扩展 = D (混合扩展: 横向 + 纵向 + 商业)

### 9.1 DDD 限界上下文划分

```
┌─────────────────────────────────────────────────────────────────┐
│                     设计服务平台 (Bounded Contexts)               │
│                                                                 │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐       │
│  │ 用户域   │  │ 模板域   │  │ 生成域   │  │ 计费域   │       │
│  │ User     │  │ Template │  │ Generate │  │ Billing  │       │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘  └────┬─────┘       │
│       │             │             │             │              │
│  ┌────▼─────┐  ┌────▼─────┐  ┌────▼─────┐  ┌────▼─────┐       │
│  │ 内容安全 │  │ 分析统计 │  │ 通知推送 │  │ 运营后台 │       │
│  │ Content  │  │ Analytics│  │ Notify   │  │ Admin    │       │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘       │
└─────────────────────────────────────────────────────────────────┘
```

**上下文映射关系**:

| 上游上下文 | 下游上下文 | 映射模式 | 说明 |
|-----------|-----------|----------|------|
| 用户域 | 计费域 | **ACL (防腐层)** | 用户域不直接引用计费实体，通过 DTO 转换 |
| 模板域 | 生成域 | **发布语言** | 模板变更通过事件发布，生成域订阅 |
| 生成域 | 计费域 | **客户-供应商** | 生成域请求计费域扣除次数 |
| 内容安全域 | 所有域 | **开放主机服务** | 内容安全作为共享服务 |

### 9.2 插件化 AI 模型架构

**核心设计**: AI 模型调用通过策略模式 + SPI 实现插件化

```java
// 1. 定义 AI 模型接口 (扩展点)
public interface AiModelPlugin {
    String getModelName();                           // 模型标识
    ImageResult generate(ImageRequest request);      // 生成图片
    boolean isAvailable();                           // 模型是否可用
    double getCostPerGeneration();                   // 单次生成成本
}

// 2. 模型注册中心
@Component
public class AiModelRegistry {
    private final Map<String, AiModelPlugin> plugins = new ConcurrentHashMap<>();

    public void register(AiModelPlugin plugin) {
        plugins.put(plugin.getModelName(), plugin);
    }

    public AiModelPlugin get(String modelName) {
        return plugins.get(modelName);
    }

    public List<AiModelPlugin> getAll() {
        return List.copyOf(plugins.values());
    }
}

// 3. 通义万相插件 (MVP 默认)
@Component
public class TongyiWanxiangPlugin implements AiModelPlugin {
    @Override
    public String getModelName() { return "tongyi-wanxiang"; }

    @Override
    public ImageResult generate(ImageRequest request) {
        // 调用通义万相 API
        return tongyiClient.generate(request.getPrompt());
    }

    @Override
    public double getCostPerGeneration() { return 0.2; } // 0.2 元/张
}

// 4. DALL-E 插件 (未来扩展)
@Component
public class DallePlugin implements AiModelPlugin {
    @Override
    public String getModelName() { return "dall-e-3"; }

    @Override
    public ImageResult generate(ImageRequest request) {
        // 调用 DALL-E API
        return dalleClient.generate(request.getPrompt());
    }
}

// 5. Stable Diffusion 插件 (未来自建)
@Component
public class StableDiffusionPlugin implements AiModelPlugin {
    @Override
    public String getModelName() { return "stable-diffusion-xl"; }

    @Override
    public ImageResult generate(ImageRequest request) {
        // 调用本地 GPU 推理服务
        return sdClient.generate(request.getPrompt());
    }
}
```

**扩展新 AI 模型只需 3 步**:
1. 实现 `AiModelPlugin` 接口
2. 添加 `@Component` 注解（自动注册）
3. 在配置文件中指定模型权重

### 9.3 图片类型扩展架构

**当前**: 海报 + 缩略图  
**未来**: Banner、社交媒体配图、PPT 封面、电商主图、Logo、图标、视频、3D 模型...

```java
// 统一的内容类型抽象
public enum ContentType {
    POSTER("海报"),
    THUMBNAIL("缩略图"),
    BANNER("横幅"),
    SOCIAL_MEDIA("社交媒体配图"),
    PPT_COVER("PPT 封面"),
    ECOMMERCE_MAIN("电商主图"),
    LOGO("Logo"),
    ICON("图标"),
    VIDEO("视频"),          // 未来扩展
    TEXT_COPY("文案"),      // 未来扩展
    VOICE("语音合成"),      // 未来扩展
    MODEL_3D("3D 模型");    // 未来扩展

    private final String name;
    ContentType(String name) { this.name = name; }
}

// 每种内容类型有独立的 PromptTemplate
@Data
@Entity
public class ContentTemplate {
    @Id
    private Long id;
    private ContentType contentType;        // 内容类型
    private String style;                   // 风格: 简约/复古/赛博朋克...
    private String promptTemplate;          // 提示词模板
    private String aspectRatio;             // 宽高比: 16:9, 1:1, 9:16...
    private String outputFormat;            // 输出格式: png/jpeg/webp
    private double weight;                  // 推荐权重 (排序用)
    private Boolean enabled;                // 是否启用 (特性开关)
}
```

**扩展新图片类型只需**:
1. 在 `ContentType` 枚举中添加新值
2. 后台录入新模板的提示词和参数
3. 前端自动展示（基于枚举动态渲染）

### 9.4 商业模式扩展架构

#### 9.4.1 定价策略插件化

```java
// 定价策略接口
public interface PricingStrategy {
    PricingResult calculate(User user, GenerateRequest request);
    String getStrategyName();
}

// 按次计费 (MVP 默认)
@Component("pay-per-use")
public class PayPerUseStrategy implements PricingStrategy {
    @Override
    public PricingResult calculate(User user, GenerateRequest request) {
        return new PricingResult(
            user.getBalance(),           // 检查余额
            1,                            // 每次消耗 1 次
            PricingModel.PAY_PER_USE
        );
    }
}

// 订阅制 (未来扩展)
@Component("subscription")
public class SubscriptionStrategy implements PricingStrategy {
    @Override
    public PricingResult calculate(User user, GenerateRequest request) {
        if (user.getSubscriptionTier() == FREE) {
            return new PricingResult(0, 0, PricingModel.SUBSCRIPTION); // 免费额度
        }
        return new PricingResult(
            user.getSubscriptionQuota(), // 订阅额度
            0,                            // 订阅内免费
            PricingModel.SUBSCRIPTION
        );
    }
}

// 企业定制 (未来扩展)
@Component("enterprise")
public class EnterpriseStrategy implements PricingStrategy {
    @Override
    public PricingResult calculate(User user, GenerateRequest request) {
        // 企业客户按 API 调用量计费
        return enterpriseApiClient.billing(user.getEnterpriseId());
    }
}
```

#### 9.4.2 用户等级体系

| 等级 | 名称 | 月费 | 生成次数/月 | 优先级 | API 调用 |
|------|------|------|------------|--------|----------|
| 0 | 免费用户 | ¥0 | 10 次 | 低 | 否 |
| 1 | 个人版 | ¥19/月 | 200 次 | 中 | 否 |
| 2 | Pro 版 | ¥49/月 | 1000 次 | 高 | 否 |
| 3 | 企业版 | 定制 | 不限 | 最高 | 是 (API Key) |

### 9.5 特性开关 (Feature Toggle)

```java
// 特性开关管理器
@Service
public class FeatureToggleService {
    private final Map<String, Boolean> toggles = new ConcurrentHashMap<>();

    // 初始化: 从配置文件/数据库加载
    @PostConstruct
    public void init() {
        toggles.put("video-generation", false);     // 视频生成功能
        toggles.put("subscription-model", false);    // 订阅制
        toggles.put("enterprise-api", false);        // 企业 API
        toggles.put("template-marketplace", false);  // 模板市场
        toggles.put("social-share", true);           // 社交分享 (默认开启)
    }

    public boolean isEnabled(String feature) {
        return toggles.getOrDefault(feature, false);
    }

    public void setEnabled(String feature, boolean enabled) {
        toggles.put(feature, enabled);
    }
}
```

**使用示例**:
```java
@PostMapping("/generate")
public ResponseEntity<?> generateImage(@RequestBody GenerateRequest request) {
    if (!featureToggleService.isEnabled("video-generation")
        && request.getContentType() == ContentType.VIDEO) {
        return ResponseEntity.status(503)
            .body(ApiResponse.error("视频生成功能暂未开放"));
    }
    // ... 正常处理
}
```

### 9.6 API 版本管理

```
/api/v1/...    ← MVP 版本，稳定
/api/v2/...    ← 未来版本，新功能
```

| 策略 | 说明 |
|------|------|
| URL 版本化 | `/api/v1/images/generate` |
| 向后兼容 | v1 接口永不删除，只做废弃标记 |
| 废弃通知 | 响应头 `X-API-Deprecation: true` + `Sunset: 2027-01-01` |
| 灰度发布 | 通过特性开关控制新版本只对部分用户开放 |

---

## 10. 高可用与容灾

> **产出方**: 架构师·铁壁 (高可用专家)  
> **用户确认**: SLA = D (没有明确 SLA，但要求"尽可能高可用")

### 10.1 单点故障分析

| 组件 | 单点风险 | 影响 | 缓解方案 |
|------|---------|------|----------|
| MySQL 单节点 | **高** | 全部写入失败，服务不可用 | 主从复制 + 自动故障切换 |
| Redis 单节点 | **中** | 限流/缓存失效 | Redis Sentinel 哨兵模式 |
| OSS | **极低** | 阿里云/ AWS 服务本身 SLA 99.99% | 无需额外处理 |
| AI 模型 API | **中** | 图片生成失败 | 多模型 fallback + 降级 |
| 单个 Spring Boot 实例 | **低** | 容量减半 | Docker Compose 多实例 |
| 开发者本人 | **高** | 无人维护 | 自动化监控告警 + 文档 |

### 10.2 降级策略

**当 AI 模型 API 不可用时**:

```java
@Component
public class ImageGenerationFallback {

    @Autowired private AiModelRegistry modelRegistry;

    @Autowired private FeatureToggleService featureToggle;

    /**
     * 图片生成带降级
     */
    public ImageResult generateWithFallback(ImageRequest request) {
        // 优先级 1: 主模型
        try {
            AiModelPlugin primary = modelRegistry.get(request.getPreferredModel());
            if (primary != null && primary.isAvailable()) {
                return primary.generate(request);
            }
        } catch (Exception e) {
            log.warn("主模型 {} 生成失败，尝试降级", request.getPreferredModel(), e);
        }

        // 优先级 2: 备用模型
        try {
            AiModelPlugin backup = modelRegistry.get("tongyi-wanxiang-v2");
            if (backup != null && backup.isAvailable()) {
                log.info("切换到备用模型 tongyi-wanxiang-v2");
                return backup.generate(request);
            }
        } catch (Exception e) {
            log.warn("备用模型也失败", e);
        }

        // 优先级 3: 返回模板默认图 (极端降级)
        if (featureToggle.isEnabled("fallback-template-image")) {
            log.warn("AI 模型全部不可用，返回模板默认图");
            return ImageResult.fallback(request.getTemplateId());
        }

        // 优先级 4: 排队等待
        throw new ServiceUnavailableException(
            "AI 服务繁忙，请稍后重试 (排队中...)");
    }
}
```

**降级层级**:

| 层级 | 条件 | 行为 | 用户体验 |
|------|------|------|----------|
| L1 | AI API 正常 | 正常生成 | 无感知 |
| L2 | 主模型失败 | 切换到备用模型 | 延迟增加 1-2 秒 |
| L3 | 所有模型失败 | 返回模板默认图 | 显示占位图，提示"生成中..." |
| L4 | 极端情况 | 排队等待，返回 503 | 提示"服务繁忙，请稍后再试" |

### 10.3 熔断与重试

```java
// Spring Retry + Resilience4j 配置
resilience4j:
  circuitbreaker:
    instances:
      aiModelApi:
        slidingWindowSize: 10              # 最近 10 次调用
        failureRateThreshold: 50           # 失败率 > 50% 触发熔断
        waitDurationInOpenState: 30s       # 熔断 30 秒
        permittedNumberOfCallsInHalfOpenState: 3  # 半开状态允许 3 次测试调用
        recordExceptions:
          - java.net.SocketTimeoutException
          - java.io.IOException

  retry:
    instances:
      aiModelApi:
        maxAttempts: 3                     # 最多重试 3 次
        waitDuration: 1s                   # 首次重试间隔 1 秒
        retryExceptions:
          - java.net.SocketTimeoutException
```

### 10.4 备份与恢复

| 数据类型 | 备份策略 | 恢复 RTO | 恢复 RPO |
|----------|---------|----------|----------|
| MySQL 数据 | 每天全量 + binlog 增量 | < 1 小时 | < 5 分钟 |
| Redis 数据 | AOF 持久化 + RDB 快照 | < 5 分钟 | < 1 分钟 |
| OSS 图片 | OSS 自带冗余 (3 副本) | N/A | N/A |
| 配置文件 | Git 版本控制 | < 1 分钟 | 0 |

**备份自动化脚本**:
```bash
#!/bin/bash
# backup-daily.sh - 每日凌晨 2 点执行

# MySQL 全量备份
mysqldump -u root -p"${MYSQL_ROOT_PASSWORD}" \
    --single-transaction --routines --triggers \
    design_platform > "/backup/mysql/design_platform_$(date +%Y%m%d).sql"

# 压缩备份
gzip "/backup/mysql/design_platform_$(date +%Y%m%d).sql"

# 保留最近 30 天
find /backup/mysql -name "*.sql.gz" -mtime +30 -delete

# 同步到远程存储 (阿里云 OSS)
ossutil cp -r /backup/mysql/ oss://design-platform-backup/mysql/
```

### 10.5 监控告警 (单人开发必备)

**核心监控指标**:

| 指标 | 阈值 | 告警级别 | 通知方式 |
|------|------|----------|----------|
| CPU 使用率 | > 80% 持续 5 分钟 | 警告 | 邮件 |
| 内存使用率 | > 85% | 严重 | 邮件 + 短信 |
| MySQL 连接数 | > 80% 最大连接数 | 警告 | 邮件 |
| Redis 内存使用 | > 75% | 警告 | 邮件 |
| 图片生成成功率 | < 95% 持续 10 分钟 | 严重 | 邮件 + 短信 |
| API 响应时间 (P99) | > 5 秒 | 警告 | 邮件 |
| 磁盘使用率 | > 85% | 严重 | 邮件 + 短信 |
| 服务存活 | 进程退出 | 紧急 | 短信 |

**极简监控方案** (低成本):

```yaml
# docker-compose-monitoring.yml
services:
  prometheus:
    image: prom/prometheus:latest
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
      - prometheus-data:/prometheus
    ports:
      - "9090:9090"

  grafana:
    image: grafana/grafana:latest
    volumes:
      - grafana-data:/var/lib/grafana
    ports:
      - "3000:3000"
    depends_on:
      - prometheus

  node-exporter:
    image: prom/node-exporter:latest
    ports:
      - "9100:9100"

volumes:
  prometheus-data:
  grafana-data:
```

**Prometheus 配置**:
```yaml
# prometheus.yml
scrape_configs:
  - job_name: 'spring-boot'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['spring-boot:8080']

  - job_name: 'node'
    static_configs:
      - targets: ['node-exporter:9100']
```

### 10.6 混沌工程 (轻量级)

单人开发不需要复杂的混沌工程平台，用脚本即可:

```bash
#!/bin/bash
# chaos-test.sh - 每周执行一次

echo "=== 混沌测试:  killing MySQL ==="
docker kill mysql || true
sleep 5
docker start mysql
sleep 30

# 检查服务是否自动恢复
if curl -s http://localhost:8080/api/health | grep -q "UP"; then
    echo "✅ MySQL 恢复后服务正常"
else
    echo "❌ MySQL 恢复后服务异常!"
fi
```

---

## 11. 演进路线图

### Phase 0: MVP (Month 1) — 当前阶段

**目标**: 验证产品市场匹配，核心功能可用

- [x] 模块化单体架构
- [x] JWT 认证 + RBAC
- [x] 图片模板管理 (后台)
- [x] AI 图片生成 (异步线程池)
- [x] 次数扣减 + 幂等控制
- [x] Docker Compose 部署
- [x] OSS 图片存储
- [x] 基础限流 (Nginx + Redis)
- [x] 插件化 AI 模型接口 (策略模式)
- [x] 特性开关 (Feature Toggle)
- [x] Prometheus + Grafana 监控
- [x] 每日自动备份

### Phase 1: 性能优化 (Month 2-3)

**触发条件**: 日活 > 5w 或 QPS > 5000 持续 1 周

- [ ] Redis 缓存热点模板数据
- [ ] MySQL 读写分离
- [ ] CDN 加速图片分发
- [ ] 图片压缩和格式优化 (WebP)
- [ ] 接口响应时间监控 (APM)
- [ ] 多实例 Docker Compose (3 replicas)
- [ ] AI 模型 fallback 降级
- [ ] 自动备份脚本验证

### Phase 2: 架构拆分 (Month 4-6)

**触发条件**: 日活 > 10w 且 QPS > 10000 持续 1 月

- [ ] 提取图片生成为独立微服务
- [ ] 引入消息队列 (RabbitMQ/Kafka)
- [ ] 引入 Service Registry (Nacos)
- [ ] 引入 API Gateway (Spring Cloud Gateway)
- [ ] 引入配置中心
- [ ] 订阅制商业模式上线
- [ ] 视频生成插件 (纵向扩展)

### Phase 3: 云原生升级 (Month 7-12)

**触发条件**: 业务稳定增长，需要自动化运维

- [ ] 迁移到 Kubernetes (ACK/EKS)
- [ ] 引入 HPA 自动扩缩容
- [ ] 引入 Service Mesh (Istio)
- [ ] 引入 CI/CD Pipeline
- [ ] 引入分布式追踪 (Jaeger)
- [ ] API 开放平台 (商业扩展)
- [ ] 模板市场 (创作者经济)

### Phase 4: 大规模扩展 (Year 2+)

**触发条件**: 用户量 > 1亿

- [ ] 分库分表 (ShardingSphere)
- [ ] 事件溯源 + CQRS
- [ ] 多区域部署
- [ ] 自建 AI 模型 (GPU 集群)
- [ ] 数据仓库 + BI 分析
- [ ] 3D 模型生成 (纵向扩展)
- [ ] 语音合成 (纵向扩展)

---

## 9. AI 辅助开发指南

### 9.1 Token 预算分配 (1000 元)

| 用途 | 预算占比 | 金额 |
|------|---------|------|
| AI 编码辅助 | 60% | 600 元 |
| AI 代码审查 | 20% | 200 元 |
| AI 测试生成 | 15% | 150 元 |
| 应急缓冲 | 5% | 50 元 |

### 9.2 AI 提示词模板

**CRUD 代码生成**:
```
请用 Spring Boot + MyBatis-Plus 生成以下模块的完整 CRUD 代码：
- 模块名: [template/image/billing/auth]
- 实体: [字段列表]
- 要求: 包含 Controller/Service/Repository/Entity/DTO
- 规范: 统一响应格式、异常处理、参数校验
```

**单元测试生成**:
```
请为以下 Service 方法生成 JUnit 5 + Mockito 单元测试：
- 方法: [方法签名]
- 覆盖: 正常路径 + 边界条件 + 异常路径
- 要求: 测试覆盖率 > 80%
```

**安全审查**:
```
请审查以下 Spring Boot 代码的安全漏洞：
- 关注: SQL注入/XSS/CSRF/认证绕过/敏感数据泄露
- 输出: 漏洞等级 + 修复建议
```

### 9.3 开发效率技巧

1. **用 AI 生成样板代码**: Controller、Service、Entity、DTO 等重复代码
2. **用 AI 写 SQL**: 复杂查询、索引优化建议
3. **用 AI 调试**: 粘贴错误日志，让 AI 分析原因
4. **用 AI 写文档**: API 文档、部署文档、README
5. **你自己专注**: 核心业务逻辑、架构决策、代码审查

---

## 13. 附录：辩论纪要

### 13.1 8 位架构师阵容

| 编号 | 姓名 | 专长 | 核心立场 |
|------|------|------|----------|
| 1 | 雷恩 (Ryan) | 分布式高并发 | 微服务 + 事件驱动 |
| 2 | 陈默 (Chen Mo) | 数据一致性 | 强一致 + 事务优先 |
| 3 | Alex | 云原生 | K8s + Serverless |
| 4 | 林薇 (Lin Wei) | 安全架构 | 零信任 + 安全优先 |
| 5 | 老周 (Lao Zhou) | 全栈务实 | 单体优先 + 渐进式 |
| 6 | 张量 (Tensor) | 弹性伸缩 | 预测性扩容 + 容量规划 |
| 7 | 叶岚 (Ye Lan) | 业务扩展/DDD | 插件化 + 限界上下文 |
| 8 | 铁壁 (Ironwall) | 高可用/容灾 | 降级 + 熔断 + 混沌工程 |

### 13.2 主要争议点

| 争议点 | 雷恩 | 陈默 | Alex | 林薇 | 老周 | 张量 | 叶岚 | 铁壁 | 共识 |
|--------|------|------|------|------|------|------|------|------|------|
| 单体 vs 微服务 | 微服务 | 不重要 | K8s+单体 | 看情况 | **单体** | **单体+弹性** | **模块化单体** | **单体+容灾** | **模块化单体** |
| 消息队列 | **必须** | 可选 | KEDA | 不重要 | **不需要** | **MVP 线程池** | 不重要 | 不重要 | MVP 线程池 |
| 一致性 | 最终一致 | **强一致** | 不重要 | 不重要 | 本地事务 | 不重要 | 不重要 | 不重要 | **本地事务+幂等** |
| 分库分表 | **立即** | 预留键 | 托管方案 | 不重要 | **MVP 单表** | **MVP 单表** | 不重要 | 不重要 | **MVP 单表** |
| 部署 | 不重要 | 不重要 | **K8s** | 不重要 | **Docker Compose** | **Docker Compose** | 不重要 | **Docker Compose** | **Docker Compose** |
| 限流策略 | 令牌桶 | 不重要 | HPA | **WAF** | Nginx | **多级限流** | 不重要 | 不重要 | **Nginx+Redis** |
| AI 模型 | 不重要 | 不重要 | Serverless | 不重要 | 不重要 | **多模型** | **插件化** | **fallback** | **插件化+fallback** |
| 监控 | 不重要 | 不重要 | OpenTelemetry | 不重要 | 不重要 | **Prometheus** | 不重要 | **Prometheus** | **Prometheus+Grafana** |

### 10.2 辩论金句

> **老周**: "MVP 的核心是验证业务，不是验证架构。先活下来，再谈扩展。"

> **雷恩**: "10000 QPS 不是小数目。单体架构在 10000 QPS 下，连接池会打满，GC 停顿会拖垮所有请求。"

> **陈默**: "数据一致性不是性能的对立面，而是系统的基石。先做对，再做快。"

> **Alex**: "K8s 的 HPA 可以让单体应用自动扩到多个 Pod，既保留了单体开发的简单性，又获得了水平扩展的能力。"

> **林薇**: "不管单体还是微服务，如果认证授权没做好，10000 QPS 下很容易成为 DDoS 目标。安全是基础，不是功能。"

---

## 附录 A: 核心 API 设计

### A.1 认证相关

| 方法 | 路径 | 描述 | 权限 |
|------|------|------|------|
| POST | /api/auth/register | 用户注册 | 公开 |
| POST | /api/auth/login | 用户登录 | 公开 |
| POST | /api/auth/refresh | 刷新 Token | 需 Refresh Token |
| GET | /api/auth/profile | 获取用户信息 | 需登录 |

### A.2 图片模板

| 方法 | 路径 | 描述 | 权限 |
|------|------|------|------|
| GET | /api/templates | 获取模板列表 | 公开 |
| GET | /api/templates/{id} | 获取模板详情 | 公开 |
| GET | /api/categories | 获取分类列表 | 公开 |
| POST | /api/admin/templates | 创建模板 | 管理员 |
| PUT | /api/admin/templates/{id} | 更新模板 | 管理员 |
| DELETE | /api/admin/templates/{id} | 删除模板 | 管理员 |

### A.3 图片生成

| 方法 | 路径 | 描述 | 权限 |
|------|------|------|------|
| POST | /api/images/generate | 生成图片 | 需登录 + 有次数 |
| GET | /api/images/task/{orderId} | 查询任务状态 | 需登录 |
| GET | /api/images/my | 获取我的图片列表 | 需登录 |
| GET | /api/images/{id}/url | 获取图片 URL | 需登录 |

### A.4 账户管理

| 方法 | 路径 | 描述 | 权限 |
|------|------|------|------|
| GET | /api/account/balance | 查询余额 | 需登录 |
| GET | /api/account/transactions | 交易记录 | 需登录 |
| POST | /api/admin/accounts/adjust | 管理员调整次数 | 管理员 |

---

## 附录 B: 技术栈清单

### 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 21 | 运行时 |
| Spring Boot | 3.2+ | 框架 |
| Spring Security | 6.x | 安全 |
| MyBatis-Plus | 3.5+ | ORM |
| JWT (jjwt) | 0.12+ | 认证 |
| Lombok | 1.18+ | 代码简化 |
| JUnit 5 | 5.10+ | 测试 |
| Mockito | 5.x+ | 模拟 |

### 基础设施

| 技术 | 版本 | 用途 |
|------|------|------|
| MySQL | 8.0 | 数据库 |
| Redis | 7.x | 缓存/限流 |
| Nginx | 1.25+ | 反向代理 |
| Docker | 24+ | 容器化 |
| Docker Compose | 2.24+ | 编排 |
| 阿里云 OSS | - | 图片存储 |

### 前端

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4+ | 框架 |
| Vite | 5.x+ | 构建工具 |
| Element Plus | 2.5+ | UI 组件库 |
| Axios | 1.6+ | HTTP 客户端 |
| Pinia | 2.1+ | 状态管理 |

### AI 图片模型 (待定)

| 方案 | 成本/张 | 延迟 | 推荐场景 |
|------|--------|------|----------|
| 通义万相 | ~0.1-0.3元 | 5-15s | 国内用户，性价比高 |
| DALL-E 3 | ~0.04-0.08美元 | 10-30s | 高质量，海外用户 |
| Stable Diffusion (自建) | ~0.01元 (电费) | 3-10s | 量大后自建更划算 |

---

**文档结束**

> 本架构文档由 **8 位系统架构师辩论团队** 共同产出。
>
> | 架构师 | 贡献 |
> |--------|------|
> | 雷恩 (分布式) | 微服务架构、CQRS、事件驱动 |
> | 陈默 (一致性) | 事务管理、幂等控制、数据完整性 |
> | Alex (云原生) | K8s 部署、可观测性、自动扩缩容 |
> | 林薇 (安全) | 零信任、认证授权、合规 |
> | 老周 (务实) | MVP 策略、单体优先、AI 辅助 |
> | 张量 (扩容) | 动态扩容、连接池调优、缓存分层 |
> | 叶岚 (扩展) | DDD、插件化架构、特性开关 |
> | 铁壁 (高可用) | 降级策略、熔断重试、监控告警 |
>
> 最终方案是各方妥协和共识的结果。随着业务发展，请按照演进路线图逐步升级架构。
