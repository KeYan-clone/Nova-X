CREATE DATABASE IF NOT EXISTS novax_report DEFAULT CHARACTER SET utf8mb4;

USE novax_report;

DROP TABLE IF EXISTS `daily_statistics`;
CREATE TABLE `daily_statistics` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `statistics_date` date NOT NULL COMMENT '统计日期',
  `total_charging_count` int NOT NULL DEFAULT 0 COMMENT '充电次数',
  `total_energy` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '总电量',
  `total_duration` int NOT NULL DEFAULT 0 COMMENT '总时长',
  `total_revenue` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '总收入',
  `charging_revenue` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '充电收入',
  `service_fee_revenue` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '服务费收入',
  `parking_revenue` decimal(12,2) NOT NULL DEFAULT 0.00 COMMENT '停车费收入',
  `new_user_count` int NOT NULL DEFAULT 0 COMMENT '新增用户',
  `active_user_count` int NOT NULL DEFAULT 0 COMMENT '活跃用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_date` (`statistics_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日统计表';

INSERT INTO `daily_statistics` (`statistics_date`, `total_charging_count`, `total_energy`, `total_duration`,
                                 `total_revenue`, `charging_revenue`, `service_fee_revenue`, `parking_revenue`,
                                 `new_user_count`, `active_user_count`)
VALUES
('2026-01-18', 280, 4500.50, 8400, 8100.80, 7200.00, 720.00, 180.80, 25, 180),
('2026-01-19', 320, 5100.00, 9600, 9180.00, 8160.00, 816.00, 204.00, 30, 210),
('2026-01-20', 350, 5600.00, 10500, 10080.00, 8960.00, 896.00, 224.00, 28, 230);
