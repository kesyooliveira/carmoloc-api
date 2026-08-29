CREATE TABLE equipment (
    id             UUID PRIMARY KEY,
    name           VARCHAR(150)   NOT NULL,
    description    VARCHAR(500),
    category       VARCHAR(30)    NOT NULL,
    pricing_type   VARCHAR(10)    NOT NULL,
    daily_price    NUMERIC(10,2),
    hourly_price   NUMERIC(10,2),
    total_quantity INTEGER        NOT NULL CHECK (total_quantity >= 0),
    status         VARCHAR(20)    NOT NULL,
    active         BOOLEAN        NOT NULL DEFAULT true,
    created_at     TIMESTAMP      NOT NULL,
    updated_at     TIMESTAMP      NOT NULL
);

CREATE INDEX idx_equipment_category ON equipment (category);
CREATE INDEX idx_equipment_active ON equipment (active);