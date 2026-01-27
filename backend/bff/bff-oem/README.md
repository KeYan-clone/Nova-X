# BFF OEM Service - 设备制造商聚合服务

## 服务简介

**bff-oem** 是面向设备制造商（OEM）的BFF聚合服务，提供设备注册、监控、远程管理和统计分析功能。

### 核心功能

#### 1. 设备管理
- 设备注册（单个/批量）
- 设备激活
- 查询厂商设备列表

#### 2. 设备监控
- 实时设备状态
- 实时数据查询
- 远程指令下发
- OTA固件升级

#### 3. 数据统计
- OEM概览（设备总数、在线数、故障数）
- 设备统计（出货量、激活率、在线率）

## API 接口

### 设备管理
```http
POST /oem/devices/register              # 注册设备
POST /oem/devices/batch-register        # 批量注册
GET  /oem/devices/manufacturer/{id}     # 厂商设备列表
POST /oem/devices/{deviceId}/activate   # 激活设备
```

### 设备监控
```http
GET  /oem/monitor/devices/{deviceId}/status    # 设备状态
GET  /oem/monitor/devices/{deviceId}/realtime  # 实时数据
POST /oem/monitor/devices/{deviceId}/commands  # 发送指令
POST /oem/monitor/devices/{deviceId}/ota       # OTA升级
```

### 数据统计
```http
GET  /oem/dashboard/overview/{manufacturerId}  # OEM概览
GET  /oem/dashboard/stats/{manufacturerId}     # 设备统计
```

## 部署信息

**服务端口**: 8203
**服务名**: bff-oem
**API文档**: http://localhost:8203/doc.html
