CREATE DATABASE IF NOT EXISTS novax_scheduling DEFAULT CHARACTER SET utf8mb4;

USE novax_scheduling;

DROP TABLE IF EXISTS `charging_reservation`;
CREATE TABLE `charging_reservation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reservation_no` varchar(32) NOT NULL COMMENT '预约编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `station_id` bigint NOT NULL COMMENT '充电站ID',
  `station_name` varchar(100) DEFAULT NULL COMMENT '充电站名称',
  `device_id` bigint DEFAULT NULL COMMENT '设备ID',
  `device_code` varchar(32) DEFAULT NULL COMMENT '设备编号',
  `reservation_start_time` datetime NOT NULL COMMENT '预约开始时间',
  `reservation_end_time` datetime NOT NULL COMMENT '预约结束时间',
  `estimated_energy` decimal(10,2) DEFAULT NULL COMMENT '预计充电电量',
  `reservation_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '预约状态',
  `session_id` bigint DEFAULT NULL COMMENT '实际使用的会话ID',
  `cancel_reason` varchar(500) DEFAULT NULL COMMENT '取消原因',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_reservation_no` (`reservation_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_station_id` (`station_id`),
  KEY `idx_device_id` (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充电预约表';

INSERT INTO `charging_reservation` (`reservation_no`, `user_id`, `station_id`, `station_name`, `device_id`, `device_code`,
                                    `reservation_start_time`, `reservation_end_time`, `estimated_energy`, `reservation_status`)
VALUES
('RS20260120140001', 1001, 1, '市中心充电站', 1, 'DV20260120001',
 '2026-01-21 09:00:00', '2026-01-21 11:00:00', 50.00, 'CONFIRMED'),

('RS20260120150002', 1002, 2, '商业区充电站', 5, 'DV20260120005',
 '2026-01-21 14:00:00', '2026-01-21 16:00:00', 40.00, 'CONFIRMED'),

('RS20260120160003', 1003, 1, '市中心充电站', 2, 'DV20260120002',
 '2026-01-20 18:00:00', '2026-01-20 20:00:00', 35.00, 'COMPLETED');
