BEGIN TRANSACTION;

INSERT OR IGNORE INTO session (id, tutorEmail, studentEmail, startTime, endTime, courseID) VALUES
(2, 'sukhdeep@example.com', NULL, '2025-06-11T09:00', '2025-06-11T10:00', 'COMP1010'),
(3, 'sukhdeep@example.com', NULL, '2025-06-11T11:00', '2025-06-11T12:00', 'COMP1010'),
(4, 'sukhdeep@example.com', NULL, '2025-06-12T13:00', '2025-06-12T14:00', 'COMP1010'),
(5, 'sukhdeep@example.com', NULL, '2025-06-13T15:00', '2025-06-13T16:00', 'COMP1010'),
(6, 'sukhdeep@example.com', NULL, '2025-06-14T17:00', '2025-06-14T18:00', 'COMP1010'),
(7, 'sukhdeep@example.com', NULL, '2025-06-15T09:30', '2025-06-15T10:30', 'COMP1010'),
(8, 'sukhdeep@example.com', NULL, '2025-06-15T11:00', '2025-06-15T12:00', 'COMP1010'),
(9, 'sukhdeep@example.com', NULL, '2025-06-16T14:00', '2025-06-16T15:00', 'COMP1010'),
(10, 'sukhdeep@example.com', NULL, '2025-06-17T16:00', '2025-06-17T17:00', 'COMP1010'),
(11, 'sukhdeep@example.com', NULL, '2025-05-10T09:00', '2025-05-10T10:00', 'COMP1010'),
(12, 'sukhdeep@example.com', NULL, '2025-05-11T11:00', '2025-05-11T12:00', 'COMP1010'),
(13, 'sukhdeep@example.com', NULL, '2025-05-12T13:00', '2025-05-12T14:00', 'COMP1010'),
(14, 'sukhdeep@example.com', NULL, '2025-05-13T15:00', '2025-05-13T16:00', 'COMP1010'),
(15, 'sukhdeep@example.com', NULL, '2025-05-14T17:00', '2025-05-14T18:00', 'COMP1010'),
(16, 'sukhdeep@example.com', NULL, '2025-05-15T09:30', '2025-05-15T10:30', 'COMP1010'),
(17, 'sukhdeep@example.com', NULL, '2025-05-15T11:00', '2025-05-15T12:00', 'COMP1010'),
(18, 'sukhdeep@example.com', NULL, '2025-05-16T14:00', '2025-05-16T15:00', 'COMP1010'),
(19, 'sukhdeep@example.com', NULL, '2025-05-17T16:00', '2025-05-17T17:00', 'COMP1010');

COMMIT;
