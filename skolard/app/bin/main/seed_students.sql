BEGIN TRANSACTION;

INSERT OR IGNORE INTO student (email, name, password) VALUES
('alice@example.com', 'Alice', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5'), -- password: alice123
('bob@example.com', 'Bob', 'b86fc6b051f63d73de262d4c34e3a0a9b3c4f1d3747c8c6e7e4a6dff3e6d9f7b'), -- password: bobpass
('simran@example.com', 'Simran Dhillon', '8d969eef6ecad3c29a3a629280e686cff8f3f8e0ab3ebcd1e20b979d53e5a16f'), -- password: simran123
('raj@example.com', 'Raj Gill', 'f7c3bc1d808e04732adf679965ccc34ca7ae3441b90a2e8c9fc6e8b7b3e1f5da'); -- password: raj123

COMMIT;