# Nova-X 充电桩管理平台 🚗⚡

> 新能源汽车充电桩智能管理系统 - 基于Spring Cloud微服务架构

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.0-blue.svg)](https://spring.io/projects/spring-cloud)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Build](https://img.shields.io/badge/build-passing-success.svg)](backend)

## 📋 项目简介

Nova-X (星云) 是一个企业级充电桩管理平台，通过数字化手段连接充电桩、储能电站等新能源资产，实现能源在生产、存储与消耗环节的高效调度。为充电桩运营商、车主、设备厂商和电力供应商提供全方位的充电服务管理解决方案。

### 🎯 核心价值
- **能源互联** - 打破能源孤岛，实现电网负荷平衡
- **智能调度** - 构建可自我修复、智能预测的全球补能网络
- **多端协同** - 支持 Web、小程序、APP 三端一致的用户体验
- **差异化服务** - 为 C端用户、运营商、OEM、电力供应商提供专属服务

### ✨ 技术特色
- **微服务架构** - 18+独立服务，基于 Spring Boot 3.5.0 + Spring Cloud 2025.0.0
- **高可用** - SLA 99.99%，支持异地多活部署
- **高性能** - 核心链路 ≥ 50,000 TPS，IoT 指令延迟 ≤ 500ms
- **安全合规** - JWT认证、TLS 1.3、OAuth2、RBAC权限控制
- **地理搜索** - Haversine公式实现附近站点精准搜索
- **分时电价** - 智能分时定价，支持尖峰平谷策略
- **统一网关** - Spring Cloud Gateway集中认证和流量管理
- **服务治理** - Nacos服务发现、Sentinel限流熔断
- **分布式追踪** - TraceId全链路追踪，问题快速定位

## 🏗️ 系统架构

```
┌──────────────────────────────────────────────────────────────────┐
│                         Frontend Layer                            │
│  (用户端 | 运营商端 | 管理后台 | OEM端 | 电力供应商端)             │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│                      API Gateway (9000) ✅                        │
│  JWT认证 | 路由转发 | 限流熔断 | 链路追踪 | 跨域处理              │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌────────────────────┬────────────────────┬─────────────────────────┐
│  Infrastructure    │  Business Layer    │   Data Layer            │
├────────────────────┼────────────────────┼─────────────────────────┤
│ Auth Service ✅    │ Account Service ✅ │ Algorithm Service ⏳    │
│ Monitor Service ⏳ │ Station Service ✅ │ Search Service ⏳       │
│ Log Service ⏳     │ Device Service ✅  │ Data Sync Service ⏳    │
│                    │ Pricing Service ✅ │                         │
│                    │ Session Service ✅ │                         │
│                    │ Billing Service ⏳ │                         │
│                    │ Payment Service ⏳ │                         │
│                    │ Member Service ⏳  │                         │
│                    │ Notification ⏳    │                         │
│                    │ Work Order ⏳      │                         │
│                    │ Scheduling ⏳      │                         │
│                    │ DR/VPP Service ⏳  │                         │
│                    │ Report Service ⏳  │                         │
│                    │ Settlement ⏳      │                         │
│                    │ IoT Gateway ⏳     │                         │
└────────────────────┴────────────────────┴─────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│              Common Modules (公共模块层) ✅                        │
│  Core | Security | MyBatis | Redis | Kafka | Log | Swagger       │
└──────────────────────────────────────────────────────────────────┘
                              ↓
┌──────────────────────────────────────────────────────────────────┐
│                   Infrastructure                                  │
│  MySQL 8.0 | Redis 7.0 | Kafka 3.9 | Nacos 2.3 | Sentinel 1.8   │
└──────────────────────────────────────────────────────────────────┘
```

**图例**: ✅ 已完成 | ⏳ 规划中

## 🚀 技术栈

### 后端技术
| 技术                 | 版本       | 说明              |
| -------------------- | ---------- | ----------------- |
| Spring Boot          | 3.5.0      | 应用框架          |
| Spring Cloud         | 2025.0.0   | 微服务框架        |
| Spring Cloud Alibaba | 2023.0.3.2 | 阿里微服务组件    |
| MyBatis Plus         | 3.5.9      | ORM框架           |
| MySQL                | 8.0+       | 关系数据库        |
| Redis                | 7.0+       | 缓存数据库        |
| Kafka                | 3.9.0      | 消息队列          |
| Nacos                | 2.3+       | 服务注册/配置中心 |
| Sentinel             | 1.8+       | 限流熔断          |
| JWT                  | -          | 认证授权          |
| Swagger/Knife4j      | 4.5.0      | API文档           |

### 前端技术 (规划中)
- Vue 3.x / React 18+
- TypeScript
- Element Plus / Ant Design
- ECharts (数据可视化)

## 📖 项目文档

### 设计文档
- 📖 [项目背景书](docs/项目背景书.md) - 行业痛点、业务生态、核心挑战
- 📋 [需求描述文档](docs/需求描述文档.md) - 三端功能需求、非功能需求
- 🏗️ [架构设计文档](docs/架构设计文档.md) - 整体架构、模块设计、技术选型
- 💾 [数据设计文档](docs/数据设计文档.md) - 分片策略、缓存设计、数据生命周期
- 🔌 [接口设计文档](docs/接口设计文档.md) - API规范、鉴权、实时通道
- 🎯 [服务职责边界说明](docs/服务职责边界说明.md) - 服务职责划分

### 开发文档
- [后端开发指南](backend/README.md) - 后端开发详细说明
- [项目完成报告](backend/PROJECT_COMPLETION.md) - 项目完成总结
- [最终完成报告](backend/FINAL_COMPLETION_REPORT.md) - 详细完成报告
- [开发进度](backend/PROGRESS.md) - 实时开发进度

### API文档
- 网关聚合文档: http://localhost:9000/doc.html
- 各服务独立文档: http://localhost:{port}/doc.html

## 项目结构

```
Nova-X/
├── docs/                           # 📚 项目文档
│   ├── 项目背景书.md
│   ├── 需求描述文档.md
│   ├── 架构设计文档.md
│   ├── 数据设计文档.md
│   └── 接口设计文档.md
│
├── backend/                        # 🔧 后端服务（Spring Boot 微服务）
│   ├── README.md                   # 后端详细说明
│   ├── pom.xml                     # Maven 父配置
│   ├── docker-compose.yml          # 本地开发环境
│   │
│   ├── common/                     # 公共模块 ✅
│   │   ├── common-core            # 核心工具
│   │   ├── common-security        # 安全模块
│   │   ├── common-mybatis         # 持久层
│   │   ├── common-redis           # 缓存
│   │   ├── common-kafka           # 消息队列
│   │   ├── common-log             # 日志
│   │   └── common-swagger         # API文档
│   │
│   ├── infrastructure/             # 基础设施 ✅
│   │   ├── auth-service           # 认证服务 (8080)
│   │   └── gateway-service        # 网关服务 (9000)
│   │
│   ├── services/                   # 业务服务
│   │   ├── account-service        # 账户服务 (8081) ✅
│   │   ├── station-service        # 站点服务 (8082) ✅
│   │   ├── device-service         # 设备服务 (8083) ✅
│   │   ├── pricing-service        # 定价服务 (8084) ✅
│   │   ├── session-service        # 会话服务 (8085) ✅
│   │   ├── billing-service        # 计费服务 (8086) ⏳
│   │   ├── payment-service        # 支付服务 (8087) ⏳
│   │   └── ...                    # 更多服务
│   │
│   └── scripts/                    # 脚本文件
│       └── sql/                    # 数据库脚本
│
├── frontend/                       # 🎨 前端（规划中）
│   ├── web-consumer/               # C端用户 Web
│   ├── web-operator/               # 运营商后台
│   ├── web-admin/                  # 平台管理后台
│   ├── miniprogram/                # 小程序（微信/支付宝/抖音）
│   └── mobile-app/                 # APP（iOS/Android）
│
├── iot/                            # 📡 IoT 相关（待创建）
│   ├── protocol-adapters/          # 协议适配器（OCPP/GB/T）
│   ├── edge-services/              # 边缘服务
│   └── device-simulators/          # 设备模拟器
│
├── deployment/                     # 🚀 部署配置（待创建）
│   ├── kubernetes/                 # K8s 配置
│   ├── terraform/                  # 基础设施即代码
│   └── ansible/                    # 自动化运维
│
└── scripts/                        # 🛠️ 工具脚本
    ├── db/                         # 数据库脚本
    └── deploy/                     # 部署脚本
```

## 📊 项目进度

### ✅ 已完成 (Phase 1)

**基础设施层** (2/2)
- ✅ **auth-service** - 认证授权服务
  - 密码登录 + 短信登录
  - JWT双Token机制
  - 图形验证码 + 短信验证码
  - 登录日志审计

- ✅ **gateway-service** - API网关服务
  - 15个微服务路由配置
  - JWT认证过滤器
  - 分布式链路追踪(TraceId)
  - Sentinel限流熔断
  - 全局跨域CORS
  - 统一异常处理

**公共模块层** (7/7)
- ✅ common-core - 统一响应、异常处理、工具类
- ✅ common-security - JWT、加密解密、签名验证
- ✅ common-mybatis - MyBatis Plus配置、BaseEntity
- ✅ common-redis - Redis工具、分布式锁
- ✅ common-kafka - Kafka生产者
- ✅ common-log - 日志切面、TraceId
- ✅ common-swagger - OpenAPI 3.0、Knife4j

**核心业务服务** (5/16 - 核心完成)
- ✅ **account-service** - 账户管理服务
  - 用户注册/查询/更新
  - 实名认证管理
  - 角色权限管理

- ✅ **station-service** - 充电站管理服务
  - 充电站CRUD
  - **地理位置搜索** (Haversine公式)
  - 附近站点查询
  - 站点状态管理

- ✅ **device-service** - 设备管理服务
  - 充电桩管理 (25+字段)
  - 充电枪管理 (支持多枪)
  - 设备状态监控 (5种状态)
  - 设备上线/离线管理

- ✅ **pricing-service** - 定价服务
  - 定价模板管理
  - **分时电价策略** (尖峰/平/谷)
  - 电费计算引擎

- ✅ **session-service** - 充电会话服务
  - 会话管理 (启动/停止)
  - 充电数据记录
  - 会话状态跟踪

### ⏳ 规划中 (Phase 2)
- ⏳ billing-service - 计费服务
- ⏳ payment-service - 支付服务
- ⏳ member-service - 会员服务
- ⏳ notification-service - 通知服务
- ⏳ work-order-service - 工单服务
- ⏳ scheduling-service - 调度服务
- ⏳ 其他业务服务...

### 📈 统计数据
| 指标       | 数量     | 状态          |
| ---------- | -------- | ------------- |
| 微服务总数 | 18个规划 | 7个核心已完成 |
| Java类     | 100+     | ✅             |
| 代码行数   | 10,000+  | ✅             |
| REST接口   | 50+      | ✅             |
| 数据库表   | 20+      | ✅             |
| SQL脚本    | 10+      | ✅             |

## 🔥 核心特性详解

### 1. 地理位置搜索 🌍
使用Haversine公式在MySQL中直接计算地理距离，性能优异：
```sql
SELECT *,
  (6371 * ACOS(
    COS(RADIANS(lat)) * COS(RADIANS(latitude))
    * COS(RADIANS(longitude) - RADIANS(lng))
    + SIN(RADIANS(lat)) * SIN(RADIANS(latitude))
  )) AS distance
FROM station
HAVING distance <= radius
ORDER BY distance
```

### 2. 分时电价策略 💰
智能分时定价，自动根据时段计算电价：
- **尖峰时段** (10:00-15:00, 18:00-21:00): 电价 × 1.5
- **平时段** (07:00-10:00, 15:00-18:00, 21:00-23:00): 电价 × 1.0
- **谷时段** (23:00-07:00): 电价 × 0.6

### 3. 设备状态管理 🔋
完整的设备状态流转机制：
```
充电桩状态: OFFLINE → IDLE → CHARGING ⇄ MAINTENANCE
                                 ↓
                              FAULT

充电枪状态: IDLE → RESERVED → CHARGING → IDLE
              ↓                 ↓
         UNAVAILABLE ←――― FAULT
```

### 4. 统一网关认证 🔐
所有请求通过网关统一认证，支持：
- JWT Token验证
- 白名单机制
- 自动刷新Token
- TraceId链路追踪
- 限流熔断保护

### 后端（Java 微服务）
- **开发语言** - Java 21
- **核心框架** - Spring Boot 3.5.x, Spring Cloud 2025.x
- **服务注册** - Nacos Discovery
- **配置中心** - Nacos Config
- **API 网关** - Spring Cloud Gateway
- **ORM 框架** - MyBatis Plus
- **数据库** - MySQL 8.0 / PostgreSQL 15+ (分库分表 ShardingSphere)
- **缓存** - Redis Cluster + Redisson
- **时序数据库** - InfluxDB 2.x / TimescaleDB
- **消息队列** - Apache Kafka 3.x / RocketMQ 5.x
- **搜索引擎** - Elasticsearch 8.x
- **文档存储** - MongoDB 6.x
- **对象存储** - MinIO / AWS S3
- **分布式事务** - Seata
- **限流熔断** - Sentinel
- **监控** - Prometheus + Grafana
- **链路追踪** - SkyWalking / Jaeger
- **日志** - ELK Stack (Elasticsearch + Logstash + Kibana)
- **容器编排** - Kubernetes + Istio

### 前端（多端）
- **Web** -  Vue 3 + TypeScript
  - 微前端框架：qiankun / Module Federation
  - UI 组件库：Ant Design / Element Plus
  - 状态管理：Redux Toolkit / Pinia
  - 数据可视化：ECharts / D3.js
  - 地图：Mapbox / Leaflet
- **小程序** - Taro / uni-app (统一多端)
  - 微信小程序、支付宝小程序、抖音小程序、百度小程序
- **APP** - Flutter / React Native
  - 热更新：CodePush
  - 推送：Firebase / JPush / 个推

### IoT 层
- **协议支持** - OCPP 1.6/2.0, GB/T 27930, MQTT, CoAP
- **边缘计算** - EdgeX Foundry / K3s
- **设备影子** - AWS IoT Device Shadow / 自研

### 数据层
- **数据湖** - HDFS / AWS S3
- **数据仓库** - Hive / ClickHouse
- **实时计算** - Apache Flink
- **离线计算** - Apache Spark
- **机器学习** - TensorFlow / PyTorch (负荷预测、路径规划)

### 运维
- **CI/CD** - Jenkins / GitLab CI / GitHub Actions
- **基础设施即代码** - Terraform
- **配置管理** - Ansible
- **容器镜像仓库** - Harbor
- **制品库** - Nexus / Artifactory

## 后端服务架构

### 🌐 基础设施层
- **gateway-service** - API 网关（入口路由、鉴权、限流、灰度、协议转换）
- **auth-service** - 认证授权中心（Token 签发、OAuth2 服务端、用户认证、会话管理）
- **monitor-service** - 指标监控告警（Prometheus 采集、实时告警、性能指标、健康检查）
- **log-service** - 日志聚合检索（ELK 聚合、日志检索、链路追踪、事后分析）

### 🎯 BFF 层（Backend For Frontend）
按终端和角色拆分，提供差异化的接口聚合：
- **bff-consumer** - C端用户 BFF（APP/小程序）
- **bff-operator** - 运营商 BFF（Web后台）
- **bff-oem** - 设备制造商 BFF（Web后台）
- **bff-utility** - 电力供应商 BFF（Web后台）
- **bff-admin** - 平台管理 BFF（Web后台）

### 💼 核心业务服务（15个微服务）

#### 用户与权限
- **account-service** - 账户与权限（用户、组织、角色、RBAC、MFA、审计日志）

#### 资产管理
- **station-service** - 站点管理（站点信息、地理位置、设施信息）
- **device-service** - 设备元数据管理（充电桩档案、型号规格、配置参数、资产信息）
- **iot-gateway-service** - IoT 设备状态网关（实时状态上报、OCPP/GB/T 协议、设备影子、指令下发、OTA 推送）

#### 充电业务
- **session-service** - 充电会话（订单创建、会话生命周期、状态机、异常补偿）
- **billing-service** - 计费服务（实时计价、分时定价、电量统计）
- **settlement-service** - 清结算（多方分账、对账、发票、税务）
- **payment-service** - 支付网关（微信/支付宝/银行卡、预授权、退款、对账）
- **pricing-service** - 定价策略（动态定价、峰谷平、促销活动、优惠券）

#### 智能调度
- **scheduling-service** - 调度服务（有序充电、功率分配、负荷均衡）
- **dr-vpp-service** - 需求响应与虚拟电厂（DR 指令执行、VPP 聚合、竞价交易、补偿计算）

#### 用户运营
- **member-service** - 会员与积分（会员等级、积分体系、优惠券、营销活动）

#### 系统支撑
- **notification-service** - 通知中心（短信、邮件、Push、站内信、模板管理）
- **work-order-service** - 工单与运维（告警、工单流转、巡检计划、备件管理）
- **report-service** - 报表与 BI（数据聚合、报表生成、订阅推送、数据导出）

### 🧠 算法服务
- **algorithm-service** - 算法引擎（负荷预测、拥堵预测、路径规划、功率优化、异常检测）

### 📊 数据服务
- **data-sync-service** - 数据同步（Binlog 监听、CDC、数据湖同步、冷热分层）
- **search-service** - 搜索服务（基于 Elasticsearch，站点搜索、日志检索、全文搜索）

### 📦 公共模块（Common）
- **common-core** - 核心工具（统一响应、异常处理、常量、枚举）
- **common-security** - 安全验证模块（Token 验证、签名验证、加密解密工具，不负责签发）
- **common-bff** - BFF 公共基础（认证拦截器、参数验证、响应封装、通用聚合逻辑）
- **common-redis** - Redis 工具（分布式锁、缓存注解、序列化）
- **common-mybatis** - MyBatis 配置（分页插件、字段填充、乐观锁）
- **common-kafka** - Kafka 工具（生产者、消费者、事务消息）
- **common-log** - 日志采集端（TraceID 生成、操作日志切面、访问日志拦截器，不负责聚合）
- **common-swagger** - API 文档（SpringDoc、接口注解）

## 核心业务流程

### 1. 用户充电流程（端到端 < 500ms）
```
用户 → APP扫码 → BFF → 鉴权 → 预扣费 → 订单创建 →
调度计算 → IoT网关 → 充电桩启动 → 状态回传 → 实时监控 →
停止充电 → 计费封账 → 分账结算 → 推送通知 → 用户评价
```

### 2. 需求响应流程（DR）
```
电力供应商 → 监测负荷 → 创建DR指令 → 选择区域 → 下发限制 →
平台接收 → 调度执行 → 功率调整 → 实时监控 → 评估补偿 → 结算分账
```

### 3. 智能调度流程
```
设备上报状态 → 负荷预测 → 功率分配计算 → 有序充电策略 →
指令下发 → 实时调整 → 效果评估 → 策略优化
```

## 数据架构

### 分库分表策略
- **订单/会话表** - 按地域（国家/大区）+ 时间（月/季度）分片
- **设备状态表** - 按设备 ID 分片，水平扩展
- **时序数据** - 使用 InfluxDB，按时间窗口分片，热数据 7 天

### 缓存设计
```
Redis Cluster 架构
├── Session 缓存（TTL 2小时）
├── 站点快照（TTL 10秒）
├── 价格策略（TTL 30分钟）
├── Token 缓存（TTL 24小时）
└── 热点保护（本地缓存 + 分布式缓存）
```

### 消息队列主题（事件驱动架构）
- `account-events` - 账户事件流（注册、认证、权限变更）
- `device-events` - 设备事件流（设备上线、离线、故障、配置变更）
- `session-events` - 充电会话事件流（启动、停止、异常、完成）
- `billing-events` - 计费事件流（实时计费、账单生成）
- `payment-events` - 支付事件流（支付成功、失败、退款）
- `settlement-events` - 结算事件流（对账、清算、分账）
- `alarm-events` - 告警事件流（设备告警、系统告警）
- `dr-commands` - 需求响应指令（DR 下发、执行结果）
- `notification-events` - 通知事件流（短信、Push、邮件）

## 性能指标

### 系统级 SLA
- **可用性** - 99.99%（年故障时间 < 52.6 分钟）
- **并发能力** - 核心链路 ≥ 50,000 TPS
- **响应时间** - API 网关 P95 < 100ms，P99 < 200ms
- **IoT 指令** - 端到端延迟 ≤ 500ms
- **数据一致性** - RPO ≤ 1 分钟，RTO ≤ 5 分钟

### 接口性能
| 接口类型     | P95 延迟 | P99 延迟 | 目标 TPS |
| ------------ | -------- | -------- | -------- |
| 地图找桩     | < 300ms  | < 500ms  | 10,000   |
| 扫码启充     | < 500ms  | < 800ms  | 5,000    |
| 充电状态查询 | < 100ms  | < 200ms  | 20,000   |
| 支付确认     | < 2s     | < 3s     | 3,000    |
| 数据看板     | < 1s     | < 2s     | 1,000    |

### 数据处理能力
- **设备上报频率** - 每 5 秒/次
- **日数据处理量** - ≥ 10 亿条
- **实时计算延迟** - < 1 秒
- **离线报表延迟** - T+1 天

## 开发规范

### 代码规范
- 遵循《阿里巴巴 Java 开发规范》
- 使用 Lombok 简化 POJO
- 统一异常处理（GlobalExceptionHandler）
- RESTful API 设计（/api/v1/资源名）
- 分层架构：Controller → Service → Manager → DAO

### 数据库规范
- **命名** - 表名/字段名：小写 + 下划线（snake_case）
- **必备字段** - id (主键), create_time, update_time, deleted (逻辑删除)
- **索引** - 高频查询字段建立索引，组合索引遵循最左前缀
- **分页** - 使用 MyBatis Plus 分页插件，禁止全表扫描

### 接口规范
- **统一响应** - Result<T> 封装（code, message, data, timestamp, traceId）
- **统一错误码** - 0 成功，40xxx 客户端错误，50xxx 服务端错误
- **幂等设计** - 关键接口支持 Idempotency-Key
- **签名验证** - 关键写接口要求签名（app_id + timestamp + nonce + sign）
- **版本控制** - Header: X-API-Version

### 安全规范
- **传输加密** - HTTPS (TLS 1.3)
- **数据加密** - 敏感字段 AES-256 加密存储
- **认证授权** - JWT Token（用户端），OAuth2（管理端），MFA（高权限）
- **防护** - 防 SQL 注入、XSS、CSRF；接口限流；敏感操作审计日志

## 🚀 快速开始

### 环境要求
```
- JDK 21+
- Maven 3.8+
- Docker 20+
- Docker Compose 2.0+
- Kubernetes 1.26+（生产环境）
```

### 📖 三种配置方式

Nova-X 支持三种配置管理方式，**推荐使用方式三（Nacos配置中心）**：

| 方式         | 说明                       | 适用场景                 | 文档链接                                                |
| ------------ | -------------------------- | ------------------------ | ------------------------------------------------------- |
| 方式一       | 直接修改 application.yml   | 单机开发、快速测试       | 见下文                                                  |
| 方式二       | 使用环境变量覆盖           | CI/CD、容器化部署        | 见下文                                                  |
| **方式三** ⭐ | **Nacos 配置中心（推荐）** | **生产环境、多环境管理** | [**查看详细教程**](backend/nacos-configs/QUICKSTART.md) |

---

### 方式三：使用 Nacos 配置中心（推荐）⭐

**优势**：
- ✅ 集中管理所有服务配置
- ✅ 支持配置热更新（无需重启服务）
- ✅ 多环境隔离（dev/test/prod）
- ✅ 配置历史版本管理
- ✅ 配置加密保护敏感信息

#### 快速启动（5分钟）

**详细步骤请查看**：[📖 Nacos 配置中心快速启动指南](backend/nacos-configs/QUICKSTART.md)

```bash
# 1. 启动所有中间件（包括 Nacos）
cd backend
docker-compose up -d

# 2. 访问 Nacos 控制台并上传配置
浏览器打开：http://localhost:8848/nacos
账号密码：nacos/nacos

# 3. 自动上传所有配置（可选）
cd backend/nacos-configs
.\upload-configs.ps1

# 4. 编译并启动服务
cd backend
mvn clean package -DskipTests
java -jar infrastructure/gateway-service/target/gateway-service-1.0.0.jar
```

#### 配置文件位置
- 📁 配置模板：[backend/nacos-configs/](backend/nacos-configs/)
- 📄 公共配置：[common.yaml](backend/nacos-configs/common.yaml) - MySQL、Redis、Kafka 地址
- 📄 服务配置：[account-service.yaml](backend/nacos-configs/account-service.yaml) 等

#### 修改数据库和中间件地址

编辑 Nacos 中的 `common.yaml`：
```yaml
datasource:
  host: 你的MySQL地址  # 修改这里
  username: 你的用户名
  password: 你的密码

redis:
  host: 你的Redis地址  # 修改这里
  password: 你的密码

kafka:
  bootstrap-servers: 你的Kafka地址:9092  # 修改这里
```

点击「发布」后，所有服务**自动生效**（无需重启）！🎉

---

### 方式一：本地开发（直接修改配置文件）

#### 1. 启动基础设施
```bash
cd backend
docker-compose up -d

# 等待所有服务启动（约 2-3 分钟）
docker-compose ps
```

启动的服务包括：
- MySQL (3306) - 业务数据库
- Redis (6379) - 缓存
- Nacos (8848) - 服务注册与配置中心
- Kafka (9092) - 消息队列
- Elasticsearch (9200) - 搜索引擎
- 其他中间件...

#### 2. 修改配置文件（如果不使用 localhost）

编辑各服务的 `application.yml`，例如：
```bash
# 修改账户服务配置
vim backend/services/account-service/src/main/resources/application.yml
```

修改数据库、Redis、Kafka 地址：
```yaml
spring:
  datasource:
    url: jdbc:mysql://你的MySQL地址:3306/nova_x_account
    username: 你的用户名
    password: 你的密码
  redis:
    host: 你的Redis地址
    password: 你的密码
```

#### 3. 编译项目
```bash
cd backend
mvn clean package -DskipTests
```

#### 4. 启动服务（按顺序）
```bash
# 1. 启动网关
java -jar infrastructure/gateway-service/target/gateway-service-1.0.0.jar

# 2. 启动业务服务
java -jar services/account-service/target/account-service-1.0.0.jar
java -jar services/station-service/target/station-service-1.0.0.jar
# ...
```

#### 5. 验证服务
```bash
# 测试网关
curl http://localhost:9000/actuator/health

# 测试账户服务（通过网关）
curl http://localhost:9000/api/v1/users/test
```

---

### 方式二：使用环境变量

```bash
# 设置环境变量
export MYSQL_HOST=192.168.1.100
export MYSQL_PASSWORD=your_password
export REDIS_HOST=192.168.1.101
export KAFKA_BOOTSTRAP_SERVERS=192.168.1.102:9092

# 启动服务
java -jar account-service-1.0.0.jar
```

或使用 `.env` 文件配合 Docker Compose：
```bash
# .env 文件
MYSQL_HOST=192.168.1.100
REDIS_HOST=192.168.1.101
KAFKA_BOOTSTRAP_SERVERS=192.168.1.102:9092

# 启动
docker-compose --env-file .env up -d
```

### 生产部署

#### 使用 Docker
```bash
# 构建镜像
mvn clean package -DskipTests
docker build -t nova-x/gateway-service:1.0.0 ./gateway-service

# 推送镜像
docker push nova-x/gateway-service:1.0.0

# 运行容器
docker run -d -p 8080:8080 \
  -e NACOS_SERVER_ADDR=nacos:8848 \
  nova-x/gateway-service:1.0.0
```

#### 使用 Kubernetes
```bash
# 创建命名空间
kubectl create namespace nova-x

# 部署配置
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml

# 部署服务
kubectl apply -f k8s/gateway-service/
kubectl apply -f k8s/services/

# 查看部署状态
kubectl get pods -n nova-x
kubectl get svc -n nova-x
```

## 监控与运维

### 监控指标
- **系统指标** - CPU、内存、磁盘、网络
- **JVM 指标** - 堆内存、GC 次数、线程数
- **应用指标** - QPS/TPS、响应时间、错误率
- **业务指标** - 充电订单数、营收、设备在线率
- **基础设施** - 数据库连接池、缓存命中率、MQ 积压

### 日志规范
```
应用日志：/var/log/nova-x/{service}/app.log
访问日志：/var/log/nova-x/{service}/access.log
错误日志：/var/log/nova-x/{service}/error.log

日志格式：[timestamp] [level] [traceId] [service] [class] - message
```

### 告警策略
| 级别 | 触发条件                         | 通知方式               | 响应时间 |
| ---- | -------------------------------- | ---------------------- | -------- |
| P0   | 服务不可用、核心接口失败率 > 10% | 电话 + 短信 + 企业微信 | 5 分钟   |
| P1   | 接口响应超时、数据库连接池耗尽   | 短信 + 企业微信        | 15 分钟  |
| P2   | 缓存失效、MQ 积压                | 企业微信 + 邮件        | 30 分钟  |
| P3   | 资源使用率 > 80%                 | 邮件                   | 1 小时   |

### 故障演练
- **服务降级演练** - 每月 1 次
- **数据中心切换演练** - 每季度 1 次
- **全链路压测** - 每半年 1 次
- **应急预案演练** - 每年 2 次

## 版本历史

### v1.0.0 (2026-01-20) - MVP 版本
**功能**
- ✅ 核心充电流程（找桩、扫码、启充、停充、支付）
- ✅ 基础账户体系（注册、登录、实名认证）
- ✅ OCPP 1.6 协议接入
- ✅ 微信/支付宝支付
- ✅ 基础站点/设备管理
- ✅ 运营商后台（设备监控、基础报表）

**基础设施**
- ✅ 微服务架构（Spring Cloud）
- ✅ 服务注册（Nacos）
- ✅ API 网关（Spring Cloud Gateway）
- ✅ 数据库（MySQL + Redis）
- ✅ 消息队列（Kafka）
- ✅ 容器化（Docker）

### v1.1.0 (计划 2026-Q2) - 功能增强
- 智能推荐与路径规划
- 动态定价与促销系统
- 会员体系与积分
- 运营数据看板
- OEM 管理后台
- 多协议支持（GB/T, OCPP 2.0）

### v2.0.0 (计划 2026-Q3) - 智能调度
- 有序充电算法
- 需求响应系统（DR）
- 虚拟电厂聚合（VPP）
- 负荷预测模型
- 电力供应商管理后台

### v3.0.0 (计划 2026-Q4) - 全球化
- 多语言支持（中英日韩德法）
- 多币种支持
- 异地多活部署
- 性能优化与安全加固
- 运营增长工具

## 贡献指南

### 分支策略
- `main` - 生产环境代码
- `develop` - 开发主分支
- `feature/*` - 功能分支
- `hotfix/*` - 紧急修复分支
- `release/*` - 发布分支

### 提交规范
```
feat: 新功能
fix: 修复 Bug
docs: 文档更新
style: 代码格式调整
refactor: 重构
perf: 性能优化
test: 测试用例
chore: 构建/工具链更新
```

### Code Review 要求
- 所有代码必须经过 Code Review
- 至少 2 人 Approve 才能合并
- 必须通过所有自动化测试
- 代码覆盖率不低于 80%

## 联系方式
- **项目负责人** - Nova-X Team
- **技术支持** - tech-support@nova-x.com
- **文档站点** - https://docs.nova-x.com
- **问题反馈** - GitHub Issues

## 开发规范

### 代码规范
- 遵循阿里巴巴 Java 开发规范
- 使用 Lombok 简化代码
- 统一异常处理
- RESTful API 设计规范
- 接口版本控制：/api/v1

### 数据库规范
- 表名：小写 + 下划线
- 字段名：小写 + 下划线
- 必备字段：id, create_time, update_time, deleted
- 分库分表：按地域 + 时间维度

### 接口规范
- 统一响应格式：Result<T>
- 统一错误码
- 幂等性设计
- 签名验证
- 请求日志记录

### 安全规范
- 敏感数据加密存储
- 传输层 TLS 1.3
- JWT Token 认证
- OAuth2 授权
- 防 SQL 注入
- 防 XSS 攻击

## 快速开始

### 环境要求
- JDK 21+
- Maven 3.8+
- Docker 20+
- Kubernetes 1.26+（生产环境）

### 本地开发
```bash
# 启动基础设施（MySQL、Redis、Kafka、Nacos 等）
docker-compose up -d

# 编译项目
mvn clean install -DskipTests

# 启动网关
cd gateway-service && mvn spring-boot:run

# 启动业务服务
cd services/account-service && mvn spring-boot:run
```

### 部署
```bash
# 构建镜像
mvn clean package -DskipTests
docker build -t nova-x/gateway-service:latest ./gateway-service

# 部署到 K8s
kubectl apply -f k8s/
```

## 监控与运维

### 监控指标
- QPS/TPS
- 响应时间（P95/P99）
- 错误率
- 服务健康度
- 数据库连接池
- 缓存命中率
- 消息队列积压

### 日志
- 应用日志：/var/log/nova-x/{service}/app.log
- 访问日志：/var/log/nova-x/{service}/access.log
- 错误日志：/var/log/nova-x/{service}/error.log

### 告警
- 服务不可用
- 接口响应超时
- 数据库连接池耗尽
- 消息队列积压
- 缓存失效

## 性能指标
- API 网关：≥ 50,000 TPS
- 核心业务接口：P95 延迟 ≤ 100ms
- IoT 指令下发：端到端延迟 ≤ 500ms
- 数据库查询：≤ 100ms
- 缓存查询：≤ 10ms

## 版本历史
- v1.0.0 (2026-01-20): MVP 版本，核心充电流程
- v1.1.0 (计划): 智能推荐、动态定价
- v2.0.0 (计划): 智能调度、DR/VPP
- v3.0.0 (计划): 全球化、多语言

## 联系方式
- 项目负责人：Nova-X Team
- 技术支持：tech-support@nova-x.com
