ALTER TABLE actions
ALTER COLUMN user_id SET NOT NULL;

ALTER TABLE money_transactions
RENAME COLUMN user_id
TO sender_id;

ALTER TABLE money_transactions
ADD COLUMN receiver_id
INT REFERENCES users (id) ON DELETE SET NULL,
ADD COLUMN time TIMESTAMP NOT NULL default CURRENT_TIMESTAMP;

ALTER TABLE money_transactions
ALTER COLUMN transaction_type SET NOT NULL,
ALTER COLUMN money SET NOT NULL;

CREATE TABLE promotion_types(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(20),
    promotion_class  VARCHAR(20),
    promotion_plus   DOUBLE PRECISION DEFAULT 0.0,
    duration_days    INTEGER DEFAULT 0,
    price            DOUBLE PRECISION DEFAULT 0.0
);

INSERT INTO promotion_types(name, promotion_class, promotion_plus, price, duration_days) VALUES
('base','BID', 5, 100, 1),
('all','ALL', 5, 300, 1),
('30 days all','ALL', 5, 2000, 30);

ALTER TABLE promotions
drop column promotion_type;

ALTER TABLE promotions
add column promotion_type_id
INT REFERENCES promotion_types (id) ON DELETE SET NULL;