# Gateway Service - API网关服务

## 服务简介

API网关是Nova-X充电桩管理平台的统一入口，负责请求路由、认证鉴权、限流熔断、日志记录等功能。

## 核心功能

### 1. 路由管理
- **动态路由**: 基于Nacos服务发现的动态路由
- **负载均衡**: 集成Spring Cloud LoadBalancer
- **路径重写**: 支持路径前缀剥离
- **15+服务路由**: 覆盖所有微服务

### 2. 安全认证
- **JWT验证**: 统一的Token验证
- **白名单机制**: 登录、注册等接口免认证
- **用户信息传递**: 将用户ID、用户名等信息传递给下游服务
- **多种Token获取方式**: 支持Header和URL参数

### 3. 限流熔断
- **Sentinel集成**: 阿里巴巴Sentinel限流框架
- **网关级限流**: QPS限流、并发线程数限流
- **熔断降级**: 服务不可用时自动熔断
- **自定义响应**: 限流后返回友好提示

### 4. 跨域处理
- **全局CORS配置**: 支持所有域名、方法、请求头
- **预检请求优化**: MaxAge设置为3600秒
- **响应头暴露**: 暴露TraceId、Authorization等自定义头

### 5. 链路追踪
- **TraceId生成**: 为每个请求生成唯一TraceId
- **RequestId生成**: 区分同一用户的不同请求
- **链路传递**: TraceId在整个调用链中传递
- **响应头返回**: 将TraceId返回给客户端

### 6. 日志记录
- **请求日志**: 记录请求方法、路径、参数、IP
- **响应日志**: 记录响应状态、耗时
- **TraceId关联**: 所有日志包含TraceId
- **客户端IP识别**: 支持代理环境的真实IP获取

### 7. 异常处理
- **全局异常处理**: 统一的异常响应格式
- **服务未找到**: 404友好提示
- **内部错误**: 500错误处理
- **异常日志**: 完整的异常堆栈记录

## 技术架构

### 技术栈
- **Spring Cloud Gateway**: 响应式网关框架
- **Spring Cloud LoadBalancer**: 客户端负载均衡
- **Alibaba Sentinel**: 限流熔断
- **Nacos Discovery**: 服务发现
- **Redis**: 分布式缓存
- **WebFlux**: 响应式Web框架

### 架构特点
- **响应式设计**: 基于WebFlux，支持高并发
- **非阻塞IO**: Netty服务器，性能优异
- **过滤器链**: 灵活的Filter机制
- **动态路由**: 支持运行时动态修改路由

## 路由配置

### 路由规则

#### 1. 认证服务 (auth-service)
```yaml
- Path=/api/v1/auth/**
- 登录、注册、验证码等
```

#### 2. 账户服务 (account-service)
```yaml
- Path=/api/v1/users/**
- 用户管理、角色权限等
```

#### 3. 站点服务 (station-service)
```yaml
- Path=/api/v1/stations/**
- 充电站管理
```

#### 4. 设备服务 (device-service)
```yaml
- Path=/api/v1/devices/**,/api/v1/connectors/**
- 充电桩、充电枪管理
```

#### 5. 会话服务 (session-service)
```yaml
- Path=/api/v1/sessions/**,/api/v1/charging/**
- 充电会话、充电过程管理
```

#### 6. 计费服务 (billing-service)
```yaml
- Path=/api/v1/billing/**
- 账单生成、费用计算
```

#### 7. 支付服务 (payment-service)
```yaml
- Path=/api/v1/payments/**
- 支付、退款、对账
```

#### 8. 定价服务 (pricing-service)
```yaml
- Path=/api/v1/pricing/**
- 定价策略、分时电价
```

#### 9. 会员服务 (member-service)
```yaml
- Path=/api/v1/members/**,/api/v1/vip/**
- 会员管理、积分、权益
```

#### 10. 工单服务 (work-order-service)
```yaml
- Path=/api/v1/work-orders/**
- 工单管理、派单、处理
```

#### 11. 通知服务 (notification-service)
```yaml
- Path=/api/v1/notifications/**,/api/v1/messages/**
- 消息推送、短信、邮件
```

#### 12. 调度服务 (scheduling-service)
```yaml
- Path=/api/v1/scheduling/**
- 充电调度、预约管理
```

#### 13. DR/VPP服务 (dr-vpp-service)
```yaml
- Path=/api/v1/dr/**,/api/v1/vpp/**
- 需求响应、虚拟电厂
```

#### 14. 报表服务 (report-service)
```yaml
- Path=/api/v1/reports/**
- 报表生成、数据统计
```

#### 15. 清结算服务 (settlement-service)
```yaml
- Path=/api/v1/settlements/**
- 清分结算、对账
```

