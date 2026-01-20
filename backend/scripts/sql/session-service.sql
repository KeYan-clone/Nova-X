-- Session Service Tables
CREATE DATABASE IF NOT EXISTS novax_session DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE novax_session;

-- Charging session table
CREATE TABLE IF NOT EXISTS t_charging_session (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id VARCHAR(50) NOT NULL UNIQUE COMMENT 'Session ID',
    user_id BIGINT NOT NULL COMMENT 'User ID',
    vehicle_id BIGINT COMMENT 'Vehicle ID',
    station_id BIGINT NOT NULL COMMENT 'Station ID',
    charger_id BIGINT NOT NULL COMMENT 'Charger ID',
    connector_id BIGINT NOT NULL COMMENT 'Connector ID',
    start_time DATETIME NOT NULL COMMENT 'Start time',
    end_time DATETIME COMMENT 'End time',
    start_soc DECIMAL(5,2) COMMENT 'Start SOC (%)',
    end_soc DECIMAL(5,2) COMMENT 'End SOC (%)',
    total_energy DECIMAL(10,3) COMMENT 'Total energy (kWh)',
    total_duration INT COMMENT 'Total duration (minutes)',
    status TINYINT DEFAULT 1 COMMENT 'Status: 1-Preparing, 2-Charging, 3-Suspended, 4-Completed, 5-Faulted',
    stop_reason VARCHAR(50) COMMENT 'Stop reason',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_user_id (user_id),
    INDEX idx_station_id (station_id),
    INDEX idx_charger_id (charger_id),
    INDEX idx_session_id (session_id),
    INDEX idx_status (status),
    INDEX idx_start_time (start_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Charging session table';

-- Session meter value table (time-series data)
CREATE TABLE IF NOT EXISTS t_session_meter_value (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    session_id BIGINT NOT NULL COMMENT 'Session ID',
    timestamp DATETIME NOT NULL COMMENT 'Timestamp',
    current DECIMAL(10,3) COMMENT 'Current (A)',
    voltage DECIMAL(10,3) COMMENT 'Voltage (V)',
    power DECIMAL(10,3) COMMENT 'Power (kW)',
    energy DECIMAL(10,3) COMMENT 'Cumulative energy (kWh)',
    soc DECIMAL(5,2) COMMENT 'SOC (%)',
    temperature DECIMAL(5,2) COMMENT 'Temperature (°C)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_session_id (session_id),
    INDEX idx_timestamp (timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Session meter value table';
