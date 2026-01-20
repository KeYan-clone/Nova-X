-- 充电会话数据库初始化

CREATE DATABASE IF NOT EXISTS novax_session DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novax_session;

-- 充电会话表
DROP TABLE IF EXISTS `charging_session`;
CREATE TABLE `charging_session` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `session_code` VARCHAR(50) NOT NULL COMMENT '会话编号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `station_id` BIGINT NOT NULL COMMENT '站点ID',
  `device_id` BIGINT NOT NULL COMMENT '设备ID',
  `connector_id` BIGINT NOT NULL COMMENT '充电枪ID',
  `session_status` VARCHAR(20) NOT NULL COMMENT '会话状态',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME COMMENT '结束时间',
  `charging_energy` DECIMAL(10, 2) DEFAULT 0 COMMENT '充电电量(kWh)',
  `start_soc` INT COMMENT '开始SOC(%)',
  `end_soc` INT COMMENT '结束SOC(%)',
  `total_cost` DECIMAL(10, 2) COMMENT '总费用(元)',
  `electricity_cost` DECIMAL(10, 2) COMMENT '电费(元)',
  `service_cost` DECIMAL(10, 2) COMMENT '服务费(元)',
  `parking_cost` DECIMAL(10, 2) COMMENT '停车费(元)',
  `stop_reason` VARCHAR(100) COMMENT '停止原因',
  `remark` VARCHAR(500),
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_session_code` (`session_code`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_connector_id` (`connector_id`),
  KEY `idx_status` (`session_status`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充电会话表';
