CREATE TABLE app_user (
    id          UUID          PRIMARY KEY,
    username    VARCHAR(50)   NOT NULL,
    full_name   VARCHAR(150)  NOT NULL,
    email       VARCHAR(150)  NOT NULL,
    password    VARCHAR(255)  NOT NULL,
    role        VARCHAR(20)   NOT NULL,
    active      BOOLEAN       NOT NULL DEFAULT true,
    created_at  TIMESTAMP     NOT NULL,
    updated_at  TIMESTAMP     NOT NULL,

    CONSTRAINT uq_app_user_username UNIQUE (username),
    CONSTRAINT uq_app_user_email UNIQUE (email)
);