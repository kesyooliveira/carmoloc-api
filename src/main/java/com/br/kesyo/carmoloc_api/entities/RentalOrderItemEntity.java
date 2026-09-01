package com.br.kesyo.carmoloc_api.entities;

import com.br.kesyo.carmoloc_api.common.BaseEntity;
import com.br.kesyo.carmoloc_api.enums.PricingTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rental_order_item")
public class RentalOrderItemEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "rental_order_id", nullable = false)
    private RentalOrderEntity rentalOrder;

    @ManyToOne(optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private EquipmentEntity equipment;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "whole_days", nullable = false)
    private int wholeDays;

    @Column(name = "half_day_increment", nullable = false)
    private boolean halfDayIncrement = false;

    @Column(name = "daily_price_snapshot", precision = 10, scale = 2, nullable = false)
    private BigDecimal dailyPriceSnapshot;

    @Column(name = "half_day_price_snapshot", precision = 10, scale = 2)
    private BigDecimal halfDayPriceSnapshot;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    public void applyRentalPeriod(LocalDateTime start, LocalDateTime end) {
        if (equipment.getPricingType() == PricingTypeEnum.DAILY_ONLY) {
            applyDailyOnlyPeriod(start, end);
        } else {
            applyDailyAndHalfPeriod(start, end);
        }
        this.startDateTime = start;
        this.endDateTime = end;
    }

    private void applyDailyOnlyPeriod(LocalDateTime start, LocalDateTime end) {
        long days = ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate()) + 1;
        this.wholeDays = (int) Math.max(days, 1);
        this.halfDayIncrement = false;
    }

    private void applyDailyAndHalfPeriod(LocalDateTime start, LocalDateTime end) {
        long totalHours = Duration.between(start, end).toHours();
        long fullDays = totalHours / 24;
        long remainderHours = totalHours % 24;

        if (remainderHours == 0) {
            this.wholeDays = (int) fullDays;
            this.halfDayIncrement = false;
        } else if (remainderHours <= 12) {
            this.wholeDays = (int) fullDays;
            this.halfDayIncrement = true;
        } else {
            this.wholeDays = (int) fullDays + 1;
            this.halfDayIncrement = false;
        }
    }

    public void calculateSubtotal() {
        BigDecimal daysAmount = dailyPriceSnapshot.multiply(BigDecimal.valueOf(wholeDays));
        BigDecimal halfDayAmount = (halfDayIncrement && halfDayPriceSnapshot != null)
                ? halfDayPriceSnapshot
                : BigDecimal.ZERO;
        this.subtotal = daysAmount.add(halfDayAmount).multiply(BigDecimal.valueOf(quantity));
    }

}
