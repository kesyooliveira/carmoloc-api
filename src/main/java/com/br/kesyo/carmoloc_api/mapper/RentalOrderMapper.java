package com.br.kesyo.carmoloc_api.mapper;

import com.br.kesyo.carmoloc_api.dtos.rentalorder.RentalOrderItemResponseDTO;
import com.br.kesyo.carmoloc_api.dtos.rentalorder.RentalOrderResponseDTO;
import com.br.kesyo.carmoloc_api.entities.RentalOrderEntity;
import com.br.kesyo.carmoloc_api.entities.RentalOrderItemEntity;

public class RentalOrderMapper {

    private RentalOrderMapper() {}

    public static RentalOrderItemResponseDTO toItemResponseDTO (RentalOrderItemEntity item) {
        return RentalOrderItemResponseDTO.builder()
            .id(item.getId())
            .equipmentId(item.getEquipment().getId())
            .equipmentName(item.getEquipment().getName())
            .quantity(item.getQuantity())
            .startDateTime(item.getStartDateTime())
            .endDateTime(item.getEndDateTime())
            .wholeDays(item.getWholeDays())
            .halfDayIncrement(item.isHalfDayIncrement())
            .dailyPriceSnapshot(item.getDailyPriceSnapshot())
            .halfDayPriceSnapshot(item.getHalfDayPriceSnapshot())
            .subtotal(item.getSubtotal())
            .build();
    }

    public static RentalOrderResponseDTO toResponseDTO(RentalOrderEntity order) {
        return RentalOrderResponseDTO.builder()
            .id(order.getId())
            .clientId(order.getClient().getId())
            .clientName(order.getClient().getName())
            .status(order.getStatus())
            .totalAmount(order.getTotalAmount())
            .items(order.getItems().stream().map(RentalOrderMapper::toItemResponseDTO).toList())
            .createdAt(order.getCreatedAt())
            .build();
    }
}
