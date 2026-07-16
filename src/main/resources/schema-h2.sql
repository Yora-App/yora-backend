CREATE TABLE IF NOT EXISTS messages(
    id  VARCHAR(60) DEFAULT RANDOM_UUID() PRIMARY KEY,
    text VARCHAR    NOT NULL
);

CREATE TABLE IF NOT EXISTS receipts(
    id              UUID         DEFAULT RANDOM_UUID() PRIMARY KEY,
    content_type    VARCHAR      NOT NULL,
    status          VARCHAR      NOT NULL,
    file_path       VARCHAR      NOT NULL,
    uploaded_at     TIMESTAMP WITH TIME ZONE  NOT NULL,
    s3_storage_id   VARCHAR
);
