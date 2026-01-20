CREATE DATABASE IF NOT EXISTS novax_workorder DEFAULT CHARACTER SET utf8mb4;

USE novax_workorder;

DROP TABLE IF EXISTS `work_order`;
CREATE TABLE `work_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) NOT NULL COMMENT '工单编号',
  `order_type` varchar(20) NOT NULL COMMENT '工单类型',
  `device_id` bigint NOT NULL COMMENT '设备ID',
  `device_code` varchar(32) DEFAULT NULL COMMENT '设备编号',
  `station_id` bigint DEFAULT NULL COMMENT '充电站ID',
  `station_name` varchar(100) DEFAULT NULL COMMENT '充电站名称',
  `fault_description` text COMMENT '故障描述',
  `priority` varchar(20) NOT NULL DEFAULT 'MEDIUM' COMMENT '优先级',
  `order_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '工单状态',
  `reporter_id` bigint DEFAULT NULL COMMENT '报修人ID',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
  `assign_time` datetime DEFAULT NULL COMMENT '分配时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始处理时间',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `handle_result` text COMMENT '处理结果',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_status` (`order_status`),
  KEY `idx_handler_id` (`handler_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单表';

INSERT INTO `work_order` (`order_no`, `order_type`, `device_id`, `device_code`, `station_id`, `station_name`,
                          `fault_description`, `priority`, `order_status`, `reporter_id`, `handler_id`, `handler_name`,
                          `assign_time`, `start_time`, `complete_time`, `handle_result`)
VALUES
('WO20260120100001', 'FAULT', 1, 'DV20260120001', 1, '市中心充电站',
 '设备无法启动充电', 'HIGH', 'COMPLETED', 1001, 2001, '维修员2001',
 '2026-01-20 10:05:00', '2026-01-20 10:15:00', '2026-01-20 11:30:00', '已更换继电器，测试正常'),

('WO20260120110002', 'MAINTENANCE', 2, 'DV20260120002', 1, '市中心充电站',
 '定期维护保养', 'MEDIUM', 'IN_PROGRESS', 1002, 2002, '维修员2002',
 '2026-01-20 11:10:00', '2026-01-20 14:00:00', NULL, NULL),

('WO20260120120003', 'FAULT', 5, 'DV20260120005', 2, '商业区充电站',
 '充电枪接触不良', 'URGENT', 'PENDING', 1005, NULL, NULL,
 NULL, NULL, NULL, NULL);
