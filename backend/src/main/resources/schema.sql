-- ============================================================
-- intellectual schema
-- Generated from JPA domain classes
-- ============================================================

CREATE TABLE IF NOT EXISTS tags (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS dishes (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)  NOT NULL,
    description VARCHAR(1000),
    created_at  TIMESTAMP     NOT NULL,
    has_photo   BOOLEAN       NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS dish_tags (
    dish_id BIGINT NOT NULL REFERENCES dishes(id) ON DELETE CASCADE,
    tag_id  BIGINT NOT NULL REFERENCES tags(id)   ON DELETE CASCADE,
    PRIMARY KEY (dish_id, tag_id)
);

CREATE TABLE IF NOT EXISTS order_records (
    id         BIGSERIAL PRIMARY KEY,
    dish_id    BIGINT    NOT NULL REFERENCES dishes(id) ON DELETE RESTRICT,
    ordered_at TIMESTAMP NOT NULL,
    quantity   INTEGER   NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_order_dish_date
    ON order_records (dish_id, ordered_at);
