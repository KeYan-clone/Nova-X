-- 计费服务数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS novax_billing DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE novax_billing;

-- 账单表
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bill_no` varchar(32) NOT NULL COMMENT '账单编号',
  `session_id` bigint NOT NULL COMMENT '会话ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `station_id` bigint NOT NULL COMMENT '充电站ID',
  `station_name` varchar(100) DEFAULT NULL COMMENT '充电站名称',
  `device_id` bigint NOT NULL COMMENT '设备ID',
  `device_code` varchar(32) DEFAULT NULL COMMENT '设备编号',
  `connector_id` bigint NOT NULL COMMENT '充电枪ID',
  `connector_code` varchar(32) DEFAULT NULL COMMENT '充电枪编号',
  `start_time` datetime NOT NULL COMMENT '充电开始时间',
  `end_time` datetime NOT NULL COMMENT '充电结束时间',
  `charging_duration` int NOT NULL DEFAULT 0 COMMENT '充电时长(分钟)',
  `charging_energy` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '充电电量(kWh)',
  `pricing_template_id` bigint DEFAULT NULL COMMENT '定价模板ID',
  `electricity_cost` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '电费(元)',
  `service_fee` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '服务费(元)',
  `parking_fee` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '停车费(元)',
  `discount_amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额(元)',
  `total_amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '总金额(元)',
  `paid_amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '实付金额(元)',
  `bill_status` varchar(20) NOT NULL DEFAULT 'UNPAID' COMMENT '账单状态: UNPAID-未支付, PAID-已支付, REFUNDED-已退款, CANCELLED-已取消',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式: WECHAT-微信, ALIPAY-支付宝, UNIONPAY-银联, BALANCE-余额',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payment_transaction_no` varchar(64) DEFAULT NULL COMMENT '支付流水号',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0-未删除, 1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_bill_no` (`bill_no`),
  KEY `idx_session_id` (`session_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_station_id` (`station_id`),
  KEY `idx_bill_status` (`bill_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='账单表';

-- 插入测试数据
INSERT INTO `bill` (`bill_no`, `session_id`, `user_id`, `station_id`, `station_name`,
                    `device_id`, `device_code`, `connector_id`, `connector_code`,
                    `start_time`, `end_time`, `charging_duration`, `charging_energy`,
                    `pricing_template_id`, `electricity_cost`, `service_fee`, `parking_fee`,
                    `discount_amount`, `total_amount`, `paid_amount`, `bill_status`)
VALUES
('BL20260120120000001', 1, 1001, 1, '市中心充电站', 1, 'DV20260120001', 1, 'CNDV20260120001001',
 '2026-01-20 08:00:00', '2026-01-20 10:30:00', 150, 35.68,
 1, 28.54, 3.57, 5.00, 0.00, 37.11, 37.11, 'PAID'),

('BL20260120130000002', 2, 1002, 1, '市中心充电站', 2, 'DV20260120002', 3, 'CNDV20260120002001',
 '2026-01-20 10:00:00', '2026-01-20 11:30:00', 90, 22.50,
 1, 18.00, 2.25, 0.00, 0.00, 20.25, 20.25, 'UNPAID'),

('BL20260120140000003', 3, 1003, 2, '商业区充电站', 3, 'DV20260120003', 5, 'CNDV20260120003001',
 '2026-01-20 14:00:00', '2026-01-20 18:00:00', 240, 58.90,
 1, 47.12, 5.89, 10.00, 5.00, 58.01, 58.01, 'PAID');

-- 账单统计表（可选，用于汇总统计）
DROP TABLE IF EXISTS `bill_statistics`;
CREATE TABLE `bill_statistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `station_id` bigint DEFAULT NULL COMMENT '充电站ID(NULL表示全部)',
  `total_bills` int NOT NULL DEFAULT 0 COMMENT '账单总数',
  `paid_bills` int NOT NULL DEFAULT 0 COMMENT '已支付账单数',
  `total_amount` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '总金额',
  `paid_amount` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '已支付金额',
  `total_energy` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '总电量(kWh)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_date_station` (`stat_date`, `station_id`),
  KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='账单统计表';
    station_id BIGINT COMMENT 'Station ID (NULL for default)',
    rule_type TINYINT COMMENT 'Rule type: 1-TOU, 2-Flat, 3-Dynamic',
    base_price DECIMAL(10,4) COMMENT 'Base price (per kWh)',
    service_fee DECIMAL(10,4) COMMENT 'Service fee (per kWh)',
    valid_from DATETIME NOT NULL COMMENT 'Valid from',
    valid_to DATETIME COMMENT 'Valid to',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1-Active, 2-Inactive',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_operator_id (operator_id),
    INDEX idx_station_id (station_id),
    INDEX idx_valid_from (valid_from)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Pricing rule table';

-- Billing item table
CREATE TABLE IF NOT EXISTS t_billing_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id BIGINT NOT NULL COMMENT 'Session ID',
    user_id BIGINT NOT NULL COMMENT 'User ID',
    item_type TINYINT COMMENT 'Item type: 1-Energy, 2-Service, 3-Parking',
    quantity DECIMAL(10,3) COMMENT 'Quantity',
    unit_price DECIMAL(10,4) COMMENT 'Unit price',
    amount DECIMAL(10,2) COMMENT 'Amount',
    start_time DATETIME COMMENT 'Start time',
    end_time DATETIME COMMENT 'End time',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_session_id (session_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Billing item table';
