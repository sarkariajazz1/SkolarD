BEGIN TRANSACTION;

INSERT OR IGNORE INTO tutor (name, email, bio)
VALUES ('Jane Smith', 'jane@skolard.ca', 'Math enthusiast');

INSERT OR IGNORE INTO tutor (name, email, bio)
VALUES ('John Doe', 'john@skolard.ca', 'CS PhD');

COMMIT;
