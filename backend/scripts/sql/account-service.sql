-- Account Service Tables
CREATE DATABASE IF NOT EXISTS novax_account DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novax_account;

-- User table
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT 'Username',
    password VARCHAR(255) NOT NULL COMMENT 'Encrypted password',
    phone VARCHAR(20) UNIQUE COMMENT 'Phone number',
    email VARCHAR(100) UNIQUE COMMENT 'Email',
    nickname VARCHAR(50) COMMENT 'Nickname',
    avatar_url VARCHAR(255) COMMENT 'Avatar URL',
    user_type TINYINT NOT NULL COMMENT 'User type: 1-Consumer, 2-Operator, 3-OEM, 4-Utility, 5-Admin',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1-Active, 2-Inactive, 3-Locked',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0 COMMENT 'Soft delete flag',
    INDEX idx_phone (phone),
    INDEX idx_email (email),
    INDEX idx_user_type (user_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User table';

-- Vehicle table
CREATE TABLE IF NOT EXISTS t_vehicle (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT 'User ID',
    vin VARCHAR(17) UNIQUE COMMENT 'Vehicle Identification Number',
    license_plate VARCHAR(20) COMMENT 'License plate',
    brand VARCHAR(50) COMMENT 'Vehicle brand',
    model VARCHAR(50) COMMENT 'Vehicle model',
    battery_capacity DECIMAL(10,2) COMMENT 'Battery capacity (kWh)',
    max_charging_power DECIMAL(10,2) COMMENT 'Max charging power (kW)',
    connector_type VARCHAR(20) COMMENT 'Connector type',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1-Active, 2-Inactive',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_user_id (user_id),
    INDEX idx_vin (vin)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Vehicle table';
