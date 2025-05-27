BEGIN TRANSACTION;

INSERT OR IGNORE INTO tutor (email, name, bio) VALUES
('sukhdeep@example.com', 'Sukhdeep Kaur', 'Math tutor with 5 years of experience.'),
('amrit@example.com', 'Amrit Singh', 'Expert in physics and calculus.');

COMMIT;
