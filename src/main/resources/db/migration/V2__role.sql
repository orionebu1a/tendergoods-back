ALTER TABLE users
    add column role varchar(20);

CREATE TABLE item_category
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(20)
);

ALTER TABLE items
    drop column category,
    add column category INT references item_category (id) ON DELETE SET NULL;

ALTER TABLE actions
    drop column item_category_id,
    add column item_category_id INT references item_category (id) ON DELETE SET NULL;