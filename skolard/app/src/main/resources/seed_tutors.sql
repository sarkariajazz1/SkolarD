BEGIN TRANSACTION;

INSERT OR IGNORE INTO tutor (email, name, bio, password) VALUES
('sukhdeep@example.com', 'Sukhdeep Kaur', 'Math tutor with 5 years of experience.', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'); -- password: password

INSERT OR IGNORE INTO tutor (email, name, bio, password) VALUES
('amrit@example.com', 'Amrit Singh', 'Expert in physics and calculus.', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'); -- password: 123456

COMMIT;
