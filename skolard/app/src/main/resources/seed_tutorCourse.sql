BEGIN TRANSACTION;

INSERT OR IGNORE INTO tutorCourse (tutorEmail, courseID, grade) VALUES
('sukhdeep@example.com', 'COMP101', 4.0),
('amrit@example.com', 'PHYS150', 3.7),
('amrit@example.com', 'MATH200', 3.9);

COMMIT;
