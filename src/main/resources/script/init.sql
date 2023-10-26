CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION pgcrypto;

CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL,
    login    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT users_uq UNIQUE (login)
);

CREATE TABLE IF NOT EXISTS sessions
(
    id         UUID      DEFAULT uuid_generate_v4(),
    user_id    BIGINT NOT NULL,
    expires_at TIMESTAMP DEFAULT NOW() + INTERVAL '1 minute',
    CONSTRAINT sessions_pk PRIMARY KEY (id),
    CONSTRAINT sessions_users_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE 
);

CREATE TABLE IF NOT EXISTS locations
(
    id        BIGSERIAL,
    name      VARCHAR(255)   NOT NULL,
    user_id   BIGINT         NOT NULL,
    latitude  NUMERIC(9, 7)  NOT NULL,
    longitude NUMERIC(10, 7) NOT NULL,
    CONSTRAINT locations_pk PRIMARY KEY (id),
    CONSTRAINT location_uq UNIQUE (latitude, longitude, user_id),
    CONSTRAINT locations_users_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

INSERT INTO users(login, password)
VALUES ('user-0', crypt('password', gen_salt('bf')));

INSERT INTO sessions(user_id)
VALUES (1);

DROP TABLE users;
DROP TABLE sessions;
SELECT id
FROM users
WHERE login = 'user-0'
  AND password = crypt('password', password);

SELECT *
FROM users;
SELECT *
FROM sessions;
DELETE
FROM users
WHERE id > 1;