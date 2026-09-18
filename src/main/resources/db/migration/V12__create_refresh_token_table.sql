CREATE TABLE refresh_token (
    id          UUID          PRIMARY KEY,
    token       VARCHAR(255)  NOT NULL,
    user_id     UUID          NOT NULL,
    expiry_date TIMESTAMP     NOT NULL,
    revoked     BOOLEAN       NOT NULL DEFAULT false,
    active      BOOLEAN       NOT NULL DEFAULT true,
    created_at  TIMESTAMP     NOT NULL,
    updated_at  TIMESTAMP     NOT NULL,

    CONSTRAINT uq_refresh_token_token UNIQUE (token),
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE INDEX idx_refresh_token_user ON refresh_token (user_id);