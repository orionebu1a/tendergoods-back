ALTER TABLE messages
    DROP COLUMN bid_id;

ALTER TABLE messages
    ADD COLUMN bid_id INT REFERENCES bids (id) ON DELETE SET NULL;

ALTER TABLE promotions
    DROP COLUMN bid_id;

ALTER TABLE promotions
    ADD COLUMN bid_id INT REFERENCES bids (id) ON DELETE SET NULL;