CREATE TABLE equipment_unit (
    id                UUID          PRIMARY KEY,
    equipment_id      UUID          NOT NULL,
    asset_code        VARCHAR(30),
    status            VARCHAR(20)   NOT NULL,
    maintenance_note  VARCHAR(300),
    active            BOOLEAN       NOT NULL DEFAULT true,
    created_at        TIMESTAMP     NOT NULL,
    updated_at        TIMESTAMP     NOT NULL,

    CONSTRAINT fk_equipment_unit_equipment
        FOREIGN KEY (equipment_id) REFERENCES equipment (id)
);

CREATE INDEX idx_equipment_unit_equipment ON equipment_unit (equipment_id);
CREATE INDEX idx_equipment_unit_status ON equipment_unit (equipment_id, status);