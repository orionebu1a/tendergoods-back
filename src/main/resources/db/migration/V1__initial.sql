CREATE TABLE users
(
    id             SERIAL PRIMARY KEY,
    email          VARCHAR(255) UNIQUE NOT NULL,
    password_hash  VARCHAR(255)        NOT NULL,
    first_name     VARCHAR(100)        NOT NULL,
    last_name      VARCHAR(100)        NOT NULL,
    age            INT,
    gender         VARCHAR(10),
    rating         DECIMAL(3, 2)  DEFAULT 0.0,
    wallet_balance DECIMAL(10, 2) DEFAULT 0.0,
    created_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE items
(
    id           SERIAL PRIMARY KEY,
    user_id      INT REFERENCES users (id) ON DELETE CASCADE,
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    total_amount INT,
    category     VARCHAR(20),
    image_url    VARCHAR(255),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE items_in_bids
(
    item_id INT REFERENCES items (id) ON DELETE CASCADE,
    bid_id  INT REFERENCES bids (id) ON DELETE CASCADE,
    amount  INT
);

CREATE TABLE bids
(
    id               SERIAL PRIMARY KEY,
    user_id          INT REFERENCES users (id) ON DELETE CASCADE,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    starting_price   DECIMAL(10, 2) NOT NULL,
    current_price    DECIMAL(10, 2),
    price_increment  DECIMAL(10, 2),
    location         VARCHAR(255),
    start_time       TIMESTAMP      NOT NULL,
    end_time         TIMESTAMP      NOT NULL,
    promotion_rating INT
);

CREATE TABLE messages
(
    id           SERIAL PRIMARY KEY,
    sender_id    INT REFERENCES users (id) ON DELETE CASCADE,
    receiver_id  INT REFERENCES users (id) ON DELETE CASCADE,
    item_id      INT REFERENCES items (id) ON DELETE CASCADE,
    message_text TEXT NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notifications
(
    id         SERIAL PRIMARY KEY,
    user_id    INT REFERENCES users (id) ON DELETE CASCADE,
    type       VARCHAR(20),
    message    TEXT NOT NULL,
    is_read    BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reviews
(
    id          SERIAL PRIMARY KEY,
    reviewer_id INT REFERENCES users (id) ON DELETE CASCADE,
    reviewee_id INT REFERENCES users (id) ON DELETE CASCADE,
    rating      DECIMAL(2, 1) NOT NULL CHECK (rating BETWEEN 0 AND 5),
    review_text TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE actions
(
    id               SERIAL PRIMARY KEY,
    action_type      VARCHAR(20),
    user_id          INT REFERENCES users (id) ON DELETE SET NULL,
    item_id          INT REFERENCES items (id) ON DELETE SET NULL,
    item_category_id VARCHAR(20),
    action_time      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE promotions
(
    id             SERIAL PRIMARY KEY,
    promotion_type VARCHAR(20),
    user_id        INT       REFERENCES users (id) ON DELETE SET NULL,
    bid_id         INT       REFERENCES items (id) ON DELETE SET NULL,
    start_time     TIMESTAMP NOT NULL,
    end_time       TIMESTAMP NOT NULL
);