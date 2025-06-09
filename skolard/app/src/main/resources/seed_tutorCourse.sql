BEGIN TRANSACTION;

INSERT OR IGNORE INTO tutorCourse (tutorEmail, courseID, grade) VALUES
('sukhdeep@example.com', 'COMP101', 4.0),
('amrit@example.com', 'PHYS150', 3.7),
('amrit@example.com', 'MATH200', 3.9).
('sukhdeep@example.com', 'COMP1010', 4.0),
('li@example.com', 'COMP1010', 3.0),
('bob@example.com', 'COMP1010', 2.5);


COMMIT;
