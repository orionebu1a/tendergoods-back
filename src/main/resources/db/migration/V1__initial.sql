CREATE TABLE users
(
    id             SERIAL PRIMARY KEY,
    email          VARCHAR(255) UNIQUE NOT NULL,
    password_hash  VARCHAR(255)        NOT NULL,
    first_name     VARCHAR(100),
    last_name      VARCHAR(100),
    age            INT,
    gender         VARCHAR(10),
    rating         DOUBLE PRECISION DEFAULT 0.0,
    wallet_balance DOUBLE PRECISION DEFAULT 0.0,
    created_at     TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bids
(
    id               SERIAL PRIMARY KEY,
    user_id          INT REFERENCES users (id) ON DELETE CASCADE,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    starting_price   DOUBLE PRECISION NOT NULL,
    current_price    DOUBLE PRECISION NOT NULL,
    price_increment  DOUBLE PRECISION NOT NULL,
    location         VARCHAR(255)     NOT NULL,
    start_time       TIMESTAMP        NOT NULL,
    end_time         TIMESTAMP        NOT NULL,
    promotion_rating INT
);

CREATE TABLE items
(
    id           SERIAL PRIMARY KEY,
    user_id      INT REFERENCES users (id) ON DELETE CASCADE,
    title        VARCHAR(255)                        NOT NULL,
    description  TEXT,
    total_amount INT                                 NOT NULL,
    category     VARCHAR(40)                         NOT NULL,
    image_url    VARCHAR(255),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    bid_id       INT                                 REFERENCES bids (id) ON DELETE SET NULL
);

CREATE TABLE messages
(
    id           SERIAL PRIMARY KEY,
    sender_id    INT REFERENCES users (id) ON DELETE CASCADE,
    receiver_id  INT REFERENCES users (id) ON DELETE CASCADE,
    item_id      INT REFERENCES items (id) ON DELETE CASCADE,
    message_text TEXT                                NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE notifications
(
    id         SERIAL PRIMARY KEY,
    user_id    INT REFERENCES users (id) ON DELETE CASCADE,
    type       VARCHAR(40),
    message    TEXT                                NOT NULL,
    is_read    BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE reviews
(
    id          SERIAL PRIMARY KEY,
    reviewer_id INT REFERENCES users (id) ON DELETE CASCADE,
    reviewee_id INT REFERENCES users (id) ON DELETE CASCADE,
    rating      DOUBLE PRECISION                    NOT NULL CHECK (rating BETWEEN 0 AND 5),
    review_text TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE actions
(
    id               SERIAL PRIMARY KEY,
    action_type      VARCHAR(40),
    user_id          INT                                 REFERENCES users (id) ON DELETE SET NULL,
    item_id          INT                                 REFERENCES items (id) ON DELETE SET NULL,
    item_category_id VARCHAR(40),
    action_time      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE promotions
(
    id             SERIAL PRIMARY KEY,
    promotion_type VARCHAR(40),
    user_id        INT       REFERENCES users (id) ON DELETE SET NULL,
    bid_id         INT       REFERENCES items (id) ON DELETE SET NULL,
    start_time     TIMESTAMP NOT NULL,
    end_time       TIMESTAMP NOT NULL
);

CREATE TABLE money_transactions
(
    id               SERIAL PRIMARY KEY,
    transaction_type VARCHAR(40),
    user_id          INT REFERENCES users (id) ON DELETE SET NULL,
    money            DOUBLE PRECISION DEFAULT 0.0
);