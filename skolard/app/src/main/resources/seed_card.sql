BEGIN TRANSACTION;

DELETE FROM card WHERE 1=1;

INSERT OR IGNORE INTO card (accountEmail, name, cardNumber, expiry) VALUES
('alice@example.com', 'Alice', '4111111111111111', '12/26'),
('bob@example.com', 'Bob', '5555555555554444', '11/25');

COMMIT;
