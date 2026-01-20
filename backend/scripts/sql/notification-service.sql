CREATE DATABASE IF NOT EXISTS novax_notification DEFAULT CHARACTER SET utf8mb4;

USE novax_notification;

DROP TABLE IF EXISTS `notification_template`;
CREATE TABLE `notification_template` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `template_code` varchar(50) NOT NULL COMMENT '模板编码',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `message_type` varchar(20) NOT NULL COMMENT '消息类型',
  `title` varchar(200) DEFAULT NULL COMMENT '模板标题',
  `content` text NOT NULL COMMENT '模板内容',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`template_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知模板表';

DROP TABLE IF EXISTS `notification_message`;
CREATE TABLE `notification_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `message_type` varchar(20) NOT NULL COMMENT '消息类型',
  `template_id` bigint DEFAULT NULL COMMENT '模板ID',
  `title` varchar(200) DEFAULT NULL COMMENT '消息标题',
  `content` text NOT NULL COMMENT '消息内容',
  `receiver` varchar(100) DEFAULT NULL COMMENT '接收地址',
  `business_type` varchar(50) DEFAULT NULL COMMENT '业务类型',
  `business_id` bigint DEFAULT NULL COMMENT '业务ID',
  `send_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '发送状态',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `failure_reason` varchar(500) DEFAULT NULL COMMENT '失败原因',
  `retry_count` int NOT NULL DEFAULT 0 COMMENT '重试次数',
  `is_read` tinyint NOT NULL DEFAULT 0 COMMENT '是否已读',
  `read_time` datetime DEFAULT NULL COMMENT '读取时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_send_status` (`send_status`),
  KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知消息表';

INSERT INTO `notification_template` (`template_code`, `template_name`, `message_type`, `title`, `content`) VALUES
('CHARGING_START', '充电开始通知', 'IN_APP', '充电已开始', '您在${stationName}的充电已开始，设备编号：${deviceCode}'),
('CHARGING_COMPLETE', '充电完成通知', 'IN_APP', '充电已完成', '充电已完成！本次充电${energy}kWh，费用${amount}元'),
('PAYMENT_SUCCESS', '支付成功通知', 'IN_APP', '支付成功', '您已成功支付${amount}元'),
('BALANCE_LOW', '余额不足提醒', 'PUSH', '余额不足', '您的账户余额仅剩${balance}元，请及时充值');
