BEGIN TRANSACTION;


INSERT OR IGNORE INTO tutor (email, name, bio, password) VALUES
('sukhdeep@example.com', 'Sukhdeep Kaur', 'Math tutor with 5 years of experience.', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8'), -- password: "password"
('amrit@example.com', 'Amrit Singh', 'Expert in physics and calculus.', '6cb75f652a9b52798eb6cf2201057c73e0679d7d2d3c6c7a3ad7a8a99d60f8b1'); -- password: "123456"

COMMIT;
