CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       login VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL
);

INSERT INTO users (login, password) VALUES ('user1', 'password1');
INSERT INTO users (login, password) VALUES ('user2', 'password2');
INSERT INTO users (login, password) VALUES ('user3', 'password3');
INSERT INTO users (login, password) VALUES ('user4', 'password4');
