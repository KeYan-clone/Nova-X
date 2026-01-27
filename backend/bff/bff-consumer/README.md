# BFF Consumer Service - C端用户聚合服务

## 服务简介

**bff-consumer** 是面向C端用户（车主）的BFF聚合服务，为移动APP和小程序提供优化的接口。

### 核心功能

#### 1. 充电服务
- 查找附近充电站
- 扫码启动充电
- 实时充电状态
- 停止充电
- 充电历史记录

#### 2. 支付服务
- 账单查询
- 创建支付订单
- 支付记录
- 充电详情（聚合会话+账单）

#### 3. 会员服务
- 会员信息
- 余额查询
- 积分查询
- 余额充值

#### 4. 用户首页
- 会员信息展示
- 最近充电记录
- 附近充电站推荐

## API 接口

### 充电相关
```http
GET  /consumer/nearby-stations      # 查找附近充电站
GET  /consumer/home/{userId}         # 用户首页
POST /consumer/charging/start        # 启动充电
POST /consumer/charging/{sessionId}/stop  # 停止充电
```

### 支付相关
```http
GET  /consumer/payment/bills/{sessionId}  # 获取账单
POST /consumer/payment/create             # 创建支付
GET  /consumer/payment/history/{userId}   # 支付记录
GET  /consumer/payment/charge-detail/{sessionId}  # 充电详情
```

### 会员相关
```http
GET  /consumer/member/{userId}          # 会员信息
GET  /consumer/member/{userId}/balance  # 余额查询
GET  /consumer/member/{userId}/points   # 积分查询
POST /consumer/member/recharge          # 余额充值
```

## 部署信息

**服务端口**: 8201
**服务名**: bff-consumer
**API文档**: http://localhost:8201/doc.html
