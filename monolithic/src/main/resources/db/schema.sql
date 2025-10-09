-- =====================================================================
-- Schema: commerce_mono (MySQL 8.0+)
-- Charset/Collation: utf8mb4 / utf8mb4_0900_ai_ci
-- Soft Delete 전략: 실제 DELETE 금지 (deleted_at IS NULL 조건으로 조회)
-- UUID v7 저장: BINARY(16)
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
    phone_number VARCHAR(18)                              NULL,
    age          SMALLINT                                 NULL,
    gender       ENUM ('MALE','FEMALE','OTHER','UNKNOWN') NOT NULL DEFAULT 'UNKNOWN',
    status       ENUM ('ACTIVE','INACTIVE','BANNED')      NOT NULL DEFAULT 'ACTIVE',

    -- Auditing (inline)
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

    -- Auditing (inline)
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

    -- Auditing (inline)
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at   TIMESTAMP    NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uk_admins_email (email)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE p_address
(
    id              BINARY(16)   NOT NULL,
    customer_id     BINARY(16)   NOT NULL,
    recipient_name  VARCHAR(100) NOT NULL,
    recipient_phone VARCHAR(30)  NOT NULL,
    zipcode         VARCHAR(20)  NOT NULL,
    addr1           VARCHAR(255) NOT NULL,
    addr2           VARCHAR(255) NULL,
    city            VARCHAR(100) NULL,
    state           VARCHAR(100) NULL,
    country_code    CHAR(2)      NOT NULL DEFAULT 'KR',
    is_default      TINYINT(1)   NOT NULL DEFAULT 0,

    -- Auditing (inline)
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP    NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_address_customer
        FOREIGN KEY (customer_id) REFERENCES p_customer (id)
            ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

-- =====================================================================
-- 2) Categories
-- =====================================================================
CREATE TABLE big_categories
(
    id         BINARY(16)   NOT NULL,
    name       VARCHAR(120) NOT NULL,
    slug       VARCHAR(140) NOT NULL,

    -- Auditing (inline)
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP    NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uk_bigcat_slug (slug)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE small_categories
(
    id         BINARY(16)   NOT NULL,
    big_id     BINARY(16)   NOT NULL,
    name       VARCHAR(120) NOT NULL,
    slug       VARCHAR(140) NOT NULL,

    -- Auditing (inline)
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP    NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_smallcat_big
        FOREIGN KEY (big_id) REFERENCES big_categories (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    UNIQUE KEY uk_smallcat_slug (slug)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

-- =====================================================================
-- 3) Stores + Applications
-- =====================================================================
CREATE TABLE stores
(
    id         BINARY(16)                             NOT NULL,
    manager_id BINARY(16)                             NOT NULL,
    name       VARCHAR(120)                           NOT NULL,
    slug       VARCHAR(140)                           NOT NULL,
    status     ENUM ('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',

    -- Auditing (inline)
    created_at TIMESTAMP                              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP                              NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_stores_manager
        FOREIGN KEY (manager_id) REFERENCES p_manager (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    UNIQUE KEY uk_stores_slug (slug)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE store_applications
(
    application_id BINARY(16)                             NOT NULL,
    manager_id     BINARY(16)                             NOT NULL,
    store_id       BINARY(16)                             NULL,
    status         ENUM ('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
    reviewed_by    BINARY(16)                             NULL,
    reason         VARCHAR(500)                           NULL,
    requested_at   TIMESTAMP                              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reviewed_at    TIMESTAMP                              NULL,

    -- Auditing (inline)
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
-- 4) Catalog (Products / Variants)
-- =====================================================================
CREATE TABLE products
(
    id                BINARY(16)                         NOT NULL,
    store_id          BINARY(16)                         NOT NULL,
    small_category_id BINARY(16)                         NOT NULL,
    name              VARCHAR(255)                       NOT NULL,
    slug              VARCHAR(280)                       NOT NULL, -- SEO/URL Key
    summary           VARCHAR(500)                       NULL,
    description       MEDIUMTEXT                         NULL,
    image_url         VARCHAR(1024)                      NOT NULL,
    status            ENUM ('DRAFT','ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',

    -- Auditing (inline)
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
    UNIQUE KEY uk_products_slug (slug)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE product_variants
(
    id              BINARY(16)                 NOT NULL,
    product_id      BINARY(16)                 NOT NULL,
    sku             VARCHAR(100)               NOT NULL,
    currency        CHAR(3)                    NOT NULL DEFAULT 'KRW',
    price           DECIMAL(18, 2)             NOT NULL,
    list_price      DECIMAL(18, 2)             NULL,
    quantity        INT                        NOT NULL DEFAULT 0,
    attributes_json JSON                       NULL,
    status          ENUM ('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',

    -- Auditing (inline)
    created_at      TIMESTAMP                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP                  NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_variants_product
        FOREIGN KEY (product_id) REFERENCES products (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    UNIQUE KEY uk_variants_sku (sku)
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

    -- Auditing (inline)
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
-- 6) Order / Payment / Shipping
-- =====================================================================
CREATE TABLE p_orders
(
    order_id             BINARY(16)                                                           NOT NULL,
    order_number         VARCHAR(60)                                                          NOT NULL,
    customer_id          BINARY(16)                                                           NOT NULL,
    order_status         ENUM ('PENDING','PAID','SHIPPING','DELIVERED','CANCELED','REFUNDED') NOT NULL DEFAULT 'PENDING',
    order_type           ENUM ('GENERAL')                                                     NOT NULL DEFAULT 'GENERAL',
    subtotal_amount      DECIMAL(18, 2)                                                       NOT NULL DEFAULT 0,
    shipping_fee         DECIMAL(18, 2)                                                       NOT NULL DEFAULT 0,
    discount_amount      DECIMAL(18, 2)                                                       NOT NULL DEFAULT 0,
    final_payment_amount DECIMAL(18, 2)                                                       NOT NULL,

    -- Auditing (inline)
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
    order_item_id         BINARY(16)     NOT NULL,
    order_id              BINARY(16)     NOT NULL,
    product_id            BINARY(16)     NOT NULL,
    variant_id            BINARY(16)     NOT NULL,
    product_name_snapshot VARCHAR(255)   NOT NULL,
    sku_snapshot          VARCHAR(100)   NOT NULL,
    unit_price_snapshot   DECIMAL(18, 2) NOT NULL,
    qty                   INT            NOT NULL,
    line_amount           DECIMAL(18, 2) NOT NULL,

    -- Auditing (inline)
    created_at            TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at            TIMESTAMP      NULL,

    PRIMARY KEY (order_item_id),
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES p_orders (order_id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_order_items_product
        FOREIGN KEY (product_id) REFERENCES products (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_order_items_variant
        FOREIGN KEY (variant_id) REFERENCES product_variants (id)
            ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE order_shipping_addresses
(
    id              BINARY(16)   NOT NULL,
    order_id        BINARY(16)   NOT NULL,
    recipient_name  VARCHAR(100) NOT NULL,
    recipient_phone VARCHAR(30)  NOT NULL,
    zipcode         VARCHAR(20)  NOT NULL,
    addr1           VARCHAR(255) NOT NULL,
    addr2           VARCHAR(255) NULL,
    city            VARCHAR(100) NULL,
    state           VARCHAR(100) NULL,
    country_code    CHAR(2)      NOT NULL,

    -- Auditing (inline)
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP    NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_ordaddr_order
        FOREIGN KEY (order_id) REFERENCES p_orders (order_id)
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
    paid_amount       DECIMAL(18, 2)                                                        NULL,
    pg_transaction_id VARCHAR(120)                                                          NULL,
    approval_code     VARCHAR(60)                                                           NULL,
    payload_json      JSON                                                                  NULL,
    receipt_url       VARCHAR(512)                                                          NULL,
    requested_at      TIMESTAMP                                                             NULL,
    approved_at       TIMESTAMP                                                             NULL,
    failed_at         TIMESTAMP                                                             NULL,
    failure_reason    VARCHAR(255)                                                          NULL,

    -- Auditing (inline)
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

CREATE TABLE shipments
(
    shipment_id     BINARY(16)                              NOT NULL,
    order_id        BINARY(16)                              NOT NULL,
    status          ENUM ('READY','IN_TRANSIT','DELIVERED') NOT NULL DEFAULT 'READY',
    carrier         VARCHAR(80)                             NULL,
    tracking_number VARCHAR(120)                            NULL,
    shipped_at      TIMESTAMP                               NULL,
    delivered_at    TIMESTAMP                               NULL,

    -- Auditing (inline)
    created_at      TIMESTAMP                               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at      TIMESTAMP                               NULL,

    PRIMARY KEY (shipment_id),
    CONSTRAINT fk_shipments_order
        FOREIGN KEY (order_id) REFERENCES p_orders (order_id)
            ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

-- =====================================================================
-- 7) Reviews & Recommend/Not-Recommend
-- =====================================================================
CREATE TABLE p_reviews
(
    review_id   BINARY(16)    NOT NULL,
    order_id    BINARY(16)    NOT NULL,
    customer_id BINARY(16)    NOT NULL,
    product_id  BINARY(16)    NOT NULL,
    variant_id  BINARY(16)    NULL,
    rating      DECIMAL(2, 1) NOT NULL,
    content     TEXT          NULL,
    images      JSON          NULL,
    is_public   TINYINT(1)    NOT NULL DEFAULT 1,

    -- Auditing (inline)
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP     NULL,

    PRIMARY KEY (review_id),
    CONSTRAINT fk_reviews_order
        FOREIGN KEY (order_id) REFERENCES p_orders (order_id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_reviews_customer
        FOREIGN KEY (customer_id) REFERENCES p_customer (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_reviews_product
        FOREIGN KEY (product_id) REFERENCES products (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_reviews_variant
        FOREIGN KEY (variant_id) REFERENCES product_variants (id)
            ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE TABLE product_recommends
(
    recommend_id  BINARY(16)         NOT NULL,
    customer_id   BINARY(16)         NOT NULL,
    product_id    BINARY(16)         NOT NULL,
    order_item_id BINARY(16)         NOT NULL,
    vote          ENUM ('UP','DOWN') NOT NULL,

    -- Auditing (inline)
    created_at    TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at    TIMESTAMP          NULL,

    PRIMARY KEY (recommend_id),
    CONSTRAINT fk_preco_customer
        FOREIGN KEY (customer_id) REFERENCES p_customer (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_preco_product
        FOREIGN KEY (product_id) REFERENCES products (id)
            ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_preco_order_item
        FOREIGN KEY (order_item_id) REFERENCES order_items (order_item_id)
            ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
