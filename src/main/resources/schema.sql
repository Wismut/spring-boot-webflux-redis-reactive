CREATE TABLE IF NOT EXISTS book
(
    id    SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn  VARCHAR(20)  NOT NULL
);
-- CREATE TABLE IF NOT EXISTS company
-- (
--     id    SERIAL PRIMARY KEY,
--     name  VARCHAR(255) NOT NULL
-- );
