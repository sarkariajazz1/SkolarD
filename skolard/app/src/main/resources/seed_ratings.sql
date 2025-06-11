BEGIN TRANSACTION;

INSERT OR IGNORE INTO ratings (tutorEmail, sessionId, courseName, studentEmail, rating) VALUES
('sukhdeep@example.com', 1, 'COMP1010', 'alice@example.com', 5),
('amrit@example.com', 2, 'MATH1700', 'bob@example.com', 4),
('sukhdeep@example.com', 1, 'COMP1010', 'bob@example.com', 5),
('li@example.com', 3, 'COMP1010', 'simran@example.com', 3),
('maria@example.com', 4, 'MATH1700', 'alice@example.com', 5),
('david@example.com', 5, 'ECON1020', 'raj@example.com', 4);

COMMIT;