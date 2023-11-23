CREATE EXTENSION "uuid-ossp";

CREATE TABLE users
(
    id       BIGSERIAL,
    login    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT users_uq UNIQUE (login)
);

CREATE TABLE sessions
(
    id         UUID      DEFAULT uuid_generate_v4(),
    user_id    BIGINT NOT NULL,
    expires_at TIMESTAMP DEFAULT NOW() + INTERVAL '30 minute',
    CONSTRAINT sessions_pk PRIMARY KEY (id),
    CONSTRAINT sessions_users_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_user_id ON sessions (user_id);

CREATE TABLE locations
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