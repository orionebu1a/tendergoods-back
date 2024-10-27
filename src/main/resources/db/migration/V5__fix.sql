ALTER TABLE messages
    drop column item_id,
    add column bid_id      INT REFERENCES items (id) ON DELETE SET NULL,
    drop column sender_id,
    drop column receiver_id,
    add column sender_id   INT REFERENCES users (id) ON DELETE SET NULL,
    add column receiver_id INT REFERENCES users (id) ON DELETE SET NULL;

ALTER TABLE actions
    add column bid_price DOUBLE PRECISION;

ALTER TABLE bids
    add column last_user_bet INT REFERENCES users (id) ON DELETE SET NULL;