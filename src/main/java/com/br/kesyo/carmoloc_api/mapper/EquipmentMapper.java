package com.br.kesyo.carmoloc_api.mapper;

import com.br.kesyo.carmoloc_api.dtos.equipment.EquipmentRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.equipment.EquipmentResponseDTO;
import com.br.kesyo.carmoloc_api.dtos.equipment.EquipmentUnitResponseDTO;
import com.br.kesyo.carmoloc_api.entities.EquipmentEntity;
import com.br.kesyo.carmoloc_api.entities.EquipmentUnitEntity;

public class EquipmentMapper {

    private EquipmentMapper() {}

    public static EquipmentEntity toEntity(EquipmentRequestDTO dto) {
        EquipmentEntity entity = new EquipmentEntity();
        applyToEntity(dto, entity);
        return entity;
    }

    public static void applyToEntity(EquipmentRequestDTO dto, EquipmentEntity entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setCategory(dto.getCategory());
        entity.setPricingType(dto.getPricingType());
        entity.setDailyPrice(dto.getDailyPrice());
        entity.setHalfDayPrice(dto.getHalfDayPrice());
    }

    public static EquipmentResponseDTO toResponseDTO(EquipmentEntity entity, int totalUnits, int availableUnits) {
        return EquipmentResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .pricingType(entity.getPricingType())
                .dailyPrice(entity.getDailyPrice())
                .halfDayPrice(entity.getHalfDayPrice())
                .status(entity.getStatus())
                .totalUnits(totalUnits)
                .availableUnits(availableUnits)
                .active(entity.isActive())
                .build();
    }

    public static EquipmentUnitResponseDTO toUnitResponseDTO(EquipmentUnitEntity entity) {
        return EquipmentUnitResponseDTO.builder()
            .id(entity.getId())
            .assetCode(entity.getAssetCode())
            .status(entity.getStatus())
            .maintenanceNote(entity.getMaintenanceNote())
            .build();
    }
}
