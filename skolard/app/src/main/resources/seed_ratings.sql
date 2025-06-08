BEGIN TRANSACTION;

INSERT OR IGNORE INTO ratings (tutorName, sessionId, courseName, studentName, rating) VALUES
('Sukhdeep Kaur', 1, 'Introduction to Computer Science', 'Alice', 5),
('Amrit Singh', 2, 'Calculus II', 'Bob', 4),
('Sukhdeep Kaur', 1, 'Introduction to Computer Science', 'Bob', 5),
('Li Wei', 3, 'Introduction to Computer Science', 'Simran Dhillon', 3),
('Maria Garcia', 4, 'Calculus II', 'Alice', 5),
('David Johnson', 5, 'Physics for Engineers', 'Raj Gill', 4);

COMMIT;