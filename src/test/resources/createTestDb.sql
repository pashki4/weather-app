CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT AUTO_INCREMENT,
    login    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id),
    CONSTRAINT users_uq UNIQUE (login)
);

CREATE TABLE IF NOT EXISTS sessions
(
    id         UUID      DEFAULT RANDOM_UUID(),
    user_id    BIGINT NOT NULL,
    expires_at TIMESTAMP DEFAULT NOW() + INTERVAL '1' MINUTE,
    CONSTRAINT sessions_pk PRIMARY KEY (id),
    CONSTRAINT sessions_users_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_expire_at ON sessions (user_id);

CREATE TABLE IF NOT EXISTS locations
(
    id        BIGINT AUTO_INCREMENT,
    name      VARCHAR(255)   NOT NULL,
    user_id   BIGINT         NOT NULL,
    latitude  NUMERIC(9, 7)  NOT NULL,
    longitude NUMERIC(10, 7) NOT NULL,
    CONSTRAINT locations_pk PRIMARY KEY (id),
    CONSTRAINT location_uq UNIQUE (latitude, longitude, user_id),
    CONSTRAINT locations_users_fk FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

INSERT INTO users(login, password)
VALUES ('user-0', 'password-0');

SELECT * FROM users;
