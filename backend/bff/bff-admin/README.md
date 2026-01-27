# BFF Admin Service - 管理后台聚合服务

## 服务简介

**bff-admin** 是面向平台管理员的BFF聚合服务，提供系统管理、监控、用户管理、充电站管理等功能。

### 核心功能

#### 1. 系统仪表盘
- 聚合充电统计、营收统计、充电站统计
- 充电站详情+设备列表（跨服务聚合）

#### 2. 系统监控
- 服务健康状态（通过monitor-service）
- 系统指标监控
- 日志查询

#### 3. 用户管理
- 用户列表
- 用户详情
- 禁用/启用用户

#### 4. 充电站管理
- 充电站列表
- 充电站详情
- 审核充电站
- 下线充电站

## API 接口

### 系统仪表盘
```http
GET /admin/dashboard                       # 仪表盘数据（聚合）
GET /admin/stations/{stationId}/detail     # 充电站详情（聚合）
```

### 系统监控
```http
GET /admin/monitor/health          # 服务健康状态
GET /admin/monitor/metrics         # 系统指标
GET /admin/monitor/logs            # 日志查询
```

### 用户管理
```http
GET  /admin/users                     # 用户列表
GET  /admin/users/{accountId}         # 用户详情
POST /admin/users/{accountId}/disable # 禁用用户
POST /admin/users/{accountId}/enable  # 启用用户
```

### 充电站管理
```http
GET  /admin/stations                       # 充电站列表
GET  /admin/stations/{stationId}           # 充电站详情
POST /admin/stations/{stationId}/approve   # 审核充电站
POST /admin/stations/{stationId}/offline   # 下线充电站
```

## 部署信息

**服务端口**: 8205
**服务名**: bff-admin
**API文档**: http://localhost:8205/doc.html
