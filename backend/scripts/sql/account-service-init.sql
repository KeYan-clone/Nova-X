-- ==========================================
-- Nova-X 数据库初始化脚本
-- ==========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `nova_x_account` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `nova_x_account`;

-- ==========================================
-- 用户表
-- ==========================================
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
  `username` VARCHAR(50) COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（加密）',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `email` VARCHAR(100) COMMENT '邮箱',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像',
  `gender` TINYINT DEFAULT 0 COMMENT '性别（0-未知，1-男，2-女）',
  `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
  `verified` TINYINT DEFAULT 0 COMMENT '实名认证状态（0-未认证，1-已认证）',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `id_card` VARCHAR(20) COMMENT '身份证号',
  `user_type` TINYINT DEFAULT 1 COMMENT '用户类型（1-普通用户，2-运营商，3-OEM，4-电力供应商，5-管理员）',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NOT NULL COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
  `version` INT DEFAULT 0 COMMENT '版本号（乐观锁）',
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_username` (`username`),
  KEY `idx_email` (`email`),
  KEY `idx_status` (`status`),
  KEY `idx_user_type` (`user_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ==========================================
-- 用户车辆表
-- ==========================================
CREATE TABLE IF NOT EXISTS `user_vehicle` (
  `id` BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `plate_number` VARCHAR(20) NOT NULL COMMENT '车牌号',
  `brand` VARCHAR(50) COMMENT '品牌',
  `model` VARCHAR(50) COMMENT '型号',
  `battery_capacity` DECIMAL(10,2) COMMENT '电池容量（kWh）',
  `connector_type` VARCHAR(20) COMMENT '接口类型（GB/T, CCS, CHAdeMO）',
  `is_default` TINYINT DEFAULT 0 COMMENT '是否默认（0-否，1-是）',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NOT NULL COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除，1-已删除）',
  `version` INT DEFAULT 0 COMMENT '版本号（乐观锁）',
  KEY `idx_user_id` (`user_id`),
  KEY `idx_plate_number` (`plate_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户车辆表';

-- ==========================================
-- 组织表
-- ==========================================
CREATE TABLE IF NOT EXISTS `organization` (
  `id` BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
  `name` VARCHAR(100) NOT NULL COMMENT '组织名称',
  `code` VARCHAR(50) NOT NULL COMMENT '组织编码',
  `type` TINYINT NOT NULL COMMENT '组织类型（1-运营商，2-OEM，3-电力供应商）',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父组织ID',
  `level` INT DEFAULT 1 COMMENT '层级',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `contact_name` VARCHAR(50) COMMENT '联系人',
  `contact_phone` VARCHAR(20) COMMENT '联系电话',
  `contact_email` VARCHAR(100) COMMENT '联系邮箱',
  `address` VARCHAR(255) COMMENT '地址',
  `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NOT NULL COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  `version` INT DEFAULT 0 COMMENT '版本号',
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织表';

-- ==========================================
-- 角色表
-- ==========================================
CREATE TABLE IF NOT EXISTS `role` (
  `id` BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
  `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  `description` VARCHAR(255) COMMENT '角色描述',
  `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NOT NULL COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  `version` INT DEFAULT 0 COMMENT '版本号',
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ==========================================
-- 权限表
-- ==========================================
CREATE TABLE IF NOT EXISTS `permission` (
  `id` BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
  `name` VARCHAR(50) NOT NULL COMMENT '权限名称',
  `code` VARCHAR(100) NOT NULL COMMENT '权限编码',
  `type` TINYINT NOT NULL COMMENT '权限类型（1-菜单，2-按钮，3-接口）',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父权限ID',
  `path` VARCHAR(255) COMMENT '路径',
  `icon` VARCHAR(50) COMMENT '图标',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` DATETIME NOT NULL COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
  `version` INT DEFAULT 0 COMMENT '版本号',
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- ==========================================
-- 用户角色关联表
-- ==========================================
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ==========================================
-- 角色权限关联表
-- ==========================================
CREATE TABLE IF NOT EXISTS `role_permission` (
  `id` BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `permission_id` BIGINT NOT NULL COMMENT '权限ID',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- ==========================================
-- 审计日志表
-- ==========================================
CREATE TABLE IF NOT EXISTS `audit_log` (
  `id` BIGINT NOT NULL PRIMARY KEY COMMENT '主键ID',
  `user_id` BIGINT COMMENT '用户ID',
  `username` VARCHAR(50) COMMENT '用户名',
  `operation` VARCHAR(50) NOT NULL COMMENT '操作',
  `method` VARCHAR(255) COMMENT '方法',
  `params` TEXT COMMENT '参数',
  `ip` VARCHAR(50) COMMENT 'IP地址',
  `user_agent` VARCHAR(255) COMMENT 'User Agent',
  `status` TINYINT NOT NULL COMMENT '状态（0-失败，1-成功）',
  `error_msg` TEXT COMMENT '错误消息',
  `duration` BIGINT COMMENT '执行时长（ms）',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  KEY `idx_user_id` (`user_id`),
  KEY `idx_operation` (`operation`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审计日志表';

-- ==========================================
-- 初始化数据
-- ==========================================

-- 插入管理员角色
INSERT INTO `role` (`id`, `name`, `code`, `description`, `status`, `create_time`, `update_time`, `deleted`, `version`)
VALUES (1, '超级管理员', 'ADMIN', '系统超级管理员', 1, NOW(), NOW(), 0, 0);

-- 插入普通用户角色
INSERT INTO `role` (`id`, `name`, `code`, `description`, `status`, `create_time`, `update_time`, `deleted`, `version`)
VALUES (2, '普通用户', 'USER', '普通用户', 1, NOW(), NOW(), 0, 0);

-- 插入运营商角色
INSERT INTO `role` (`id`, `name`, `code`, `description`, `status`, `create_time`, `update_time`, `deleted`, `version`)
VALUES (3, '运营商', 'OPERATOR', '运营商', 1, NOW(), NOW(), 0, 0);
