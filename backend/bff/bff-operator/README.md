# BFF Operator Service - 运营商聚合服务

## 服务简介

**bff-operator** 是面向充电站运营商的BFF聚合服务，提供站点管理、设备监控、工单处理和运营报表等功能。

### 核心功能

#### 1. 站点管理
- 站点列表（支持按运营商筛选）
- 站点详情
- 创建/更新站点
- 站点设备列表

#### 2. 设备管理
- 设备状态统计
- 设备维护设置

#### 3. 工单管理
- 工作台数据聚合
- 我的工单
- 待处理工单
- 接单/完成工单

#### 4. 运营报表
- 充电统计
- 收入统计
- 站点统计
- 运营概览（聚合多维度数据）

## API 接口

### 站点管理
```http
GET  /operator/stations              # 站点列表
GET  /operator/stations/{stationId}  # 站点详情
POST /operator/stations              # 创建站点
PUT  /operator/stations/{stationId}  # 更新站点
GET  /operator/stations/{stationId}/devices  # 站点设备
```

### 设备管理
```http
GET  /operator/devices/status-stats        # 设备状态统计
POST /operator/devices/{deviceId}/maintain # 设置维护
```

### 工单管理
```http
GET  /operator/workbench/{operatorId}      # 工作台
POST /operator/orders/{orderId}/accept     # 接单
POST /operator/orders/{orderId}/complete   # 完成工单
```

### 报表管理
```http
GET  /operator/reports/charging   # 充电统计
GET  /operator/reports/revenue    # 收入统计
GET  /operator/reports/stations   # 站点统计
GET  /operator/reports/overview   # 运营概览
```

## 部署信息

**服务端口**: 8202
**服务名**: bff-operator
**API文档**: http://localhost:8202/doc.html
