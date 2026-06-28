CREATE TABLE IF NOT EXISTS messages(
    id      VARCHAR(60)     DEFAULT uuidv4() PRIMARY KEY,
    text    VARCHAR         NOT NULL
);