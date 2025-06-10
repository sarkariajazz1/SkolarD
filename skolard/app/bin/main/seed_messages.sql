BEGIN TRANSACTION;

INSERT INTO messages (timeSent, studentEmail, tutorEmail, senderEmail, message) VALUES 
('2024-01-15T14:30:00', 'alice@example.com', 'sukhdeep@example.com', 'alice@example.com', 'Hello, I have a question about the assignment.'),
('2024-01-15T14:35:00', 'alice@example.com', 'sukhdeep@example.com', 'sukhdeep@example.com', 'Sure, go ahead!'),
('2024-01-16T09:00:00', 'bob@example.com', 'amrit@example.com', 'bob@example.com', 'Can we reschedule our session?'),
('2024-01-16T09:15:00', 'bob@example.com', 'amrit@example.com', 'amrit@example.com', 'Yes, let me know what works for you.');

COMMIT;