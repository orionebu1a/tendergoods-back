ALTER TABLE messages
    DROP COLUMN item_id;
ALTER TABLE messages
    ADD COLUMN bid_id INT REFERENCES items (id) ON DELETE SET NULL;
ALTER TABLE messages
    DROP COLUMN sender_id;
ALTER TABLE messages
    DROP COLUMN receiver_id;
ALTER TABLE messages
    ADD COLUMN sender_id INT REFERENCES users (id) ON DELETE SET NULL;
ALTER TABLE messages
    ADD COLUMN receiver_id INT REFERENCES users (id) ON DELETE SET NULL;

ALTER TABLE actions
    add column bid_price DOUBLE PRECISION;

ALTER TABLE bids
    add column last_user_bet INT REFERENCES users (id) ON DELETE SET NULL;