BEGIN TRANSACTION;

INSERT OR IGNORE INTO support_ticket (
    requester_email,
    requester_role,
    title,
    description,
    created_at,
    closed_at,
    is_handled
) VALUES
('raj@example.com', 'student',
 'App crashing on login',
 'The app crashes every time I try to log in on my phone.',
 '2025-06-01T10:00:00', NULL, 0),

('amrit@example.com', 'tutor',
 'Missing session data',
 'My last tutoring session isnt showing up in the dashboard.',
 '2025-06-01T11:15:00', NULL, 0),

('simran@example.com', 'student',
 'Unable to message tutor',
 'I can’t send messages through the chat screen.',
 '2025-06-01T08:30:00', '2025-06-01T09:45:00', 1);

COMMIT;
