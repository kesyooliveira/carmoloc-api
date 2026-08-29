CREATE TABLE rental_order_item (
    id                   UUID          PRIMARY KEY,
    rental_order_id      UUID          NOT NULL,
    equipment_id         UUID          NOT NULL,
    quantity             INTEGER       NOT NULL CHECK (quantity > 0),
    start_date_time      TIMESTAMP     NOT NULL,
    end_date_time        TIMESTAMP     NOT NULL,
    unit_price_snapshot  NUMERIC(10,2) NOT NULL,
    subtotal             NUMERIC(10,2) NOT NULL,
    active               BOOLEAN       NOT NULL DEFAULT true,
    created_at           TIMESTAMP     NOT NULL,
    updated_at           TIMESTAMP     NOT NULL,

    CONSTRAINT fk_rental_order_item_order
       FOREIGN KEY (rental_order_id) REFERENCES rental_order (id),
    CONSTRAINT fk_rental_order_item_equipment
       FOREIGN KEY (equipment_id) REFERENCES equipment (id),
    CONSTRAINT chk_rental_order_item_dates
       CHECK (end_date_time >= start_date_time)
);

CREATE INDEX idx_rental_order_item_order ON rental_order_item (rental_order_id);
CREATE INDEX idx_rental_order_item_equipment_dates
    ON rental_order_item (equipment_id, start_date_time, end_date_time);