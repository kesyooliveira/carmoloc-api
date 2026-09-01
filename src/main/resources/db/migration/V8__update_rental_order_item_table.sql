ALTER TABLE rental_order_item
    DROP COLUMN unit_price_snapshot;

ALTER TABLE rental_order_item
    ADD COLUMN daily_price_snapshot NUMERIC(10,2),
    ADD COLUMN half_day_price_snapshot NUMERIC(10,2);

ALTER TABLE rental_order_item
    ALTER COLUMN daily_price_snapshot SET NOT NULL;

ALTER TABLE rental_order_item
    ADD COLUMN whole_days INTEGER NOT NULL DEFAULT 0;

ALTER TABLE rental_order_item
    ALTER COLUMN whole_days DROP DEFAULT;

ALTER TABLE rental_order_item
    ADD COLUMN half_day_increment BOOLEAN NOT NULL DEFAULT false;

ALTER TABLE rental_order_item
    ADD CONSTRAINT chk_rental_order_item_billed_period
        CHECK (whole_days > 0 OR half_day_increment = true);

ALTER TABLE rental_order_item
    ADD CONSTRAINT chk_rental_order_item_half_day_price
        CHECK (half_day_increment = false OR half_day_price_snapshot IS NOT NULL);