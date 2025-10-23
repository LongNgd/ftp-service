CREATE TABLE IF NOT EXISTS file_record (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    path VARCHAR(500),
    owner_id BIGINT,
    owner_type VARCHAR(50)
);
