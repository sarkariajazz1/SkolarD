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
(10, 'bob@example.com', 'kate@example.com', '2025-06-06T13:00', '2025-06-06T14:00', 'MATH1500'),
(11, 'bob@example.com', 'kate@example.com', '2025-06-06T13:00', '2025-06-06T14:00', 'MATH1500'),
(12, 'amrit@example.com', 'leo@example.com', '2025-06-07T10:00', '2025-06-07T11:00', 'PHYS1050'),
(13, 'sukhdeep@example.com', 'maya@example.com', '2025-06-07T14:00', '2025-06-07T15:00', 'COMP1010'),
(14, 'maria@example.com', 'nina@example.com', '2025-06-08T09:00', '2025-06-08T10:00', 'MATH1500'),
(15, 'li@example.com', 'oliver@example.com', '2025-06-08T11:00', '2025-06-08T12:00', 'COMP1010'),
(16, 'david@example.com', 'paula@example.com', '2025-06-09T08:30', '2025-06-09T09:30', 'PHYS1050'),
(17, 'alice@example.com', 'quentin@example.com', '2025-06-09T13:00', '2025-06-09T14:00', 'COMP1010'),
(18, 'bob@example.com', 'rachel@example.com', '2025-06-10T10:30', '2025-06-10T11:30', 'MATH1500'),
(19, 'amrit@example.com', 'sam@example.com', '2025-06-10T15:00', '2025-06-10T16:00', 'PHYS1050'),
(20, 'sukhdeep@example.com', 'tina@example.com', '2025-06-11T09:00', '2025-06-11T10:00', 'COMP1010');

COMMIT;