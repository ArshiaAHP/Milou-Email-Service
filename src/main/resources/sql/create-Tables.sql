CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE emails (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    body TEXT,
    sent_date DATETIME NOT NULL,
    code VARCHAR(6) NOT NULL UNIQUE,
    sender_id BIGINT NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES users(id)
);

CREATE TABLE email_recipients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isRead BOOLEAN NOT NULL DEFAULT FALSE,
    email_id BIGINT NOT NULL,
    recipient_id BIGINT NOT NULL,
    FOREIGN KEY (email_id) REFERENCES emails(id),
    FOREIGN KEY (recipient_id) REFERENCES users(id),
    UNIQUE (email_id, recipient_id)
);