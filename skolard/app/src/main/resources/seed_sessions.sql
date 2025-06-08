BEGIN TRANSACTION;

INSERT OR IGNORE INTO session (id, tutorEmail, studentEmail, startTime, endTime, courseID) VALUES
(1, 'sukhdeep@example.com', 'test@example.com', '2025-06-10T10:00', '2025-06-10T11:00', 'COMP1010');
COMMIT;