## 过滤器链

### 执行顺序
1. **TraceIdFilter** (优先级最高)
   - 生成TraceId和RequestId
   - 添加到请求头和响应头

2. **JwtAuthenticationFilter**
   - 验证JWT Token
   - 白名单路径放行
   - 提取用户信息并传递给下游

3. **业务过滤器** (Spring Cloud Gateway内置)
   - 路由匹配
   - 负载均衡
   - 请求转发

4. **RequestLogFilter** (优先级最低)
   - 记录请求和响应日志
   - 计算请求耗时

### 白名单配置
```java
/api/v1/auth/login        # 登录
/api/v1/auth/register     # 注册
/api/v1/auth/captcha      # 验证码
/api/v1/auth/sms-code     # 短信验证码
/doc.html                 # API文档
/v3/api-docs              # OpenAPI文档
/swagger-ui               # Swagger UI
/webjars                  # 静态资源
/favicon.ico              # 图标
```

## API调用示例

### 1. 通过网关访问认证服务
```bash
# 密码登录
curl -X POST http://localhost:9000/api/v1/auth/login/password \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456"
  }'

# 响应包含TraceId
{
  "code": 200,
  "message": "success",
  "data": {
    "accessToken": "eyJhbGc...",
    "userId": 1
  }
}
```

### 2. 携带Token访问业务服务
```bash
# 查询用户信息
curl http://localhost:9000/api/v1/users/1 \
  -H "Authorization: Bearer eyJhbGc..."

# 网关会验证Token，并将用户信息传递给account-service
```

### 3. 查看响应头
```bash
curl -i http://localhost:9000/api/v1/auth/captcha

# 响应头包含
X-Trace-Id: 550e8400e29b41d4a716446655440000
X-Request-Id: 6ba7b810-9dad-11d1-80b4-00c04fd430c8
```

## 限流配置

### Sentinel Dashboard
- **控制台地址**: http://localhost:8858
- **应用名**: gateway-service
- **限流规则**: 通过控制台配置

### 限流示例
```yaml
# QPS限流：每秒最多100个请求
- resource: /api/v1/users/**
  count: 100
  grade: QPS

# 并发线程数限流：最多50个线程
- resource: /api/v1/sessions/**
  count: 50
  grade: THREAD
```

### 限流响应
```json
{
  "code": 429,
  "message": "请求过于频繁，请稍后重试",
  "data": null
}
```

## 监控指标

### 业务指标
- 请求总量
- 请求成功率
- 平均响应时间
- Token验证失败率

### 性能指标
- QPS (每秒请求数)
- 响应时间分布 (P50, P95, P99)
- 限流触发次数
- 熔断触发次数

### 系统指标
- CPU使用率
- 内存使用率
- 网络流量
- 连接数

## 部署运行

### 环境要求
- JDK 21+
- Redis 7.0+
- Nacos 2.3+
- Sentinel Dashboard (可选)

### 启动步骤
```bash
# 1. 启动Redis
redis-server

# 2. 启动Nacos
sh nacos/bin/startup.sh -m standalone

# 3. 启动Sentinel Dashboard (可选)
java -jar sentinel-dashboard.jar

# 4. 启动网关服务
cd infrastructure/gateway-service
mvn spring-boot:run
```

### 验证
```bash
# 健康检查
curl http://localhost:9000/actuator/health

# 路由信息
curl http://localhost:9000/actuator/gateway/routes
```

## 性能优化

### 1. 连接池优化
```yaml
spring:
  cloud:
    gateway:
      httpclient:
        pool:
          max-connections: 500
          max-idle-time: 30s
```

### 2. 超时配置
```yaml
spring:
  cloud:
    gateway:
      httpclient:
        connect-timeout: 3000
        response-timeout: 30s
```

### 3. 缓存优化
- Token验证结果缓存
- 路由配置缓存
- 限流规则缓存

## 安全机制

### 1. Token验证
- 所有非白名单请求必须携带Token
- Token过期自动拒绝
- Token黑名单机制

### 2. 请求签名 (预留)
- 防篡改
- 防重放攻击
- 时间戳验证

### 3. IP限流
- 防止恶意攻击
- 保护后端服务
- 可配置白名单

## 后续优化

1. **动态路由**: 支持通过Nacos配置中心动态修改路由
2. **灰度发布**: 支持按版本、用户分流
3. **请求重试**: 失败请求自动重试
4. **缓存预热**: 路由配置预加载
5. **监控告警**: 集成Prometheus和Grafana

---

**服务端口**: 9000
**服务名**: gateway-service
**负责人**: Nova-X Team
**更新时间**: 2026-01-20
