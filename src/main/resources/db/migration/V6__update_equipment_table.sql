ALTER TABLE equipment
    DROP COLUMN total_quantity;

ALTER TABLE equipment
    ADD COLUMN half_day_price NUMERIC(10,2);

ALTER TABLE equipment
    ALTER COLUMN daily_price SET NOT NULL;

ALTER TABLE equipment
    ADD CONSTRAINT chk_equipment_half_day_price
        CHECK (
                pricing_type <> 'DAY_AND_HALF'
                    OR half_day_price IS NOT NULL
            );