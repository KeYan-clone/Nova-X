-- 充电站服务数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS novax_station DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novax_station;

-- 充电站表
CREATE TABLE IF NOT EXISTS `station` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `station_code` VARCHAR(32) NOT NULL COMMENT '站点编号',
    `station_name` VARCHAR(100) NOT NULL COMMENT '站点名称',
    `operator_id` BIGINT NOT NULL COMMENT '运营商ID',
    `operator_name` VARCHAR(100) NOT NULL COMMENT '运营商名称',
    `station_type` VARCHAR(20) NOT NULL COMMENT '站点类型：PUBLIC-公共站, PRIVATE-专用站, BUS-公交站, TAXI-出租车站',
    `station_status` VARCHAR(20) NOT NULL DEFAULT 'NORMAL' COMMENT '站点状态：NORMAL-正常, MAINTENANCE-维护中, OFFLINE-离线',
    `province` VARCHAR(50) NOT NULL COMMENT '省份',
    `city` VARCHAR(50) NOT NULL COMMENT '城市',
    `district` VARCHAR(50) NOT NULL COMMENT '区县',
    `address` VARCHAR(200) NOT NULL COMMENT '详细地址',
    `longitude` DECIMAL(10, 6) NOT NULL COMMENT '经度',
    `latitude` DECIMAL(10, 6) NOT NULL COMMENT '纬度',
    `contact_person` VARCHAR(50) COMMENT '联系人',
    `contact_phone` VARCHAR(20) COMMENT '联系电话',
    `total_piles` INT NOT NULL DEFAULT 0 COMMENT '总桩数',
    `available_piles` INT NOT NULL DEFAULT 0 COMMENT '可用桩数',
    `total_connectors` INT NOT NULL DEFAULT 0 COMMENT '总枪数',
    `available_connectors` INT NOT NULL DEFAULT 0 COMMENT '可用枪数',
    `business_start_time` VARCHAR(10) COMMENT '营业开始时间',
    `business_end_time` VARCHAR(10) COMMENT '营业结束时间',
    `is_24_hours` TINYINT NOT NULL DEFAULT 0 COMMENT '是否24小时营业',
    `parking_fee` VARCHAR(200) COMMENT '停车费说明',
    `station_images` TEXT COMMENT '站点图片',
    `description` VARCHAR(500) COMMENT '站点描述',
    `facilities` VARCHAR(200) COMMENT '设施说明',
    `rating` DECIMAL(3, 2) NOT NULL DEFAULT 5.00 COMMENT '评分',
    `review_count` INT NOT NULL DEFAULT 0 COMMENT '评价数量',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标识',
    `version` INT NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_station_code` (`station_code`),
    INDEX `idx_operator_id` (`operator_id`),
    INDEX `idx_station_type` (`station_type`),
    INDEX `idx_station_status` (`station_status`),
    INDEX `idx_city` (`city`),
    INDEX `idx_location` (`longitude`, `latitude`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充电站表';

-- 插入示例数据
INSERT INTO `station` (`station_code`, `station_name`, `operator_id`, `operator_name`, `station_type`,
    `province`, `city`, `district`, `address`, `longitude`, `latitude`,
    `contact_person`, `contact_phone`, `business_start_time`, `business_end_time`, `is_24_hours`,
    `parking_fee`, `description`, `facilities`, `total_piles`, `available_piles`,
    `total_connectors`, `available_connectors`)
VALUES
('ST202601200001', '中关村充电站', 1, '国网电动', 'PUBLIC',
    '北京市', '北京市', '海淀区', '中关村大街1号', 116.319802, 39.983405,
    '张三', '13800138000', '00:00', '23:59', 1,
    '前2小时免费，之后5元/小时', '位于中关村核心区域，交通便利', '有卫生间、休息室、便利店',
    20, 15, 40, 30),

('ST202601200002', '奥林匹克公园充电站', 1, '国网电动', 'PUBLIC',
    '北京市', '北京市', '朝阳区', '北辰西路奥林匹克公园', 116.403119, 39.993894,
    '李四', '13900139000', '06:00', '22:00', 0,
    '10元/小时', '奥林匹克公园配套充电站', '有卫生间、休息区',
    15, 12, 30, 25),

('ST202601200003', '首都机场充电站', 2, '特来电', 'PUBLIC',
    '北京市', '北京市', '顺义区', '首都机场T3航站楼停车场', 116.603094, 40.048900,
    '王五', '13700137000', '00:00', '23:59', 1,
    '按机场停车收费标准', '机场配套充电设施，24小时服务', '有卫生间、餐饮',
    25, 20, 50, 40);
