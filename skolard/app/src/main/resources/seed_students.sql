BEGIN TRANSACTION;

INSERT OR IGNORE INTO student (name, email)
VALUES ('Alice', 'alice@example.com');

INSERT OR IGNORE INTO student (name, email)
VALUES ('Bob', 'bob@example.com');

COMMIT;
