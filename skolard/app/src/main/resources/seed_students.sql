BEGIN TRANSACTION;

INSERT OR IGNORE INTO student (email, name, password) VALUES
('alice@example.com', 'Alice', 'hashed_password3'),
('bob@example.com', 'Bob', 'hashed_password4'),
('simran@example.com', 'Simran Dhillon', 'hashed_password5'),
('raj@example.com', 'Raj Gill', 'hashed_password6');


COMMIT;
