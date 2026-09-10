package com.br.kesyo.carmoloc_api.dtos.equipment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddEquipmentUnitsRequestDTO {

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity;

    // opcional, se informado deve ter exatamento o tamanho de quantity
    // se omitido, os códigos serão gerados automaticamente
    private List<@NotBlank String> assetCodes;
}
