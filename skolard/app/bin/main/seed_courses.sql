BEGIN TRANSACTION;

INSERT OR IGNORE INTO courses (id, name) VALUES
('COMP101', 'Introduction to Computer Science'),
('MATH200', 'Calculus II'),
('PHYS150', 'Physics for Engineers');

COMMIT;
