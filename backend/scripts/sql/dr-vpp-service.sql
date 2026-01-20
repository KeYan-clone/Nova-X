-- DR & VPP Service Tables
CREATE DATABASE IF NOT EXISTS novax_dr_vpp DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novax_dr_vpp;

-- DR order table
CREATE TABLE IF NOT EXISTS t_dr_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT 'Order number',
    utility_id BIGINT NOT NULL COMMENT 'Utility company ID',
    order_type TINYINT COMMENT 'Order type: 1-Peak shaving, 2-Valley filling',
    target_power DECIMAL(10,2) COMMENT 'Target power (kW)',
    start_time DATETIME NOT NULL COMMENT 'Start time',
    end_time DATETIME NOT NULL COMMENT 'End time',
    price DECIMAL(10,4) COMMENT 'Price (per kWh)',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1-Published, 2-Executing, 3-Completed, 4-Cancelled',
    actual_power DECIMAL(10,2) COMMENT 'Actual power (kW)',
    actual_energy DECIMAL(10,3) COMMENT 'Actual energy (kWh)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_utility_id (utility_id),
    INDEX idx_order_no (order_no),
    INDEX idx_start_time (start_time),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='DR order table';

-- VPP resource table
CREATE TABLE IF NOT EXISTS t_vpp_resource (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resource_type TINYINT COMMENT 'Resource type: 1-Charging station, 2-V2G vehicle',
    resource_id BIGINT NOT NULL COMMENT 'Resource ID',
    operator_id BIGINT NOT NULL COMMENT 'Operator ID',
    available_capacity DECIMAL(10,2) COMMENT 'Available capacity (kW)',
    min_power DECIMAL(10,2) COMMENT 'Min power (kW)',
    max_power DECIMAL(10,2) COMMENT 'Max power (kW)',
    response_time INT COMMENT 'Response time (seconds)',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1-Available, 2-Busy, 3-Offline',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_resource_id (resource_id),
    INDEX idx_operator_id (operator_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='VPP resource table';
