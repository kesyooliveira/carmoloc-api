package com.br.kesyo.carmoloc_api.dtos.rentalorder;

import com.br.kesyo.carmoloc_api.enums.RentalOrderStatusEnum;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class RentalOrderResponseDTO {

    private UUID id;
    private UUID clientId;
    private String clientName;
    private RentalOrderStatusEnum status;
    private BigDecimal totalAmount;
    private List<RentalOrderItemResponseDTO> items;
    private Instant createdAt;
}
