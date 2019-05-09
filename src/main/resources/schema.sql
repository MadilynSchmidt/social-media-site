CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    email_address VARCHAR(200) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    first_name TEXT,
    last_name TEXT,
    location TEXT
);

CREATE TABLE friend_request(
    id BIGSERIAL PRIMARY KEY,
    sender_id BIGINT NOT NULL REFERENCES users(id),
    recipient_id BIGINT NOT NULL REFERENCES users(id),
    status VARCHAR(8) NOT NULL
    );