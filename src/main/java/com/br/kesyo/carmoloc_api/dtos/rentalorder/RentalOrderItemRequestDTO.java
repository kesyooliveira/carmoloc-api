package com.br.kesyo.carmoloc_api.dtos.rentalorder;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class RentalOrderItemRequestDTO {

    @NotNull(message = "equipmentId id required")
    private UUID equipmentId;

    @NotNull(message = "quantity id required")
    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "startDateTime is required")
    private LocalDateTime startDateTime;

    @NotNull(message = "endDateTime is required")
    private LocalDateTime endDateTime;
}
