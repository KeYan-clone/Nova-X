-- Billing Service Tables
CREATE DATABASE IF NOT EXISTS novax_billing DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novax_billing;

-- Pricing rule table
CREATE TABLE IF NOT EXISTS t_pricing_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rule_name VARCHAR(100) NOT NULL COMMENT 'Rule name',
    operator_id BIGINT NOT NULL COMMENT 'Operator ID',
    station_id BIGINT COMMENT 'Station ID (NULL for default)',
    rule_type TINYINT COMMENT 'Rule type: 1-TOU, 2-Flat, 3-Dynamic',
    base_price DECIMAL(10,4) COMMENT 'Base price (per kWh)',
    service_fee DECIMAL(10,4) COMMENT 'Service fee (per kWh)',
    valid_from DATETIME NOT NULL COMMENT 'Valid from',
    valid_to DATETIME COMMENT 'Valid to',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1-Active, 2-Inactive',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_operator_id (operator_id),
    INDEX idx_station_id (station_id),
    INDEX idx_valid_from (valid_from)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Pricing rule table';

-- Billing item table
CREATE TABLE IF NOT EXISTS t_billing_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id BIGINT NOT NULL COMMENT 'Session ID',
    user_id BIGINT NOT NULL COMMENT 'User ID',
    item_type TINYINT COMMENT 'Item type: 1-Energy, 2-Service, 3-Parking',
    quantity DECIMAL(10,3) COMMENT 'Quantity',
    unit_price DECIMAL(10,4) COMMENT 'Unit price',
    amount DECIMAL(10,2) COMMENT 'Amount',
    start_time DATETIME COMMENT 'Start time',
    end_time DATETIME COMMENT 'End time',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_session_id (session_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Billing item table';
