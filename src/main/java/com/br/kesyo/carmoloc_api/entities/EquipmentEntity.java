package com.br.kesyo.carmoloc_api.entities;

import com.br.kesyo.carmoloc_api.common.BaseEntity;
import com.br.kesyo.carmoloc_api.enums.EquipmentCategoryEnum;
import com.br.kesyo.carmoloc_api.enums.EquipmentStatusEnum;
import com.br.kesyo.carmoloc_api.enums.PricingTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "equipment")
public class EquipmentEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentCategoryEnum category;

    @Enumerated(EnumType.STRING)
    @Column(name = "pricing_type", nullable = false)
    private PricingTypeEnum pricingType;

    @Column(name = "daily_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal dailyPrice;

    @Column(name = "half_day_price", precision = 10, scale = 2)
    private BigDecimal halfDayPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentStatusEnum status = EquipmentStatusEnum.AVAILABLE;

    public boolean hasValidPricingConfiguration() {
        if (pricingType == PricingTypeEnum.DAY_AND_HALF) {
            return halfDayPrice != null;
        }
        return true;
    }

    public boolean isAvailableForRental() {
        return status == EquipmentStatusEnum.AVAILABLE && isActive();
    }
}
