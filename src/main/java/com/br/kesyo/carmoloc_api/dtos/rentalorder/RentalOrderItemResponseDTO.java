package com.br.kesyo.carmoloc_api.dtos.rentalorder;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class RentalOrderItemResponseDTO {

    private UUID id;
    private UUID equipmentId;
    private String equipmentName;
    private Integer quantity;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int wholeDays;
    private boolean halfDayIncrement;
    private BigDecimal dailyPriceSnapshot;
    private BigDecimal halfDayPriceSnapshot;
    private BigDecimal subtotal;
}
