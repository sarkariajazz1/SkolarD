BEGIN TRANSACTION;

INSERT OR IGNORE INTO tutor (email, name, bio, password) VALUES
('sukhdeep@example.com', 'Sukhdeep Kaur', 'Math tutor with 5 years of experience.', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'), -- password: password
('amrit@example.com', 'Amrit Singh', 'Expert in physics and calculus.', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'), -- password: password
('sukhdeep@example.com', 'Sukhdeep Singh', 'Experienced COMP101 tutor passionate about teaching programming fundamentals.', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'),
('maria@example.com', 'Maria Garcia', 'Mathematics tutor specializing in calculus and linear algebra.', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'),
('li@example.com', 'Li Wei', 'Friendly COMP101 tutor with a focus on practical coding examples.', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'),
('david@example.com', 'David Johnson', 'Physics tutor who simplifies complex concepts into real-world examples.', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'),
('alice@example.com', 'Alice Kim', 'Enthusiastic about helping students master data structures in COMP101.', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'),
('bob@example.com', 'Bob Lee', 'Math expert with 10+ years tutoring experience in algebra and stats.', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8');

COMMIT;
