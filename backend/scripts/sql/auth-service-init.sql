-- 认证服务数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS novax_auth DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novax_auth;

-- 登录日志表
CREATE TABLE IF NOT EXISTS `login_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT COMMENT '用户ID',
    `username` VARCHAR(64) COMMENT '用户名',
    `login_type` VARCHAR(20) NOT NULL COMMENT '登录类型：password-密码登录, sms-短信登录, oauth-第三方登录',
    `login_ip` VARCHAR(50) COMMENT '登录IP',
    `login_location` VARCHAR(100) COMMENT '登录地点',
    `browser` VARCHAR(50) COMMENT '浏览器类型',
    `os` VARCHAR(50) COMMENT '操作系统',
    `login_status` VARCHAR(20) NOT NULL COMMENT '登录状态：success-成功, failed-失败',
    `failure_reason` VARCHAR(200) COMMENT '失败原因',
    `login_time` DATETIME NOT NULL COMMENT '登录时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标识：0-未删除, 1-已删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_username` (`username`),
    INDEX `idx_login_time` (`login_time`),
    INDEX `idx_login_status` (`login_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

-- OAuth2客户端表
CREATE TABLE IF NOT EXISTS `oauth_client` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `client_id` VARCHAR(100) NOT NULL COMMENT '客户端ID',
    `client_secret` VARCHAR(200) NOT NULL COMMENT '客户端密钥',
    `client_name` VARCHAR(100) NOT NULL COMMENT '客户端名称',
    `grant_types` VARCHAR(200) NOT NULL COMMENT '授权类型：authorization_code,password,client_credentials,refresh_token',
    `redirect_uris` TEXT COMMENT '回调地址，多个用逗号分隔',
    `scopes` VARCHAR(200) COMMENT '授权范围：read,write,all',
    `access_token_validity` INT NOT NULL DEFAULT 7200 COMMENT '访问令牌有效期(秒)',
    `refresh_token_validity` INT NOT NULL DEFAULT 604800 COMMENT '刷新令牌有效期(秒)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用, 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '删除标识：0-未删除, 1-已删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_client_id` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='OAuth2客户端表';

-- 插入默认OAuth2客户端
INSERT INTO `oauth_client` (`client_id`, `client_secret`, `client_name`, `grant_types`, `scopes`)
VALUES
('web-client', '$2a$10$8qXN.QvOZKRU3eY7I0K8YOyGZv3SvR5y7XqZnXqJ5Y3Y3Y3Y3Y3Y3', 'Web客户端', 'password,refresh_token', 'all'),
('mobile-client', '$2a$10$8qXN.QvOZKRU3eY7I0K8YOyGZv3SvR5y7XqZnXqJ5Y3Y3Y3Y3Y3Y3', '移动端客户端', 'password,sms,refresh_token', 'all');
