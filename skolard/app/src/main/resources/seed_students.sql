BEGIN TRANSACTION;

INSERT OR IGNORE INTO student (email, name) VALUES
('alice@example.com', 'Alice'),
('bob@example.com', 'Bob'),
('simran@example.com', 'Simran Dhillon'),
('raj@example.com', 'Raj Gill');

COMMIT;
