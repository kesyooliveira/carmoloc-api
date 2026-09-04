package com.br.kesyo.carmoloc_api.dtos.equipment;

import com.br.kesyo.carmoloc_api.enums.EquipmentCategoryEnum;
import com.br.kesyo.carmoloc_api.enums.EquipmentStatusEnum;
import com.br.kesyo.carmoloc_api.enums.PricingTypeEnum;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class EquipmentResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private EquipmentCategoryEnum category;
    private PricingTypeEnum pricingType;
    private BigDecimal dailyPrice;
    private BigDecimal halfDayPrice;
    private EquipmentStatusEnum status;
    private int totalUnits;
    private int availableUnits;
    private boolean active;
}
