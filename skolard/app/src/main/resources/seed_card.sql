BEGIN TRANSACTION;

INSERT OR IGNORE INTO card (accountEmail, name, cardNumber, expiry) VALUES
('alice@example.com', 'Alice', 'WWZ7UCc5t95Py2uvmkIzXg==', '12/26'),
('bob@example.com', 'Bob', '2Hr6AOcWL3bAlDLyx3BpUQ==', '11/25');

COMMIT;
