CREATE TABLE client (
    id            UUID PRIMARY KEY,
    name          VARCHAR(150) NOT NULL,
    description    VARCHAR(500),
    document_type VARCHAR(10)  NOT NULL,
    document      VARCHAR(14)  NOT NULL,
    phone         VARCHAR(20)  NOT NULL,
    email         VARCHAR(150),
    street        VARCHAR(150),
    number        VARCHAR(20),
    neighborhood  VARCHAR(100),
    city          VARCHAR(100),
    state         VARCHAR(2),
    zip_code      VARCHAR(8),
    active        BOOLEAN      NOT NULL DEFAULT true,
    created_at    TIMESTAMP    NOT NULL,
    updated_at    TIMESTAMP    NOT NULL,

    CONSTRAINT uq_client_document UNIQUE (document)
);