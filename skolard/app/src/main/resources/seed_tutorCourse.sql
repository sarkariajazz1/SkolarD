BEGIN TRANSACTION;

INSERT OR IGNORE INTO tutorCourse (tutorEmail, grade, courseID) VALUES
('sukhdeep@example.com', 4.0, 'COMP101'),
('amrit@example.com', 3.7, 'PHYS150'),
('amrit@example.com', 3.9, 'MATH200');

COMMIT;
