CREATE TABLE IF NOT EXISTS messages(
    id      VARCHAR(60)     DEFAULT uuidv4() PRIMARY KEY,
    text    VARCHAR         NOT NULL
);

CREATE TABLE IF NOT EXISTS receipts(
    id              UUID         DEFAULT uuidv4() PRIMARY KEY,
    content_type    VARCHAR      NOT NULL,
    status          VARCHAR      NOT NULL,
    file_path       VARCHAR      NOT NULL,
    uploaded_at     TIMESTAMPTZ  NOT NULL,
    s3_storage_id   VARCHAR
);