package com.br.kesyo.carmoloc_api.dtos.rentalorder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RentalOrderRequestDTO {

    @NotNull(message = "clientId is required")
    private UUID clientId;

    @NotEmpty(message = "items must contain at least one item")
    @Valid
    private List<RentalOrderItemRequestDTO> items;
}
