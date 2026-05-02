-- liquibase formatted sql

-- changeset ace5:001-create-shop-tables
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       address VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE products (
                          id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description VARCHAR(255),
                          price NUMERIC(10, 2) NOT NULL,
                          stock_quantity INTEGER NOT NULL,
                          deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE carts (
                       id UUID PRIMARY KEY,
                       user_id UUID NOT NULL UNIQUE,
                       deleted BOOLEAN NOT NULL DEFAULT FALSE,
                       CONSTRAINT fk_carts_user
                           FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE cart_items (
                            id UUID PRIMARY KEY,
                            cart_id UUID NOT NULL,
                            product_id UUID NOT NULL,
                            quantity INTEGER NOT NULL,
                            deleted BOOLEAN NOT NULL DEFAULT FALSE,
                            CONSTRAINT fk_cart_items_cart
                                FOREIGN KEY (cart_id) REFERENCES carts (id),
                            CONSTRAINT fk_cart_items_product
                                FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        user_id UUID NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        deleted BOOLEAN NOT NULL DEFAULT FALSE,
                        CONSTRAINT fk_orders_user
                            FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE order_items (
                             id UUID PRIMARY KEY,
                             order_id UUID NOT NULL,
                             product_id UUID NOT NULL,
                             quantity INTEGER NOT NULL,
                             unit_price NUMERIC(10, 2) NOT NULL,
                             deleted BOOLEAN NOT NULL DEFAULT FALSE,
                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id) REFERENCES orders (id),
                             CONSTRAINT fk_order_items_product
                                 FOREIGN KEY (product_id) REFERENCES products (id)
);

-- rollback DROP TABLE order_items;
-- rollback DROP TABLE orders;
-- rollback DROP TABLE cart_items;
-- rollback DROP TABLE carts;
-- rollback DROP TABLE products;
-- rollback DROP TABLE users;