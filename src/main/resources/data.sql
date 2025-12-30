INSERT INTO categories (name, description)
VALUES
    ('Fruits', 'All kinds of fruits'),
    ('Vegetables', 'Fresh vegetables');

INSERT INTO products (name, description, category_id, price, stock_quantity, unit)
VALUES
    ('Apple', 'Red apples', 1, 1.50, 120, 'kg'),
    ('Banana', 'Yellow bananas', 1, 1.20, 200, 'kg'),
    ('Carrot', 'Fresh carrots', 2, 0.90, 150, 'kg'),
    ('Tomato', 'Red tomatoes', 2, 1.10, 180, 'kg');

INSERT INTO orders (order_number, status, total_amount)
VALUES
    ('ORD-0001', 'CREATED', 4.20),
    ('ORD-0002', 'PAID', 3.00);

INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal)
VALUES
    (1, 1, 1, 1.50, 1.50),
    (1, 3, 3, 0.90, 2.70),
    (2, 2, 2, 1.20, 2.40),
    (2, 4, 1, 1.10, 1.10);
