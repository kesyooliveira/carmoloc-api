package com.br.kesyo.carmoloc_api.entities;

import com.br.kesyo.carmoloc_api.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rental_order_item")
public class RentalOrderItemEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "rental_order_id", nullable = false)
    private RentalOrderEntity rentalOrder;

    @ManyToOne(optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private EquipmentEntity equipament;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "unit_price_snapshot", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPriceSnapshot;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    public void calculateSubtotal(long durationDays) {
        long units = switch (equipament.getPricingType()) {
            case DAILY -> ChronoUnit.DAYS.between(startDateTime.toLocalDate(), endDateTime.toLocalDate()) + 1;
            case HOURLY -> Duration.between(startDateTime, endDateTime).toHours();
        };
        this.subtotal = unitPriceSnapshot
                .multiply(BigDecimal.valueOf(durationDays))
                .multiply(BigDecimal.valueOf(quantity));
    }

}
