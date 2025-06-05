BEGIN TRANSACTION;

INSERT OR IGNORE INTO session (id, tutorEmail, studentEmail, startTime, endTime, courseID) VALUES
(1, 'sukhdeep@example.com', 'alice@example.com', '2025-06-01T10:00', '2025-06-01T11:00', 'COMP1010'),
(2, 'amrit@example.com', 'bob@example.com', '2025-06-02T14:00', '2025-06-02T15:00', 'MATH1500'),
(2, 'maria@example.com', 'bob@example.com', '2025-06-01T13:00', '2025-06-01T14:00', 'COMP1010'),
(3, 'li@example.com', 'carol@example.com', '2025-06-02T09:00', '2025-06-02T10:30', 'MATH1500'),
(4, 'david@example.com', 'erin@example.com', '2025-06-03T15:00', '2025-06-03T16:00', 'PHYS1050'),
(5, 'sukhdeep@example.com', 'frank@example.com', '2025-06-04T08:00', '2025-06-04T09:00', 'COMP1010'),
(6, 'maria@example.com', 'grace@example.com', '2025-06-04T14:30', '2025-06-04T15:30', 'MATH1500'),
(7, 'li@example.com', 'hank@example.com', '2025-06-05T10:00', '2025-06-05T11:00', 'COMP101'),
(8, 'david@example.com', 'irene@example.com', '2025-06-05T11:30', '2025-06-05T12:30', 'PHYS1050'),
(9, 'alice@example.com', 'james@example.com', '2025-06-06T09:00', '2025-06-06T10:00', 'COMP1010'),
(10, 'bob@example.com', 'kate@example.com', '2025-06-06T13:00', '2025-06-06T14:00', 'MATH1500');

COMMIT;