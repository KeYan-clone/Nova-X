-- 定价服务数据库初始化

CREATE DATABASE IF NOT EXISTS novax_pricing DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novax_pricing;

-- 定价模板表
DROP TABLE IF EXISTS `pricing_template`;
CREATE TABLE `pricing_template` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `template_name` VARCHAR(100) NOT NULL COMMENT '模板名称',
  `station_id` BIGINT COMMENT '站点ID',
  `template_type` VARCHAR(20) NOT NULL DEFAULT 'TIME_OF_USE' COMMENT '模板类型',
  `peak_price` DECIMAL(10, 4) NOT NULL COMMENT '尖峰时段电价(元/kWh)',
  `normal_price` DECIMAL(10, 4) NOT NULL COMMENT '平时段电价(元/kWh)',
  `valley_price` DECIMAL(10, 4) NOT NULL COMMENT '谷时段电价(元/kWh)',
  `service_fee` DECIMAL(10, 4) NOT NULL DEFAULT 0 COMMENT '服务费(元/kWh)',
  `parking_fee` DECIMAL(10, 4) COMMENT '停车费(元/小时)',
  `effective_date` DATE NOT NULL COMMENT '生效日期',
  `expiry_date` DATE COMMENT '失效日期',
  `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
  `remark` VARCHAR(500),
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_station_id` (`station_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定价模板表';

-- 插入默认定价
INSERT INTO `pricing_template` (`template_name`, `peak_price`, `normal_price`, `valley_price`, `service_fee`, `effective_date`, `status`)
VALUES ('通用定价', 1.2000, 0.8000, 0.4000, 0.8000, '2024-01-01', 'ACTIVE');
