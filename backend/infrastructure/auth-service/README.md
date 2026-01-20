# Auth Service - 认证授权服务

## 服务简介

认证授权服务是Nova-X充电桩管理平台的基础设施服务，负责用户身份认证、令牌管理、权限验证等核心功能。

## 核心功能

### 1. 用户登录
- **密码登录**: 支持用户名/手机号/邮箱 + 密码登录
- **短信登录**: 支持手机号 + 短信验证码登录
- **OAuth2登录**: 支持第三方平台登录（预留）
- **图形验证码**: 登录时验证，防止暴力破解
- **记住我**: 延长登录有效期

### 2. 令牌管理
- **JWT Token**: 基于JWT的无状态令牌
- **访问令牌**: 用于API访问，有效期2小时
- **刷新令牌**: 用于刷新访问令牌，有效期7天
- **令牌刷新**: 自动刷新机制，提升用户体验
- **令牌撤销**: 退出登录时撤销令牌

### 3. 验证码管理
- **图形验证码**: 基于Hutool的CircleCaptcha
- **短信验证码**: 6位随机数字，有效期5分钟
- **验证码防刷**: Redis限流，防止频繁发送

### 4. 登录日志
- **登录记录**: 记录每次登录尝试
- **失败追踪**: 记录失败原因
- **设备信息**: 记录IP、浏览器、操作系统
- **安全审计**: 异常登录检测

## 技术架构

### 技术栈
- **Spring Boot 3.5.0**: 基础框架
- **Spring Security**: 安全框架
- **JWT (jjwt 0.12.6)**: 令牌生成与验证
- **Redis**: 令牌存储与验证码缓存
- **MyBatis Plus**: 数据库访问
- **Hutool**: 验证码生成
- **Nacos**: 服务注册与配置管理

### 架构特点
- **无状态设计**: 基于JWT，支持水平扩展
- **双令牌机制**: 访问令牌 + 刷新令牌，平衡安全与体验
- **Redis存储**: 令牌黑名单、验证码缓存
- **分布式会话**: 支持多节点部署

## API接口

### 认证接口

#### 1. 密码登录
```http
POST /api/v1/auth/login/password
Content-Type: application/json

{
  "username": "admin",
  "password": "123456",
  "captcha": "1234",
  "captchaKey": "uuid-xxxx",
  "rememberMe": false
}
```

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 7200,
    "userId": 1,
    "username": "admin",
    "realName": "管理员",
    "phone": "138****8000",
    "userType": "ADMIN"
  }
}
```

#### 2. 短信登录
```http
POST /api/v1/auth/login/sms
Content-Type: application/json

{
  "phone": "13800138000",
  "smsCode": "123456",
  "rememberMe": false
}
```

#### 3. 刷新令牌
```http
POST /api/v1/auth/refresh
Refresh-Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 4. 退出登录
```http
POST /api/v1/auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 5. 获取图形验证码
```http
GET /api/v1/auth/captcha
```

**响应**:
```json
{
  "code": 200,
  "data": {
    "captchaKey": "uuid-xxxx",
    "captchaImage": "data:image/png;base64,iVBORw0KG...",
    "expiresIn": 300
  }
}
```

#### 6. 发送短信验证码
```http
POST /api/v1/auth/sms-code?phone=13800138000
```

#### 7. 验证Token
```http
GET /api/v1/auth/validate
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 数据库设计

### login_log - 登录日志表
| 字段           | 类型         | 说明     |
| -------------- | ------------ | -------- |
| id             | BIGINT       | 主键     |
| user_id        | BIGINT       | 用户ID   |
| username       | VARCHAR(64)  | 用户名   |
| login_type     | VARCHAR(20)  | 登录类型 |
| login_ip       | VARCHAR(50)  | 登录IP   |
| login_location | VARCHAR(100) | 登录地点 |
| browser        | VARCHAR(50)  | 浏览器   |
| os             | VARCHAR(50)  | 操作系统 |
| login_status   | VARCHAR(20)  | 登录状态 |
| failure_reason | VARCHAR(200) | 失败原因 |
| login_time     | DATETIME     | 登录时间 |

### oauth_client - OAuth2客户端表
| 字段                  | 类型         | 说明        |
| --------------------- | ------------ | ----------- |
| id                    | BIGINT       | 主键        |
| client_id             | VARCHAR(100) | 客户端ID    |
| client_secret         | VARCHAR(200) | 客户端密钥  |
| grant_types           | VARCHAR(200) | 授权类型    |
| redirect_uris         | TEXT         | 回调地址    |
| access_token_validity | INT          | Token有效期 |

## 部署运行

### 环境要求
- JDK 21+
- MySQL 8.0+
- Redis 7.0+
- Nacos 2.3+

### 数据库初始化
```bash
mysql -u root -p < scripts/sql/auth-service-init.sql
```

### 配置修改
修改 `application.yml` 中的配置：
- 数据库连接信息
- Redis连接信息
- Nacos服务地址
- JWT密钥

### 启动服务
```bash
cd backend/infrastructure/auth-service
mvn spring-boot:run
```

### 访问文档
- Swagger文档: http://localhost:8080/doc.html
- OpenAPI文档: http://localhost:8080/v3/api-docs

## 安全机制

### 1. 密码安全
- **BCrypt加密**: 密码采用BCrypt加密存储
- **盐值**: 每个密码独立盐值
- **强度验证**: 密码强度要求

### 2. Token安全
- **JWT签名**: HMAC SHA256签名
- **过期机制**: 访问令牌2小时，刷新令牌7天
- **黑名单**: 退出登录后加入黑名单
- **单点登录**: 支持多设备互踢

### 3. 防护机制
- **验证码**: 防止暴力破解
- **限流**: 防止频繁请求
- **IP白名单**: 敏感接口IP限制
- **审计日志**: 完整的操作记录

## 监控指标

### 业务指标
- 登录成功率
- 登录失败率
- Token刷新次数
- 验证码发送量

### 性能指标
- 登录响应时间
- Token验证耗时
- Redis缓存命中率

## 后续优化

1. **多因素认证(MFA)**: 支持TOTP、短信等二次验证
2. **OAuth2完善**: 支持授权码模式
3. **单点登录(SSO)**: 跨系统单点登录
4. **生物识别**: 指纹、人脸识别登录
5. **风控系统**: 异常登录检测与阻断

---

**服务端口**: 8080
**服务名**: auth-service
**负责人**: Nova-X Team
**更新时间**: 2026-01-20
