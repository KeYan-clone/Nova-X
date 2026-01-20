-- Station Service Tables
CREATE DATABASE IF NOT EXISTS novax_station DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novax_station;

-- Charging station table
CREATE TABLE IF NOT EXISTS t_station (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    station_code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Station code',
    station_name VARCHAR(100) NOT NULL COMMENT 'Station name',
    operator_id BIGINT NOT NULL COMMENT 'Operator ID',
    address VARCHAR(255) COMMENT 'Address',
    latitude DECIMAL(10,6) COMMENT 'Latitude',
    longitude DECIMAL(10,6) COMMENT 'Longitude',
    total_chargers INT DEFAULT 0 COMMENT 'Total number of chargers',
    available_chargers INT DEFAULT 0 COMMENT 'Available chargers',
    station_type TINYINT COMMENT 'Station type: 1-Public, 2-Private, 3-Shared',
    power_capacity DECIMAL(10,2) COMMENT 'Total power capacity (kW)',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1-Active, 2-Maintenance, 3-Offline',
    open_time TIME COMMENT 'Opening time',
    close_time TIME COMMENT 'Closing time',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_operator_id (operator_id),
    INDEX idx_station_code (station_code),
    INDEX idx_location (latitude, longitude)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Charging station table';

-- Charger table
CREATE TABLE IF NOT EXISTS t_charger (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    charger_code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Charger code',
    station_id BIGINT NOT NULL COMMENT 'Station ID',
    charger_name VARCHAR(100) COMMENT 'Charger name',
    charger_type TINYINT COMMENT 'Charger type: 1-AC, 2-DC',
    max_power DECIMAL(10,2) COMMENT 'Max power (kW)',
    manufacturer VARCHAR(50) COMMENT 'Manufacturer',
    model VARCHAR(50) COMMENT 'Model',
    protocol VARCHAR(20) COMMENT 'Protocol: OCPP1.6, OCPP2.0.1',
    connector_count INT DEFAULT 1 COMMENT 'Number of connectors',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1-Available, 2-Charging, 3-Faulted, 4-Unavailable',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_station_id (station_id),
    INDEX idx_charger_code (charger_code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Charger table';

-- Connector table
CREATE TABLE IF NOT EXISTS t_connector (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    charger_id BIGINT NOT NULL COMMENT 'Charger ID',
    connector_no INT NOT NULL COMMENT 'Connector number',
    connector_type VARCHAR(20) COMMENT 'Connector type: CCS, CHAdeMO, Type2',
    max_power DECIMAL(10,2) COMMENT 'Max power (kW)',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1-Available, 2-Charging, 3-Faulted, 4-Reserved',
    current_session_id BIGINT COMMENT 'Current charging session ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    UNIQUE KEY uk_charger_connector (charger_id, connector_no),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Connector table';
