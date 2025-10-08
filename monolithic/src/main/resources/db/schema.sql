-- =====================================================================
-- Schema: commerce_mono (MySQL 8.0+)
-- Charset/Collation: utf8mb4 / utf8mb4_0900_ai_ci
-- Soft Delete 전략: 실제 DELETE 금지, FK는 모두 RESTRICT
-- UUID v7 저장: BINARY(16)  (애플리케이션에서 UUID_TO_BIN(:v7, 0) 사용 권장)
-- =====================================================================

SET NAMES utf8mb4;
DROP DATABASE IF EXISTS commerce_mono;
CREATE DATABASE commerce_mono
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE commerce_mono;

SET FOREIGN_KEY_CHECKS = 0;

-- ========== 공통 ==========
CREATE TABLE p_time (
                        p_time_id   BINARY(16)   NOT NULL,
                        created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        created_by  VARCHAR(100) NOT NULL,
                        updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        updated_by  VARCHAR(100) NOT NULL,
                        deleted_at  TIMESTAMP    NULL,
                        deleted_by  VARCHAR(100) NULL,
                        PRIMARY KEY (p_time_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ========== 코드 테이블 ==========
CREATE TABLE order_status_codes (
                                    code         VARCHAR(30)  NOT NULL,
                                    display_name VARCHAR(50)  NOT NULL,
                                    sort_order   INT          NOT NULL,
                                    is_active    TINYINT(1)   NOT NULL DEFAULT 1,
                                    p_time_id    BINARY(16)   NOT NULL,
                                    PRIMARY KEY (code),
                                    CONSTRAINT fk_order_status_codes_ptime
                                        FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                            ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE order_type_codes (
                                  code         VARCHAR(30)  NOT NULL,
                                  display_name VARCHAR(50)  NOT NULL,
                                  sort_order   INT          NOT NULL,
                                  is_active    TINYINT(1)   NOT NULL DEFAULT 1,
                                  p_time_id    BINARY(16)   NOT NULL,
                                  PRIMARY KEY (code),
                                  CONSTRAINT fk_order_type_codes_ptime
                                      FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                          ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE payment_status_codes (
                                      code         VARCHAR(30)  NOT NULL,
                                      display_name VARCHAR(50)  NOT NULL,
                                      sort_order   INT          NOT NULL,
                                      is_active    TINYINT(1)   NOT NULL DEFAULT 1,
                                      p_time_id    BINARY(16)   NOT NULL,
                                      PRIMARY KEY (code),
                                      CONSTRAINT fk_payment_status_codes_ptime
                                          FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                              ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE payment_method_codes (
                                      code         VARCHAR(30)  NOT NULL,
                                      display_name VARCHAR(50)  NOT NULL,
                                      sort_order   INT          NOT NULL,
                                      is_active    TINYINT(1)   NOT NULL DEFAULT 1,
                                      p_time_id    BINARY(16)   NOT NULL,
                                      PRIMARY KEY (code),
                                      CONSTRAINT fk_payment_method_codes_ptime
                                          FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                              ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ========== 분류/카테고리/배달 구역 ==========
CREATE TABLE p_categories (
                              category_id   BINARY(16)   NOT NULL,
                              category_name VARCHAR(100) NOT NULL,
                              sort_order    INT          NOT NULL,
                              is_active     TINYINT(1)   NOT NULL DEFAULT 1,
                              p_time_id     BINARY(16)   NOT NULL,
                              PRIMARY KEY (category_id),
                              CONSTRAINT fk_p_categories_ptime
                                  FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                      ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE p_menu_category (
                                 code         VARCHAR(30)  NOT NULL,
                                 display_name VARCHAR(50)  NOT NULL,
                                 sort_order   INT          NOT NULL,
                                 is_active    TINYINT(1)   NOT NULL DEFAULT 1,
                                 p_time_id    BINARY(16)   NOT NULL,
                                 PRIMARY KEY (code),
                                 CONSTRAINT fk_p_menu_category_ptime
                                     FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                         ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE delivery_areas (
                                area_id   BINARY(16)   NOT NULL,
                                area_name VARCHAR(100) NOT NULL,
                                p_time_id BINARY(16)   NOT NULL,
                                PRIMARY KEY (area_id),
                                CONSTRAINT fk_delivery_areas_ptime
                                    FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                        ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ========== 관리자/고객/주소/장바구니 ==========
CREATE TABLE p_admins (
                          id           BINARY(16)   NOT NULL,
                          name         VARCHAR(20)  NOT NULL,
                          email        VARCHAR(255) NOT NULL,
                          password     VARCHAR(255) NOT NULL,
                          phone_number VARCHAR(18)  NULL,
                          position     VARCHAR(50)  NULL,
                          p_time_id    BINARY(16)   NOT NULL,
                          PRIMARY KEY (id),
                          UNIQUE KEY uk_p_admins_name (name),
                          CONSTRAINT fk_p_admins_ptime
                              FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                  ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE p_customer (
                            id           BINARY(16)   NOT NULL,
                            name         VARCHAR(20)  NOT NULL,
                            nickname     VARCHAR(100) NULL,
                            email        VARCHAR(255) NULL,
                            password     VARCHAR(255) NOT NULL,
                            phone_number VARCHAR(18)  NULL,
                            points       INT          NULL,
                            p_time_id    BINARY(16)   NOT NULL,
                            PRIMARY KEY (id),
                            UNIQUE KEY uk_p_customer_name (name),
                            CONSTRAINT fk_p_customer_ptime
                                FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                    ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE p_addresses (
                             id          BINARY(16)   NOT NULL,
                             customer_id BINARY(16)   NOT NULL,
                             zipcode     VARCHAR(10)  NULL,
                             road_addr   VARCHAR(500) NULL,
                             detail_addr VARCHAR(200) NULL,
                             is_selected TINYINT(1)   NOT NULL DEFAULT 0,
                             p_time_id   BINARY(16)   NOT NULL,
                             PRIMARY KEY (id),
                             CONSTRAINT fk_p_addresses_customer
                                 FOREIGN KEY (customer_id) REFERENCES p_customer(id)
                                     ON UPDATE RESTRICT ON DELETE RESTRICT,
                             CONSTRAINT fk_p_addresses_ptime
                                 FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                     ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE p_cart (
                        cart_id     BINARY(16) NOT NULL,
                        customer_id BINARY(16) NOT NULL,
                        cart_items  JSON       NOT NULL,
                        p_time_id   BINARY(16) NOT NULL,
                        PRIMARY KEY (cart_id),
                        UNIQUE KEY uk_p_cart_customer (customer_id),
                        CONSTRAINT fk_p_cart_customer
                            FOREIGN KEY (customer_id) REFERENCES p_customer(id)
                                ON UPDATE RESTRICT ON DELETE RESTRICT,
                        CONSTRAINT fk_p_cart_ptime
                            FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ========== 매장/매니저/메뉴 ==========
CREATE TABLE p_stores (
                          store_id      BINARY(16)   NOT NULL,
                          store_name    VARCHAR(200) NOT NULL,
                          store_address VARCHAR(300) NULL,
                          phone_number  VARCHAR(18)  NULL,
                          category_id   BINARY(16)   NULL,
                          min_cost      INT          NOT NULL DEFAULT 0,
                          description   TEXT         NULL,
                          store_lat     INT          NULL,
                          store_lon     INT          NULL,
                          open_status   TINYINT(1)   NULL,
                          open_time     TIME         NOT NULL,
                          close_time    TIME         NOT NULL,
                          p_time_id     BINARY(16)   NOT NULL,
                          PRIMARY KEY (store_id),
                          CONSTRAINT fk_p_stores_category
                              FOREIGN KEY (category_id) REFERENCES p_categories(category_id)
                                  ON UPDATE RESTRICT ON DELETE RESTRICT,
                          CONSTRAINT fk_p_stores_ptime
                              FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                  ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE p_managers (
                            id           BINARY(16)   NOT NULL,
                            name         VARCHAR(20)  NOT NULL,
                            email        VARCHAR(255) NOT NULL,
                            password     VARCHAR(255) NOT NULL,
                            phone_number VARCHAR(18)  NULL,
                            store_id     BINARY(16)   NULL,
                            p_time_id    BINARY(16)   NOT NULL,
                            PRIMARY KEY (id),
                            UNIQUE KEY uk_p_managers_name (name),
                            CONSTRAINT fk_p_managers_store
                                FOREIGN KEY (store_id) REFERENCES p_stores(store_id)
                                    ON UPDATE RESTRICT ON DELETE RESTRICT,
                            CONSTRAINT fk_p_managers_ptime
                                FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                    ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE p_menus (
                         menu_id            BINARY(16)   NOT NULL,
                         store_id           BINARY(16)   NOT NULL,
                         menu_num           INT          NOT NULL,
                         menu_name          VARCHAR(200) NOT NULL,
                         menu_category_code VARCHAR(30)  NOT NULL,
                         price              INT          NOT NULL,
                         description        TEXT         NULL,
                         is_available       TINYINT(1)   NOT NULL DEFAULT 1,
                         image_url          VARCHAR(500) NULL,
                         p_time_id          BINARY(16)   NOT NULL,
                         PRIMARY KEY (menu_id),
                         CONSTRAINT fk_p_menus_store
                             FOREIGN KEY (store_id) REFERENCES p_stores(store_id)
                                 ON UPDATE RESTRICT ON DELETE RESTRICT,
                         CONSTRAINT fk_p_menus_category
                             FOREIGN KEY (menu_category_code) REFERENCES p_menu_category(code)
                                 ON UPDATE RESTRICT ON DELETE RESTRICT,
                         CONSTRAINT fk_p_menus_ptime
                             FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                 ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ========== 주문/결제 요청/결제 ==========
CREATE TABLE p_orders (
                          order_id        BINARY(16)  NOT NULL,
                          order_number    VARCHAR(50) NOT NULL,
                          customer_id     BINARY(16)  NOT NULL,
                          store_id        BINARY(16)  NOT NULL,
                          payment_id      BINARY(16)  NOT NULL, -- FK는 아래 ALTER에서 추가(순환)
                          order_status    VARCHAR(30) NOT NULL,
                          order_type      VARCHAR(30) NOT NULL,
                          order_menu_list JSON        NOT NULL,
                          p_time_id       BINARY(16)  NOT NULL,
                          PRIMARY KEY (order_id),
                          UNIQUE KEY uk_p_orders_order_number (order_number),
                          CONSTRAINT fk_p_orders_customer
                              FOREIGN KEY (customer_id) REFERENCES p_customer(id)
                                  ON UPDATE RESTRICT ON DELETE RESTRICT,
                          CONSTRAINT fk_p_orders_store
                              FOREIGN KEY (store_id) REFERENCES p_stores(store_id)
                                  ON UPDATE RESTRICT ON DELETE RESTRICT,
                          CONSTRAINT fk_p_orders_status
                              FOREIGN KEY (order_status) REFERENCES order_status_codes(code)
                                  ON UPDATE RESTRICT ON DELETE RESTRICT,
                          CONSTRAINT fk_p_orders_type
                              FOREIGN KEY (order_type) REFERENCES order_type_codes(code)
                                  ON UPDATE RESTRICT ON DELETE RESTRICT,
                          CONSTRAINT fk_p_orders_ptime
                              FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                  ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE p_payment_requests (
                                    payment_request_id BINARY(16)   NOT NULL,
                                    order_id           BINARY(16)   NOT NULL,
                                    pg_provider        VARCHAR(100) NOT NULL,
                                    request_payload    JSON         NOT NULL,
                                    redirect_url       TEXT         NULL,
                                    status             VARCHAR(50)  NOT NULL,
                                    requested_at       TIMESTAMP    NOT NULL,
                                    responded_at       TIMESTAMP    NULL,
                                    failure_reason     TEXT         NULL,
                                    p_time_id          BINARY(16)   NOT NULL,
                                    PRIMARY KEY (payment_request_id),
                                    CONSTRAINT fk_p_payment_requests_order
                                        FOREIGN KEY (order_id) REFERENCES p_orders(order_id)
                                            ON UPDATE RESTRICT ON DELETE RESTRICT,
                                    CONSTRAINT fk_p_payment_requests_ptime
                                        FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                            ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE p_payments (
                            payment_id         BINARY(16)   NOT NULL,
                            customer_id        BINARY(16)   NOT NULL,
                            payment_request_id BINARY(16)   NOT NULL,
                            payment_status     VARCHAR(30)  NOT NULL,
                            payment_method     VARCHAR(30)  NOT NULL,
                            total_amount       INT          NOT NULL,
                            pg_transaction_id  VARCHAR(100) NULL,
                            approval_code      VARCHAR(50)  NULL,
                            card_info          JSON         NULL,
                            redirect_url       TEXT         NULL,
                            receipt_url        TEXT         NULL,
                            requested_at       TIMESTAMP    NULL,
                            approved_at        TIMESTAMP    NULL,
                            failed_at          TIMESTAMP    NULL,
                            failure_reason     TEXT         NULL,
                            offline_payment_note TEXT       NULL,
                            p_time_id          BINARY(16)   NOT NULL,
                            PRIMARY KEY (payment_id),
                            UNIQUE KEY uk_p_payments_request (payment_request_id),
                            CONSTRAINT fk_p_payments_customer
                                FOREIGN KEY (customer_id) REFERENCES p_customer(id)
                                    ON UPDATE RESTRICT ON DELETE RESTRICT,
                            CONSTRAINT fk_p_payments_request
                                FOREIGN KEY (payment_request_id) REFERENCES p_payment_requests(payment_request_id)
                                    ON UPDATE RESTRICT ON DELETE RESTRICT,
                            CONSTRAINT fk_p_payments_status
                                FOREIGN KEY (payment_status) REFERENCES payment_status_codes(code)
                                    ON UPDATE RESTRICT ON DELETE RESTRICT,
                            CONSTRAINT fk_p_payments_method
                                FOREIGN KEY (payment_method) REFERENCES payment_method_codes(code)
                                    ON UPDATE RESTRICT ON DELETE RESTRICT,
                            CONSTRAINT fk_p_payments_ptime
                                FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                    ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 순환 FK 마무리: 주문 → 결제
ALTER TABLE p_orders
    ADD CONSTRAINT fk_p_orders_payment
        FOREIGN KEY (payment_id) REFERENCES p_payments(payment_id)
            ON UPDATE RESTRICT ON DELETE RESTRICT;

-- ========== 배달/픽업 ==========
CREATE TABLE p_delivery_orders (
                                   order_id                  BINARY(16)   NOT NULL,
                                   delivery_fee              DECIMAL(10,2) NOT NULL DEFAULT 0,
                                   delivery_requests         TEXT         NULL,
                                   zipcode                   VARCHAR(10)  NULL,
                                   road_addr                 VARCHAR(500) NULL,
                                   detail_addr               VARCHAR(200) NULL,
                                   estimated_delivery_time   TIMESTAMP    NULL,
                                   estimated_preparation_time INT         NULL,
                                   canceled_at               TIMESTAMP    NULL,
                                   canceled_by               VARCHAR(100) NULL,
                                   cancel_reason             TEXT         NULL,
                                   p_time_id                 BINARY(16)   NOT NULL,
                                   PRIMARY KEY (order_id),
                                   CONSTRAINT fk_p_delivery_orders_order
                                       FOREIGN KEY (order_id) REFERENCES p_orders(order_id)
                                           ON UPDATE RESTRICT ON DELETE RESTRICT,
                                   CONSTRAINT fk_p_delivery_orders_ptime
                                       FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                           ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE p_store_delivery_areas (
                                        store_id    BINARY(16) NOT NULL,
                                        area_id     BINARY(16) NOT NULL,
                                        delivery_fee INT       NOT NULL DEFAULT 0,
                                        p_time_id   BINARY(16) NOT NULL,
                                        PRIMARY KEY (store_id, area_id),
                                        CONSTRAINT fk_p_sda_store
                                            FOREIGN KEY (store_id) REFERENCES p_stores(store_id)
                                                ON UPDATE RESTRICT ON DELETE RESTRICT,
                                        CONSTRAINT fk_p_sda_area
                                            FOREIGN KEY (area_id)  REFERENCES delivery_areas(area_id)
                                                ON UPDATE RESTRICT ON DELETE RESTRICT,
                                        CONSTRAINT fk_p_sda_ptime
                                            FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                                ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE p_pickup_orders (
                                 order_id            BINARY(16)  NOT NULL,
                                 pickup_requests     TEXT        NULL,
                                 estimated_pickup_time TIMESTAMP NULL,
                                 canceled_at         TIMESTAMP   NULL,
                                 canceled_by         VARCHAR(100) NULL,
                                 cancel_reason       TEXT        NULL,
                                 p_time_id           BINARY(16)  NOT NULL,
                                 PRIMARY KEY (order_id),
                                 CONSTRAINT fk_p_pickup_orders_order
                                     FOREIGN KEY (order_id) REFERENCES p_orders(order_id)
                                         ON UPDATE RESTRICT ON DELETE RESTRICT,
                                 CONSTRAINT fk_p_pickup_orders_ptime
                                     FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                         ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ========== 리뷰/AI 응답 ==========
CREATE TABLE p_reviews (
                           review_id  BINARY(16)   NOT NULL,
                           order_id   BINARY(16)   NOT NULL,
                           rating     DECIMAL(2,1) NOT NULL,
                           content    TEXT         NOT NULL,
                           p_time_id  BINARY(16)   NOT NULL,
                           PRIMARY KEY (review_id),
                           CONSTRAINT fk_p_reviews_order
                               FOREIGN KEY (order_id) REFERENCES p_orders(order_id)
                                   ON UPDATE RESTRICT ON DELETE RESTRICT,
                           CONSTRAINT fk_p_reviews_ptime
                               FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                   ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE p_ai_responses (
                                ai_response_id BINARY(16) NOT NULL,
                                description    TEXT       NOT NULL,
                                p_time_id      BINARY(16) NOT NULL,
                                PRIMARY KEY (ai_response_id),
                                CONSTRAINT fk_p_ai_responses_ptime
                                    FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                        ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ========== 일별 집계 ==========
CREATE TABLE daily_store_sales (
                                   sale_date    DATE        NOT NULL,
                                   store_id     BINARY(16)  NOT NULL,
                                   order_count  INT         NOT NULL,
                                   total_amount DECIMAL(12,2) NOT NULL,
                                   p_time_id    BINARY(16)  NOT NULL,
                                   PRIMARY KEY (sale_date, store_id),
                                   CONSTRAINT fk_daily_store_sales_store
                                       FOREIGN KEY (store_id) REFERENCES p_stores(store_id)
                                           ON UPDATE RESTRICT ON DELETE RESTRICT,
                                   CONSTRAINT fk_daily_store_sales_ptime
                                       FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                           ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE daily_menu_sales (
                                  sale_date     DATE        NOT NULL,
                                  store_id      BINARY(16)  NOT NULL,
                                  menu_id       BINARY(16)  NOT NULL,
                                  quantity_sold INT         NOT NULL,
                                  total_amount  DECIMAL(12,2) NOT NULL,
                                  p_time_id     BINARY(16)  NOT NULL,
                                  PRIMARY KEY (sale_date, store_id, menu_id),
                                  CONSTRAINT fk_daily_menu_sales_store
                                      FOREIGN KEY (store_id) REFERENCES p_stores(store_id)
                                          ON UPDATE RESTRICT ON DELETE RESTRICT,
                                  CONSTRAINT fk_daily_menu_sales_menu
                                      FOREIGN KEY (menu_id) REFERENCES p_menus(menu_id)
                                          ON UPDATE RESTRICT ON DELETE RESTRICT,
                                  CONSTRAINT fk_daily_menu_sales_ptime
                                      FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                          ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ========== 매장 신청서 ==========
CREATE TABLE p_manager_store_applications (
                                              application_id       BINARY(16)   NOT NULL,
                                              manager_name         VARCHAR(20)  NOT NULL,
                                              manager_email        VARCHAR(255) NOT NULL,
                                              manager_password     VARCHAR(255) NOT NULL,
                                              manager_phone_number VARCHAR(18)  NULL,
                                              store_name           VARCHAR(200) NOT NULL,
                                              store_address        VARCHAR(300) NULL,
                                              store_phone_number   VARCHAR(18)  NULL,
                                              category_id          BINARY(16)   NULL,
                                              description          TEXT         NULL,
                                              status               VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
                                              reviewer_admin_id    BINARY(16)   NULL,
                                              review_comment       TEXT         NULL,
                                              p_time_id            BINARY(16)   NOT NULL,
                                              PRIMARY KEY (application_id),
                                              CONSTRAINT fk_p_msa_category
                                                  FOREIGN KEY (category_id) REFERENCES p_categories(category_id)
                                                      ON UPDATE RESTRICT ON DELETE RESTRICT,
                                              CONSTRAINT fk_p_msa_reviewer
                                                  FOREIGN KEY (reviewer_admin_id) REFERENCES p_admins(id)
                                                      ON UPDATE RESTRICT ON DELETE RESTRICT,
                                              CONSTRAINT fk_p_msa_ptime
                                                  FOREIGN KEY (p_time_id) REFERENCES p_time(p_time_id)
                                                      ON UPDATE RESTRICT ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================================
-- 인덱스: 운영/마이그레이션 편의를 위해 테이블 생성 후 일괄 추가
-- (실제 쿼리 패턴에 맞춰 조정 권장)
-- =====================================================================

-- 자주 조인/필터되는 FK들(복합 포함)
ALTER TABLE p_orders
    ADD INDEX idx_orders_customer_status (customer_id, order_status),
  ADD INDEX idx_orders_store (store_id),
  ADD INDEX idx_orders_type (order_type),
  ADD INDEX idx_orders_payment (payment_id);

ALTER TABLE p_menus
    ADD INDEX idx_menus_store_available_num (store_id, is_available, menu_num);

ALTER TABLE p_stores
    ADD INDEX idx_stores_category_open (category_id, open_status);

ALTER TABLE p_payments
    ADD INDEX idx_payments_customer_status_time (customer_id, payment_status, approved_at);

ALTER TABLE p_payment_requests
    ADD INDEX idx_payreq_order (order_id);

ALTER TABLE p_addresses
    ADD INDEX idx_addresses_customer_selected (customer_id, is_selected);

ALTER TABLE p_store_delivery_areas
    ADD INDEX idx_sda_area (area_id);

ALTER TABLE daily_store_sales
    ADD INDEX idx_daily_store_sales_storedate (store_id, sale_date);

ALTER TABLE daily_menu_sales
    ADD INDEX idx_daily_menu_sales_keys (store_id, sale_date, menu_id);
