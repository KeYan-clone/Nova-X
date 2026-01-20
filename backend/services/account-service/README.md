# Account Service

账户与权限服务

## 功能

- ✅ 用户注册
- ✅ 用户信息管理
- ✅ 实名认证
- ✅ 用户启用/禁用
- ✅ 分页查询用户列表
- 🔄 角色管理（待开发）
- 🔄 权限管理（待开发）
- 🔄 组织管理（待开发）
- 🔄 审计日志（待开发）

## API 列表

### 用户管理

```
POST   /api/v1/users/register        # 用户注册
GET    /api/v1/users/{userId}        # 获取用户信息
GET    /api/v1/users/phone/{phone}   # 根据手机号获取用户
PATCH  /api/v1/users/{userId}        # 更新用户信息
POST   /api/v1/users/{userId}/verify # 实名认证
GET    /api/v1/users                 # 分页查询用户列表
POST   /api/v1/users/{userId}/disable # 禁用用户
POST   /api/v1/users/{userId}/enable  # 启用用户
```

## 数据库表

### user - 用户表

```sql
CREATE TABLE `user` (
  `id` BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
  `username` VARCHAR(50) COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（加密）',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像',
  `gender` TINYINT DEFAULT 0 COMMENT '性别（0-未知，1-男，2-女）',
  `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
  `verified` TINYINT DEFAULT 0 COMMENT '实名认证状态（0-未认证，1-已认证）',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `id_card` VARCHAR(20) COMMENT '身份证号',
  `user_type` TINYINT DEFAULT 1 COMMENT '用户类型（1-普通用户，2-运营商，3-OEM，4-电力供应商，5-管理员）',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NOT NULL COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
  `version` INT DEFAULT 0 COMMENT '版本号（乐观锁）',
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_username` (`username`),
  KEY `idx_email` (`email`),
  KEY `idx_status` (`status`),
  KEY `idx_user_type` (`user_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

## 配置

### application.yml

- 服务端口：8081
- 数据库：MySQL (nova_x_account)
- Redis：localhost:6379
- Nacos：localhost:8848

## 运行

```bash
# 编译
mvn clean package

# 运行
java -jar target/account-service-1.0.0-SNAPSHOT.jar

# 或使用 Maven
mvn spring-boot:run
```

## 测试

```bash
# 注册用户
curl -X POST http://localhost:8081/api/v1/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "13800138000",
    "password": "Test1234",
    "verifyCode": "123456",
    "nickname": "测试用户"
  }'

# 获取用户信息
curl -X GET http://localhost:8081/api/v1/users/1

# 分页查询
curl -X GET "http://localhost:8081/api/v1/users?page=1&pageSize=10"
```

## 依赖服务

- MySQL
- Redis
- Nacos
