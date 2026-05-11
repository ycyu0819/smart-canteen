-- ============================================
-- Smart Canteen - All Service Databases Schema
-- ============================================

-- User Service Database
CREATE DATABASE IF NOT EXISTS canteen_user DEFAULT CHARACTER SET utf8mb4;
USE canteen_user;

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(32) UNIQUE COMMENT '学工号',
    phone VARCHAR(16) UNIQUE NOT NULL COMMENT '手机号',
    password_hash VARCHAR(128) NOT NULL,
    nickname VARCHAR(64),
    avatar_url VARCHAR(256),
    role VARCHAR(16) NOT NULL DEFAULT 'CONSUMER' COMMENT 'CONSUMER/MERCHANT/ADMIN',
    status VARCHAR(16) NOT NULL DEFAULT 'NORMAL' COMMENT 'NORMAL/DISABLED',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_phone (phone),
    INDEX idx_student_id (student_id),
    INDEX idx_role (role)
) ENGINE=InnoDB;

CREATE TABLE refresh_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(256) NOT NULL,
    expires_at DATETIME NOT NULL,
    revoked TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_token (token)
) ENGINE=InnoDB;

-- Dish Service Database
CREATE DATABASE IF NOT EXISTS canteen_dish DEFAULT CHARACTER SET utf8mb4;
USE canteen_dish;

CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id BIGINT NOT NULL,
    name VARCHAR(64) NOT NULL,
    sort_order INT DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_merchant (merchant_id)
) ENGINE=InnoDB;

CREATE TABLE dish (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id BIGINT NOT NULL,
    category_id BIGINT,
    name VARCHAR(128) NOT NULL,
    image_url VARCHAR(256),
    description VARCHAR(512),
    price INT NOT NULL COMMENT '单位:分',
    stock INT NOT NULL DEFAULT 0,
    alert_threshold INT NOT NULL DEFAULT 10,
    status VARCHAR(16) NOT NULL DEFAULT 'OFF_SHELF' COMMENT 'ON_SHELF/OFF_SHELF',
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_merchant (merchant_id),
    INDEX idx_status (status),
    INDEX idx_category (category_id)
) ENGINE=InnoDB;

CREATE TABLE selling_slot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dish_id BIGINT NOT NULL,
    slot_type VARCHAR(16) NOT NULL COMMENT 'BREAKFAST/LUNCH/DINNER/CUSTOM',
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    is_active TINYINT NOT NULL DEFAULT 1,
    INDEX idx_dish_id (dish_id)
) ENGINE=InnoDB;

CREATE TABLE stock_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dish_id BIGINT NOT NULL,
    change_type VARCHAR(16) NOT NULL COMMENT 'DEDUCT/RESTORE/MANUAL',
    delta INT NOT NULL,
    before_stock INT NOT NULL,
    after_stock INT NOT NULL,
    related_order_id BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_dish_id (dish_id)
) ENGINE=InnoDB;

-- Order Service Database
CREATE DATABASE IF NOT EXISTS canteen_order DEFAULT CHARACTER SET utf8mb4;
USE canteen_order;

CREATE TABLE order_tbl (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no VARCHAR(32) UNIQUE NOT NULL COMMENT '订单编号',
    user_id BIGINT NOT NULL,
    merchant_id BIGINT NOT NULL,
    status VARCHAR(16) NOT NULL COMMENT 'PLACED/ACCEPTED/PREPARING/WAITING/PICKED_UP/CANCELLED',
    total_price INT NOT NULL COMMENT '单位:分',
    pickup_number VARCHAR(16),
    pickup_code VARCHAR(8),
    cancel_reason VARCHAR(128),
    cancel_time DATETIME,
    paid_at DATETIME,
    accepted_at DATETIME,
    prepared_at DATETIME,
    completed_at DATETIME,
    cancelled_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user (user_id),
    INDEX idx_merchant (merchant_id),
    INDEX idx_status (status),
    INDEX idx_order_no (order_no)
) ENGINE=InnoDB;

CREATE TABLE order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    dish_id BIGINT NOT NULL,
    dish_name VARCHAR(128) NOT NULL,
    dish_image VARCHAR(256),
    unit_price INT NOT NULL,
    quantity INT NOT NULL,
    subtotal INT NOT NULL,
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB;

CREATE TABLE order_status_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    from_status VARCHAR(16),
    to_status VARCHAR(16) NOT NULL,
    operator_id BIGINT,
    operator_type VARCHAR(16) COMMENT 'USER/MERCHANT/SYSTEM',
    remark VARCHAR(256),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB;

-- Queue Service Database
CREATE DATABASE IF NOT EXISTS canteen_queue DEFAULT CHARACTER SET utf8mb4;
USE canteen_queue;

CREATE TABLE pickup_queue (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    window_id BIGINT NOT NULL,
    pickup_number VARCHAR(16),
    pickup_code VARCHAR(8),
    queue_status VARCHAR(16) NOT NULL COMMENT 'WAITING/CALLING/PICKED_UP/EXPIRED',
    enqueue_time DATETIME NOT NULL,
    call_count INT DEFAULT 0,
    last_call_time DATETIME,
    picked_up_time DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_window_status (window_id, queue_status),
    INDEX idx_order_id (order_id)
) ENGINE=InnoDB;

CREATE TABLE calling_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    window_id BIGINT NOT NULL,
    pickup_number VARCHAR(16),
    call_time DATETIME NOT NULL,
    is_repeat TINYINT NOT NULL DEFAULT 0,
    INDEX idx_window (window_id),
    INDEX idx_call_time (call_time)
) ENGINE=InnoDB;
