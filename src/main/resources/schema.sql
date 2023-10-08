CREATE TABLE IF NOT EXISTS "user"
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    api_key  VARCHAR(255),
    role     VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS company
(
    id     SERIAL PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    symbol VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS stock
(
    id          SERIAL PRIMARY KEY,
    company_id  SERIAL REFERENCES company (id),
    name        VARCHAR(255) NOT NULL,
    code        VARCHAR(255) NOT NULL,
    price       REAL         NOT NULL,
    time_update TIMESTAMP    NOT NULL
);
