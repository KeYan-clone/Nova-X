-- 支付服务数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS novax_payment DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE novax_payment;

-- 支付记录表
DROP TABLE IF EXISTS `payment_record`;
CREATE TABLE `payment_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `payment_no` varchar(32) NOT NULL COMMENT '支付单号',
  `bill_id` bigint NOT NULL COMMENT '账单ID',
  `bill_no` varchar(32) NOT NULL COMMENT '账单编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `payment_amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '支付金额(元)',
  `payment_method` varchar(20) NOT NULL COMMENT '支付方式: WECHAT-微信, ALIPAY-支付宝, UNIONPAY-银联, BALANCE-余额',
  `payment_channel` varchar(50) DEFAULT NULL COMMENT '支付渠道: WECHAT_APP-微信APP, WECHAT_H5-微信H5, ALIPAY_APP-支付宝APP, etc',
  `payment_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '支付状态: PENDING-待支付, PAYING-支付中, SUCCESS-支付成功, FAILED-支付失败, CLOSED-已关闭',
  `third_payment_no` varchar(64) DEFAULT NULL COMMENT '第三方支付单号',
  `third_transaction_no` varchar(64) DEFAULT NULL COMMENT '第三方交易流水号',
  `payment_time` datetime DEFAULT NULL COMMENT '支付完成时间',
  `failure_reason` varchar(200) DEFAULT NULL COMMENT '支付失败原因',
  `notify_time` datetime DEFAULT NULL COMMENT '回调通知时间',
  `notify_count` int NOT NULL DEFAULT 0 COMMENT '回调通知次数',
  `client_ip` varchar(50) DEFAULT NULL COMMENT '用户支付IP',
  `device_info` varchar(200) DEFAULT NULL COMMENT '用户设备信息',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0-未删除, 1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_no` (`payment_no`),
  KEY `idx_bill_id` (`bill_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_third_payment_no` (`third_payment_no`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付记录表';

-- 退款记录表
DROP TABLE IF EXISTS `refund_record`;
CREATE TABLE `refund_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `refund_no` varchar(32) NOT NULL COMMENT '退款单号',
  `payment_id` bigint NOT NULL COMMENT '支付记录ID',
  `payment_no` varchar(32) NOT NULL COMMENT '支付单号',
  `bill_id` bigint NOT NULL COMMENT '账单ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `refund_amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '退款金额(元)',
  `refund_reason` varchar(200) NOT NULL COMMENT '退款原因',
  `refund_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '退款状态: PENDING-待退款, PROCESSING-退款中, SUCCESS-退款成功, FAILED-退款失败',
  `third_refund_no` varchar(64) DEFAULT NULL COMMENT '第三方退款单号',
  `refund_time` datetime DEFAULT NULL COMMENT '退款完成时间',
  `failure_reason` varchar(200) DEFAULT NULL COMMENT '退款失败原因',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0-未删除, 1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_refund_no` (`refund_no`),
  KEY `idx_payment_id` (`payment_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_refund_status` (`refund_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='退款记录表';

-- 插入测试数据
INSERT INTO `payment_record` (`payment_no`, `bill_id`, `bill_no`, `user_id`, `payment_amount`,
                               `payment_method`, `payment_channel`, `payment_status`,
                               `third_payment_no`, `third_transaction_no`, `payment_time`, `notify_count`)
VALUES
('PAY20260120120000001', 1, 'BL20260120120000001', 1001, 37.11,
 'WECHAT', 'WECHAT_APP', 'SUCCESS',
 '4200001234567890123456', '1234567890123456789', '2026-01-20 10:32:15', 1),

('PAY20260120140000002', 3, 'BL20260120140000003', 1003, 58.01,
 'ALIPAY', 'ALIPAY_APP', 'SUCCESS',
 '2026012012345678901234', '2026012012345678901234', '2026-01-20 18:05:30', 1),

('PAY20260120150000003', 2, 'BL20260120130000002', 1002, 20.25,
 'WECHAT', 'WECHAT_H5', 'PENDING',
 '4200001234567890123457', NULL, NULL, 0);
