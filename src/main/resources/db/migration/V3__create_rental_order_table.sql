CREATE TABLE rental_order (
    id           UUID PRIMARY KEY,
    client_id    UUID           NOT NULL,
    status       VARCHAR(20)    NOT NULL,
    total_amount NUMERIC(10,2)  NOT NULL DEFAULT 0,
    active       BOOLEAN        NOT NULL DEFAULT true,
    created_at   TIMESTAMP      NOT NULL,
    updated_at   TIMESTAMP      NOT NULL,

    CONSTRAINT fk_rental_order_client
      FOREIGN KEY (client_id) REFERENCES client (id)
);

CREATE INDEX idx_rental_order_client ON rental_order (client_id);
CREATE INDEX idx_rental_order_status ON rental_order (status);