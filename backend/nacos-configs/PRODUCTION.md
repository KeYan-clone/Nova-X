# Nacos 配置示例 - 生产环境

## 生产环境配置要点

### 1. 修改数据库配置

```yaml
datasource:
  host: prod-mysql.example.com  # 生产环境 MySQL 地址
  port: 3306
  username: prod_user
  password: "你的强密码"  # 建议使用 Nacos 加密功能
```

### 2. 修改 Redis 配置

```yaml
redis:
  host: prod-redis.example.com  # 生产环境 Redis 地址
  port: 6379
  password: "你的Redis密码"
  database: 0
  timeout: 5000
  lettuce:
    pool:
      max-active: 50  # 生产环境增加连接数
      max-wait: -1
      max-idle: 20
      min-idle: 10
```

### 3. 修改 Kafka 配置

```yaml
kafka:
  bootstrap-servers: prod-kafka1.example.com:9092,prod-kafka2.example.com:9092,prod-kafka3.example.com:9092
  consumer:
    group-id: nova-x-prod-consumer
    auto-offset-reset: latest  # 生产环境建议使用 latest
    enable-auto-commit: false  # 手动提交，更可靠
  producer:
    acks: all  # 所有副本确认
    retries: 5  # 增加重试次数
    batch-size: 16384
    linger-ms: 10
```

### 4. 修改 Elasticsearch 配置

```yaml
elasticsearch:
  uris: http://prod-es1.example.com:9200,http://prod-es2.example.com:9200,http://prod-es3.example.com:9200
  username: elastic
  password: "你的ES密码"
  connection-timeout: 10s
  socket-timeout: 30s
```

### 5. 安全配置

```yaml
security:
  jwt:
    secret: "你的256位强密钥-请务必修改"  # 至少32个字符
    expiration: 7200  # 生产环境缩短有效期到2小时
    refresh-expiration: 259200  # 3天
```

### 6. 日志级别

```yaml
logging:
  level:
    com.novax: INFO  # 生产环境使用 INFO 级别
    org.springframework: WARN
    com.alibaba.nacos: ERROR
```

## 不同环境的配置隔离

### 开发环境 (dev)
- Namespace: `dev`
- 使用本地 Docker Compose 的服务
- 日志级别: DEBUG

### 测试环境 (test)
- Namespace: `test`
- 使用测试服务器
- 日志级别: DEBUG

### 预发布环境 (pre-prod)
- Namespace: `pre-prod`
- 使用生产同等配置
- 日志级别: INFO

### 生产环境 (prod)
- Namespace: `prod`
- 使用生产服务器集群
- 日志级别: WARN
- 启用监控告警

## 配置加密

对于敏感信息，建议使用 Nacos 的配置加密功能：

```yaml
# 明文（不推荐）
datasource:
  password: mypassword

# 密文（推荐）
datasource:
  password: cipher(AQIDBAUGBwgJCgsMDQ4PEA==)
```

在 Nacos 控制台的配置加密功能中生成密文。

## 动态配置刷新

以下配置支持动态刷新（无需重启服务）：

- ✅ 日志级别
- ✅ 开关类配置
- ✅ 限流阈值
- ✅ JWT 过期时间
- ❌ 数据源配置（需要重启）
- ❌ 端口配置（需要重启）

## 配置优先级

```
环境变量 > Nacos 配置 > application.yml
```

可以通过环境变量覆盖 Nacos 配置：

```bash
export DATASOURCE_HOST=prod-mysql.example.com
export REDIS_HOST=prod-redis.example.com
java -jar account-service.jar
```
