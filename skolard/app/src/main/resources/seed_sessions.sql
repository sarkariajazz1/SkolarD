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
(15, 'sukhdeep@example.com', 'alice@example.com', '2025-05-14T17:00', '2025-05-14T18:00', 'COMP1010'),
(16, 'sukhdeep@example.com', 'alice@example.com', '2025-05-15T09:30', '2025-05-15T10:30', 'COMP1010'),
(17, 'sukhdeep@example.com', 'alice@example.com', '2025-05-15T11:00', '2025-05-15T12:00', 'COMP1010'),
(18, 'sukhdeep@example.com', 'alice@example.com', '2025-05-16T14:00', '2025-05-16T15:00', 'COMP1010'),
(19, 'sukhdeep@example.com', 'alice@example.com', '2025-05-17T16:00', '2025-05-17T17:00', 'COMP1010'),
(20, 'li@example.com', NULL, '2025-06-11T08:00', '2025-06-11T09:00', 'COMP1010'),
(21, 'alice@example.com', NULL, '2025-06-12T10:00', '2025-06-12T11:00', 'COMP1010'),
(22, 'bob@example.com', NULL, '2025-06-13T13:00', '2025-06-13T14:00', 'COMP1010'),

-- Unbooked sessions from different tutors for different courses
(23, 'li@example.com', NULL, '2025-06-13T09:00', '2025-06-13T10:00', 'MATH1500'),
(24, 'alice@example.com', NULL, '2025-06-14T11:00', '2025-06-14T12:00', 'STAT2000'),
(25, 'bob@example.com', NULL, '2025-06-15T14:00', '2025-06-15T15:00', 'PHYS1050'),

-- Booked COMP1010 sessions with different tutors
(26, 'li@example.com', 'test@example.com', '2025-05-01T10:00', '2025-05-01T11:00', 'COMP1010'),
(27, 'alice@example.com', 'test@example.com', '2025-05-02T10:00', '2025-05-02T11:00', 'COMP1010'),

-- Booked sessions for different courses
(28, 'bob@example.com', 'test@example.com', '2025-05-03T09:00', '2025-05-03T10:00', 'STAT2000'),
(29, 'alice@example.com', 'test@example.com', '2025-05-04T14:00', '2025-05-04T15:00', 'MATH1500'),

-- Future unbooked duplicate course to test sorting consistency
(30, 'li@example.com', NULL, '2025-06-20T09:00', '2025-06-20T10:00', 'COMP1010'),
(31, 'alice@example.com', NULL, '2025-06-21T11:00', '2025-06-21T12:00', 'COMP1010'),
(32, 'amrit@example.com', NULL, '2025-06-18T09:00', '2025-06-18T10:00', 'PHYS1050'),
(33, 'david@example.com', NULL, '2025-06-18T10:00', '2025-06-18T11:00', 'PHYS1050'),
(34, 'maria@example.com', NULL, '2025-06-18T11:00', '2025-06-18T12:00', 'MATH1500'),
(35, 'bob@example.com', NULL, '2025-06-19T13:00', '2025-06-19T14:00', 'STAT2000'),
(36, 'alice@example.com', NULL, '2025-06-19T14:00', '2025-06-19T15:00', 'COMP2140'),
(37, 'li@example.com', NULL, '2025-06-20T11:00', '2025-06-20T12:00', 'COMP3020'),
(38, 'sukhdeep@example.com', NULL, '2025-06-21T09:00', '2025-06-21T10:00', 'COMP4350'),

-- Booked sessions for those same subjects
(39, 'amrit@example.com', 'test@example.com', '2025-05-01T09:00', '2025-05-01T10:00', 'PHYS1050'),
(40, 'david@example.com', 'alice@example.com', '2025-05-02T10:00', '2025-05-02T11:00', 'PHYS1050'),
(41, 'maria@example.com', 'bob@example.com', '2025-05-03T11:00', '2025-05-03T12:00', 'MATH1500'),
(42, 'bob@example.com', 'li@example.com', '2025-05-04T13:00', '2025-05-04T14:00', 'STAT2000'),
(43, 'alice@example.com', 'maria@example.com', '2025-05-05T14:00', '2025-05-05T15:00', 'COMP2140'),
(44, 'li@example.com', 'bob@example.com', '2025-05-06T15:00', '2025-05-06T16:00', 'COMP3020'),
(45, 'sukhdeep@example.com', 'amrit@example.com', '2025-05-07T16:00', '2025-05-07T17:00', 'COMP4350');
COMMIT;
