-- 会员服务数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS novax_member DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE novax_member;

-- 会员等级表
DROP TABLE IF EXISTS `member_level`;
CREATE TABLE `member_level` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `level_name` varchar(50) NOT NULL COMMENT '等级名称',
  `level_code` varchar(20) NOT NULL COMMENT '等级代码',
  `level` int NOT NULL COMMENT '等级',
  `required_points` int NOT NULL DEFAULT 0 COMMENT '升级所需积分',
  `charging_discount` decimal(3,2) NOT NULL DEFAULT 1.00 COMMENT '充电折扣率 (0.1-1.0)',
  `service_fee_discount` decimal(3,2) NOT NULL DEFAULT 1.00 COMMENT '服务费折扣率 (0.1-1.0)',
  `free_parking_minutes` int NOT NULL DEFAULT 0 COMMENT '每月免费停车时长(分钟)',
  `points_multiplier` decimal(3,2) NOT NULL DEFAULT 1.00 COMMENT '积分倍率',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用',
  `description` varchar(500) DEFAULT NULL COMMENT '等级描述',
  `icon_url` varchar(200) DEFAULT NULL COMMENT '等级图标URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0-未删除, 1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_level_code` (`level_code`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会员等级表';

-- 插入会员等级数据
INSERT INTO `member_level` (`level_name`, `level_code`, `level`, `required_points`,
                            `charging_discount`, `service_fee_discount`, `free_parking_minutes`,
                            `points_multiplier`, `description`)
VALUES
('青铜会员', 'BRONZE', 1, 0, 1.00, 1.00, 0, 1.00, '注册即可成为青铜会员'),
('白银会员', 'SILVER', 2, 1000, 0.98, 0.95, 120, 1.20, '累计积分1000分升级为白银会员'),
('黄金会员', 'GOLD', 3, 5000, 0.95, 0.90, 240, 1.50, '累计积分5000分升级为黄金会员'),
('铂金会员', 'PLATINUM', 4, 10000, 0.92, 0.85, 360, 2.00, '累计积分10000分升级为铂金会员'),
('钻石会员', 'DIAMOND', 5, 50000, 0.88, 0.80, 600, 3.00, '累计积分50000分升级为钻石会员');

-- 会员信息表
DROP TABLE IF EXISTS `member_info`;
CREATE TABLE `member_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `member_no` varchar(32) NOT NULL COMMENT '会员编号',
  `level_id` bigint NOT NULL COMMENT '会员等级ID',
  `level_code` varchar(20) NOT NULL COMMENT '会员等级代码',
  `current_points` int NOT NULL DEFAULT 0 COMMENT '当前积分',
  `total_points` int NOT NULL DEFAULT 0 COMMENT '累计积分',
  `balance` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额(元)',
  `charging_count` int NOT NULL DEFAULT 0 COMMENT '累计充电次数',
  `total_charging_energy` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '累计充电电量(kWh)',
  `total_spent` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '累计消费金额(元)',
  `member_status` varchar(20) NOT NULL DEFAULT 'NORMAL' COMMENT '会员状态: NORMAL-正常, FROZEN-冻结, CANCELLED-已注销',
  `member_since` datetime NOT NULL COMMENT '会员开通时间',
  `expire_time` datetime DEFAULT NULL COMMENT '会员到期时间',
  `used_free_parking_minutes` int NOT NULL DEFAULT 0 COMMENT '本月已用免费停车时长(分钟)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0-未删除, 1-已删除)',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_member_no` (`member_no`),
  KEY `idx_level_id` (`level_id`),
  KEY `idx_member_status` (`member_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会员信息表';

-- 积分记录表
DROP TABLE IF EXISTS `points_record`;
CREATE TABLE `points_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `member_id` bigint NOT NULL COMMENT '会员ID',
  `change_type` varchar(20) NOT NULL COMMENT '积分变化类型: EARN-获得, CONSUME-消费, EXPIRE-过期, REFUND-退还',
  `points` int NOT NULL COMMENT '积分变化值',
  `balance_after` int NOT NULL COMMENT '变化后余额',
  `business_type` varchar(50) DEFAULT NULL COMMENT '业务类型: CHARGING-充电, RECHARGE-充值, SIGNUP-注册, etc',
  `business_id` bigint DEFAULT NULL COMMENT '关联业务ID',
  `related_amount` decimal(10,2) DEFAULT NULL COMMENT '关联金额(元)',
  `description` varchar(200) DEFAULT NULL COMMENT '积分来源描述',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0-未删除, 1-已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_change_type` (`change_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='积分记录表';

-- 余额记录表
DROP TABLE IF EXISTS `balance_record`;
CREATE TABLE `balance_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `member_id` bigint NOT NULL COMMENT '会员ID',
  `change_type` varchar(20) NOT NULL COMMENT '变化类型: RECHARGE-充值, CONSUME-消费, REFUND-退款, WITHDRAW-提现',
  `amount` decimal(10,2) NOT NULL COMMENT '变化金额(元)',
  `balance_after` decimal(10,2) NOT NULL COMMENT '变化后余额(元)',
  `business_type` varchar(50) DEFAULT NULL COMMENT '业务类型: CHARGING-充电, MANUAL-手动, GIFT-赠送, etc',
  `business_id` bigint DEFAULT NULL COMMENT '关联业务ID',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记(0-未删除, 1-已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_change_type` (`change_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='余额记录表';

-- 插入测试数据
INSERT INTO `member_info` (`user_id`, `member_no`, `level_id`, `level_code`,
                           `current_points`, `total_points`, `balance`,
                           `charging_count`, `total_charging_energy`, `total_spent`,
                           `member_status`, `member_since`)
VALUES
(1001, 'MB20260120001', 3, 'GOLD', 5280, 5280, 258.50, 136, 3568.90, 5280.50,
 'NORMAL', '2025-01-15 10:30:00'),

(1002, 'MB20260120002', 2, 'SILVER', 1580, 1580, 86.20, 45, 1256.30, 1850.60,
 'NORMAL', '2025-06-20 14:20:00'),

(1003, 'MB20260120003', 4, 'PLATINUM', 12680, 12680, 568.90, 285, 7856.20, 12580.30,
 'NORMAL', '2024-10-10 09:15:00');
