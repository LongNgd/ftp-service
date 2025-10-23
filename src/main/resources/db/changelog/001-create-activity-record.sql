CREATE TABLE IF NOT EXISTS activity_record (
    id BIGSERIAL PRIMARY KEY,
    file_path VARCHAR(500),
    username VARCHAR(100),
    date_time TIMESTAMP,
    activity VARCHAR(50)
);
