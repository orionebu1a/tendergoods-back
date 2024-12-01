ALTER TABLE users
    add column role VARCHAR(40);

CREATE TABLE item_category
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(40)
);

ALTER TABLE items
    drop column if exists category;

ALTER TABLE items
    add column category_id INT references item_category (id) ON DELETE SET NULL;

ALTER TABLE actions
    drop column if exists item_category_id;

ALTER TABLE actions
    add column item_category_id INT references item_category (id) ON DELETE SET NULL;