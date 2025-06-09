BEGIN TRANSACTION;

INSERT OR IGNORE INTO session (id, tutorEmail, studentEmail, startTime, endTime, courseID)
VALUES (1, 'jane@skolard.ca', 'alice@example.com', '2025-06-01T10:00', '2025-06-01T11:00', 'COMP101');

INSERT OR IGNORE INTO session (id, tutorEmail, studentEmail, startTime, endTime, courseID)
VALUES (2, 'john@skolard.ca', 'bob@example.com', '2025-06-02T14:00', '2025-06-02T15:00', 'MATH200');

COMMIT;
