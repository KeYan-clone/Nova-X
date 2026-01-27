# BFF Utility Service - 电力供应商聚合服务

## 服务简介

**bff-utility** 是面向电力供应商的BFF聚合服务，提供需求响应、电网管理、结算对账等功能。

### 核心功能

#### 1. 需求响应（DR）
- 创建DR事件
- 查询进行中事件
- 查询可用设备
- DR概览（聚合事件和设备）

#### 2. 电网管理
- 电网负荷监控
- 区域用电统计
- 电价策略查询

#### 3. 结算管理
- 创建结算单
- 待结算列表
- 结算单详情
- 确认结算

## API 接口

### 需求响应
```http
POST /utility/dr-events             # 创建DR事件
GET  /utility/dr-overview           # DR概览（聚合）
```

### 电网管理
```http
GET  /utility/grid/load                          # 电网负荷
GET  /utility/grid/regions/{regionId}/consumption # 区域用电
GET  /utility/grid/pricing                       # 电价策略
```

### 结算管理
```http
POST /utility/settlement                  # 创建结算单
GET  /utility/settlement/pending          # 待结算列表
GET  /utility/settlement/{settlementId}   # 结算详情
POST /utility/settlement/{settlementId}/confirm  # 确认结算
```

## 部署信息

**服务端口**: 8204
**服务名**: bff-utility
**API文档**: http://localhost:8204/doc.html
