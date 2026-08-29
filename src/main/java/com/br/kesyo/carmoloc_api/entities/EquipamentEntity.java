package com.br.kesyo.carmoloc_api.entities;

import com.br.kesyo.carmoloc_api.common.BaseEntity;
import com.br.kesyo.carmoloc_api.enums.EquipamentCategoryEnum;
import com.br.kesyo.carmoloc_api.enums.EquipamentStatusEnum;
import com.br.kesyo.carmoloc_api.enums.PricingTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "equipament")
public class EquipamentEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipamentCategoryEnum category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PricingTypeEnum pricingType;

    @Column(name = "daily_price", precision = 10, scale = 2)
    private BigDecimal dailyPrice;

    @Column(name = "hourly_price", precision = 10, scale = 2)
    private BigDecimal hourlyPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipamentStatusEnum status = EquipamentStatusEnum.AVAILABLE;

    public BigDecimal getApplicablePrice() {
        return switch (pricingType) {
            case DAILY -> dailyPrice;
            case HOURLY -> hourlyPrice;
        };
    }

    public boolean isAvailableForRental() {
        return status == EquipamentStatusEnum.AVAILABLE && isActive();
    }
}
