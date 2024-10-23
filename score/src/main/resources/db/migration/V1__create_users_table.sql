CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       login VARCHAR(255) NOT NULL,
                       score FLOAT NOT NULL
);

INSERT INTO users (login, score) VALUES ('user1', 0.8);
INSERT INTO users (login, score) VALUES ('user2', 0.5);
INSERT INTO users (login, score) VALUES ('user3', 0.9);
