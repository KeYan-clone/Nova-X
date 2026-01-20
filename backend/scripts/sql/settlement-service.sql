-- Settlement Service Tables
CREATE DATABASE IF NOT EXISTS novax_settlement DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novax_settlement;

-- Settlement order table
CREATE TABLE IF NOT EXISTS t_settlement_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT 'Order number',
    operator_id BIGINT NOT NULL COMMENT 'Operator ID',
    settlement_period VARCHAR(20) NOT NULL COMMENT 'Settlement period: YYYY-MM',
    total_sessions INT DEFAULT 0 COMMENT 'Total sessions',
    total_energy DECIMAL(10,3) DEFAULT 0 COMMENT 'Total energy (kWh)',
    total_amount DECIMAL(12,2) DEFAULT 0 COMMENT 'Total amount',
    platform_fee DECIMAL(12,2) DEFAULT 0 COMMENT 'Platform fee',
    settlement_amount DECIMAL(12,2) DEFAULT 0 COMMENT 'Settlement amount',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1-Pending, 2-Confirmed, 3-Paid',
    settlement_time DATETIME COMMENT 'Settlement time',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_operator_id (operator_id),
    INDEX idx_order_no (order_no),
    INDEX idx_settlement_period (settlement_period),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Settlement order table';

-- Payment record table
CREATE TABLE IF NOT EXISTS t_payment_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    payment_no VARCHAR(50) NOT NULL UNIQUE COMMENT 'Payment number',
    order_type TINYINT COMMENT 'Order type: 1-Session, 2-Settlement',
    order_id BIGINT NOT NULL COMMENT 'Order ID',
    user_id BIGINT NOT NULL COMMENT 'User ID',
    payment_method TINYINT COMMENT 'Payment method: 1-WeChat, 2-Alipay, 3-Card, 4-Wallet',
    amount DECIMAL(12,2) NOT NULL COMMENT 'Amount',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1-Pending, 2-Success, 3-Failed, 4-Refunded',
    transaction_id VARCHAR(100) COMMENT 'Third-party transaction ID',
    payment_time DATETIME COMMENT 'Payment time',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_order_id (order_id),
    INDEX idx_user_id (user_id),
    INDEX idx_payment_no (payment_no),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Payment record table';
