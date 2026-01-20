-- 设备管理数据库初始化脚本

CREATE DATABASE IF NOT EXISTS novax_device DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novax_device;

-- 设备表
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_code` VARCHAR(50) NOT NULL COMMENT '设备编码',
  `station_id` BIGINT NOT NULL COMMENT '站点ID',
  `device_type` VARCHAR(20) NOT NULL COMMENT '设备类型：AC-交流，DC-直流',
  `device_status` VARCHAR(20) NOT NULL DEFAULT 'OFFLINE' COMMENT '设备状态：IDLE-空闲，CHARGING-充电中，FAULT-故障，OFFLINE-离线，MAINTENANCE-维护中',
  `manufacturer` VARCHAR(50) NOT NULL COMMENT '制造商',
  `model` VARCHAR(50) NOT NULL COMMENT '设备型号',
  `rated_power` DECIMAL(10, 2) NOT NULL COMMENT '额定功率(kW)',
  `rated_voltage` DECIMAL(10, 2) NOT NULL COMMENT '额定电压(V)',
  `rated_current` DECIMAL(10, 2) NOT NULL COMMENT '额定电流(A)',
  `connector_count` INT NOT NULL DEFAULT 1 COMMENT '充电枪数量',
  `ip_address` VARCHAR(50) COMMENT 'IP地址',
  `firmware_version` VARCHAR(20) COMMENT '固件版本',
  `last_online_time` DATETIME COMMENT '最后在线时间',
  `last_offline_time` DATETIME COMMENT '最后离线时间',
  `total_charging_count` INT NOT NULL DEFAULT 0 COMMENT '累计充电次数',
  `total_charging_energy` DECIMAL(12, 2) NOT NULL DEFAULT 0.00 COMMENT '累计充电电量(kWh)',
  `total_working_hours` DECIMAL(12, 2) NOT NULL DEFAULT 0.00 COMMENT '累计工作时长(小时)',
  `install_date` DATETIME COMMENT '安装日期',
  `warranty_expire_date` DATETIME COMMENT '质保截止日期',
  `maintenance_cycle` INT COMMENT '维护周期(天)',
  `last_maintenance_time` DATETIME COMMENT '上次维护时间',
  `remark` VARCHAR(500) COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_code` (`device_code`),
  KEY `idx_station_id` (`station_id`),
  KEY `idx_device_status` (`device_status`),
  KEY `idx_device_type` (`device_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备表';

-- 充电枪表
DROP TABLE IF EXISTS `connector`;
CREATE TABLE `connector` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `connector_code` VARCHAR(50) NOT NULL COMMENT '充电枪编码',
  `device_id` BIGINT NOT NULL COMMENT '设备ID',
  `connector_no` INT NOT NULL COMMENT '充电枪序号',
  `connector_type` VARCHAR(20) NOT NULL COMMENT '充电枪类型：AC-交流，DC_GBT-国标直流，DC_CHADEMO-CHAdeMO，DC_CCS-CCS',
  `connector_status` VARCHAR(20) NOT NULL DEFAULT 'IDLE' COMMENT '充电枪状态：IDLE-空闲，CHARGING-充电中，FAULT-故障，RESERVED-预约中，UNAVAILABLE-不可用',
  `max_power` DECIMAL(10, 2) NOT NULL COMMENT '最大功率(kW)',
  `max_voltage` DECIMAL(10, 2) NOT NULL COMMENT '最大电压(V)',
  `max_current` DECIMAL(10, 2) NOT NULL COMMENT '最大电流(A)',
  `current_session_id` BIGINT COMMENT '当前会话ID',
  `total_charging_count` INT NOT NULL DEFAULT 0 COMMENT '累计充电次数',
  `total_charging_energy` DECIMAL(12, 2) NOT NULL DEFAULT 0.00 COMMENT '累计充电电量(kWh)',
  `remark` VARCHAR(500) COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_connector_code` (`connector_code`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_connector_status` (`connector_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充电枪表';

-- 插入测试数据
INSERT INTO `device` (`device_code`, `station_id`, `device_type`, `device_status`, `manufacturer`, `model`,
                      `rated_power`, `rated_voltage`, `rated_current`, `connector_count`, `ip_address`,
                      `firmware_version`, `install_date`, `warranty_expire_date`, `maintenance_cycle`)
VALUES
('DV202601200001', 1, 'DC', 'IDLE', '特来电', 'TLD-120DC', 120.00, 750.00, 160.00, 2, '192.168.1.101', 'v2.3.1', '2024-01-01', '2027-01-01', 90),
('DV202601200002', 1, 'AC', 'IDLE', '星星充电', 'XXC-7AC', 7.00, 220.00, 32.00, 1, '192.168.1.102', 'v1.5.2', '2024-01-01', '2027-01-01', 90),
('DV202601200003', 2, 'DC', 'OFFLINE', '国家电网', 'SGCC-60DC', 60.00, 500.00, 120.00, 1, '192.168.2.101', 'v3.1.0', '2024-02-01', '2027-02-01', 60);

INSERT INTO `connector` (`connector_code`, `device_id`, `connector_no`, `connector_type`, `connector_status`,
                         `max_power`, `max_voltage`, `max_current`)
VALUES
('CNDV20260120000101', 1, 1, 'DC_GBT', 'IDLE', 120.00, 750.00, 160.00),
('CNDV20260120000102', 1, 2, 'DC_GBT', 'IDLE', 120.00, 750.00, 160.00),
('CNDV20260120000201', 2, 1, 'AC', 'IDLE', 7.00, 220.00, 32.00),
('CNDV20260120000301', 3, 1, 'DC_GBT', 'UNAVAILABLE', 60.00, 500.00, 120.00);
