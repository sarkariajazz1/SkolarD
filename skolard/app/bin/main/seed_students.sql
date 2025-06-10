BEGIN TRANSACTION;

<<<<<<< HEAD
INSERT OR IGNORE INTO student (email, name) VALUES
('alice@example.com', 'Alice'),
('bob@example.com', 'Bob'),
('simran@example.com', 'Simran Dhillon'),
('raj@example.com', 'Raj Gill');

COMMIT;
=======
INSERT OR IGNORE INTO student (email, name, password) VALUES
('alice@example.com', 'Alice', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'), -- password: password
('bob@example.com', 'Bob', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'), -- password: password
('simran@example.com', 'Simran Dhillon', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'), -- password: password
('raj@example.com', 'Raj Gill', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'); -- password: password

COMMIT;
>>>>>>> dev
