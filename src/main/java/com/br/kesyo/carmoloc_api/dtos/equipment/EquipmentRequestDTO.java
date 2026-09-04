package com.br.kesyo.carmoloc_api.dtos.equipment;

import com.br.kesyo.carmoloc_api.enums.EquipmentCategoryEnum;
import com.br.kesyo.carmoloc_api.enums.PricingTypeEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class EquipmentRequestDTO {

    @NotBlank(message = "name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "category is required")
    private EquipmentCategoryEnum category;

    @NotNull(message = "pricingType is required")
    private PricingTypeEnum pricingType;

    @NotNull(message = "dailyPrice is required")
    @DecimalMin(value = "0.01", message = "dailyPrice must be greater than zero")
    private BigDecimal dailyPrice;

    @DecimalMin(value = "0.01", message = "halfDayPrice must be greater than zero")
    private BigDecimal halfDayPrice;

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity;

    // opcional, se informado deve ter exatamento o tamanho de quantity
    // se omitido, os códigos serão gerados automaticamente
    private List<@NotBlank String> assetCodes;
}
