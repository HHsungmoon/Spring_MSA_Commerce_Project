-- =====================================================================
-- Schema: commerce_mono (MySQL 8.0+)
-- Charset/Collation: utf8mb4 / utf8mb4_0900_ai_ci
-- Soft Delete: deleted_at IS NULL
-- UUID v7: BINARY(16)
-- 금액은 KRW "원" 단위의 INT 저장, 할인율은 정수 % 저장
-- =====================================================================

SET NAMES utf8mb4;
DROP DATABASE IF EXISTS commerce_mono;
CREATE DATABASE commerce_mono
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;
USE commerce_mono;

SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================================
-- 1) Identity & Roles
-- =====================================================================
CREATE TABLE p_customer
(
    id           BINARY(16)                               NOT NULL,
    name         VARCHAR(100)                             NOT NULL,
    nickname     VARCHAR(100)                             NULL,
    email        VARCHAR(255)                             NOT NULL,
    password     VARCHAR(255)                             NOT NULL,
    phone_number VARCHAR(18)                              NOT NULL,
    age          SMALLINT                                 NULL,

    -- 배송지: 단일 address 필드만 사용
    address      VARCHAR(255)                             NOT NULL,

    gender       ENUM ('MALE','FEMALE','OTHER','UNKNOWN') NOT NULL DEFAULT 'UNKNOWN',
    status       ENUM ('ACTIVE','INACTIVE','BANNED')      NOT NULL DEFAULT 'ACTIVE',

    created_at   TIMESTAMP                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at   TIMESTAMP                                NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uk_customer_email (email)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE p_manager
(
    id           BINARY(16)                          NOT NULL,
    name         VARCHAR(100)                        NOT NULL,
    nickname     VARCHAR(100)                        NULL,
    email        VARCHAR(255)                        NOT NULL,
    password     VARCHAR(255)                        NOT NULL,
    phone_number VARCHAR(18)                         NULL,
    status       ENUM ('ACTIVE','INACTIVE','BANNED') NOT NULL DEFAULT 'ACTIVE',

    created_at   TIMESTAMP                           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP                           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at   TIMESTAMP                           NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uk_manager_email (email)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE p_admins
(
    id           BINARY(16)   NOT NULL,
    name         VARCHAR(100) NOT NULL,
    nickname     VARCHAR(100) NULL,
    email        VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    phone_number VARCHAR(18)  NULL,
    position     VARCHAR(50)  NULL,

    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at   TIMESTAMP    NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uk_admins_email (email)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

-- =====================================================================
-- 2) Categories
-- =====================================================================
CREATE TABLE big_categories
(
    id         BINARY(16)   NOT NULL,
    name       VARCHAR(120) NOT NULL,

    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP    NULL,

    PRIMARY KEY (id)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE small_categories
(
    id         BINARY(16)   NOT NULL,
    big_id     BINARY(16)   NOT NULL,
    name       VARCHAR(120) NOT NULL,

    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP    NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_smallcat_big
        FOREIGN KEY (big_id) REFERENCES big_categories (id)
            ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

-- =====================================================================
-- 3) Stores + Applications
-- =====================================================================
CREATE TABLE stores
(
    id              BINARY(16)                             NOT NULL,
    manager_id      BINARY(16)                             NOT NULL,
    big_category_id BINARY(16)                             NOT NULL, -- 스토어가 큰 카테고리 직접 보유
    name            VARCHAR(120)                           NOT NULL,
    description     VARCHAR(1000)                          NULL,
    status          ENUM ('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',

    created_at      TIMESTAMP                              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP                              NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_stores_manager
        FOREIGN KEY (manager_id) REFERENCES p_manager (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_stores_bigcat
        FOREIGN KEY (big_category_id) REFERENCES big_categories (id)
            ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE store_applications
(
    application_id BINARY(16)                             NOT NULL,
    manager_id     BINARY(16)                             NOT NULL,
    store_id       BINARY(16)                             NULL,
    description    VARCHAR(1000)                          NULL, -- 신청 시 소개/비고
    status         ENUM ('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
    reviewed_by    BINARY(16)                             NULL,
    reason         VARCHAR(500)                           NULL,
    requested_at   TIMESTAMP                              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reviewed_at    TIMESTAMP                              NULL,

    created_at     TIMESTAMP                              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at     TIMESTAMP                              NULL,

    PRIMARY KEY (application_id),
    CONSTRAINT fk_storeapps_manager
        FOREIGN KEY (manager_id) REFERENCES p_manager (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_storeapps_store
        FOREIGN KEY (store_id) REFERENCES stores (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_storeapps_admin
        FOREIGN KEY (reviewed_by) REFERENCES p_admins (id)
            ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

-- =====================================================================
-- 4) Catalog (Products)  — 금액/할인율 INT
-- =====================================================================
CREATE TABLE products
(
    id                BINARY(16)                         NOT NULL,
    store_id          BINARY(16)                         NOT NULL,
    small_category_id BINARY(16)                         NOT NULL,
    name              VARCHAR(255)                       NOT NULL,
    sku               VARCHAR(100)                       NOT NULL,
    summary           VARCHAR(500)                       NULL,
    description       MEDIUMTEXT                         NULL,
    image_url         VARCHAR(1024)                      NOT NULL,
    currency          CHAR(3)                            NOT NULL DEFAULT 'KRW',
    price             INT                                NOT NULL, -- KRW 원 단위
    discount_rate     INT                                NULL,     -- 정수 % (0~100)
    quantity          INT                                NOT NULL DEFAULT 0,
    status            ENUM ('DRAFT','ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',

    created_at        TIMESTAMP                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at        TIMESTAMP                          NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_products_store
        FOREIGN KEY (store_id) REFERENCES stores (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_products_smallcat
        FOREIGN KEY (small_category_id) REFERENCES small_categories (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    UNIQUE KEY uk_products_sku (sku)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

-- =====================================================================
-- 5) Cart (고객 1:1, JSON 구조)
-- =====================================================================
CREATE TABLE p_cart
(
    cart_id     BINARY(16) NOT NULL,
    customer_id BINARY(16) NOT NULL,
    items_json  JSON       NOT NULL,

    created_at  TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP  NULL,

    PRIMARY KEY (cart_id),
    CONSTRAINT fk_cart_customer
        FOREIGN KEY (customer_id) REFERENCES p_customer (id)
            ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

-- =====================================================================
-- 6) Order / Payment  — 금액 INT
-- =====================================================================
CREATE TABLE p_orders
(
    order_id             BINARY(16)                                                           NOT NULL,
    order_number         VARCHAR(60)                                                          NOT NULL,
    customer_id          BINARY(16)                                                           NOT NULL,
    order_status         ENUM ('PENDING','PAID','SHIPPING','DELIVERED','CANCELED','REFUNDED') NOT NULL DEFAULT 'PENDING',
    subtotal_amount      INT                                                                  NOT NULL DEFAULT 0, -- KRW
    shipping_fee         INT                                                                  NOT NULL DEFAULT 0, -- KRW
    discount_amount      INT                                                                  NOT NULL DEFAULT 0, -- KRW
    final_payment_amount INT                                                                  NOT NULL,           -- KRW

    -- 배송지 스냅샷
    recipient_name       VARCHAR(100)                                                         NOT NULL,
    recipient_phone      VARCHAR(30)                                                          NOT NULL,
    address              VARCHAR(255)                                                         NOT NULL,

    created_at           TIMESTAMP                                                            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP                                                            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at           TIMESTAMP                                                            NULL,

    PRIMARY KEY (order_id),
    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id) REFERENCES p_customer (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    UNIQUE KEY uk_orders_order_number (order_number)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE order_items
(
    order_item_id         BINARY(16)   NOT NULL,
    order_id              BINARY(16)   NOT NULL,
    product_id            BINARY(16)   NOT NULL,
    product_name_snapshot VARCHAR(255) NOT NULL,
    sku_snapshot          VARCHAR(100) NOT NULL,
    unit_price_snapshot   INT          NOT NULL, -- KRW
    quantity              INT          NOT NULL,
    line_amount           INT          NOT NULL, -- KRW

    created_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at            TIMESTAMP    NULL,

    PRIMARY KEY (order_item_id),
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES p_orders (order_id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_order_items_product
        FOREIGN KEY (product_id) REFERENCES products (id)
            ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE p_payments
(
    payment_id        BINARY(16)                                                            NOT NULL,
    order_id          BINARY(16)                                                            NOT NULL,
    customer_id       BINARY(16)                                                            NOT NULL,
    payment_status    ENUM ('PENDING','PAID','CANCELED','REFUNDED')                         NOT NULL DEFAULT 'PENDING',
    payment_method    ENUM ('KAKAOPAY','CARD','VA','NAVER_PAY','KAKAO_PAY','BANK_TRANSFER') NOT NULL DEFAULT 'KAKAOPAY',
    paid_amount       INT                                                                   NULL, -- KRW
    pg_transaction_id VARCHAR(120)                                                          NULL,
    approval_code     VARCHAR(60)                                                           NULL,
    payload_json      JSON                                                                  NULL,
    receipt_url       VARCHAR(512)                                                          NULL,
    requested_at      TIMESTAMP                                                             NULL,
    approved_at       TIMESTAMP                                                             NULL,
    failed_at         TIMESTAMP                                                             NULL,
    failure_reason    VARCHAR(255)                                                          NULL,

    created_at        TIMESTAMP                                                             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP                                                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at        TIMESTAMP                                                             NULL,

    PRIMARY KEY (payment_id),
    CONSTRAINT fk_payments_order
        FOREIGN KEY (order_id) REFERENCES p_orders (order_id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_payments_customer
        FOREIGN KEY (customer_id) REFERENCES p_customer (id)
            ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

-- =====================================================================
-- 미생성(간소화 유지): p_reviews, product_recommends, shipments, order_shipping_addresses, product_variants
-- =====================================================================

SET FOREIGN_KEY_CHECKS = 1;
