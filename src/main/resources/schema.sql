CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY,
    email_address VARCHAR(200) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    location TEXT NOT NULL
);

CREATE TABLE friend_request(
    id BIGSERIAL PRIMARY KEY,
    sender_id BIGINT NOT NULL REFERENCES users(id),
    recipient_id BIGINT NOT NULL REFERENCES users(id),
    status VARCHAR(8) NOT NULL
    );

CREATE TABLE post(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    content TEXT NOT NULL
);