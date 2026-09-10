package com.br.kesyo.carmoloc_api.dtos.equipment;

import com.br.kesyo.carmoloc_api.enums.EquipmentUnitStatusEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class EquipmentUnitResponseDTO {

    private UUID id;
    private Instant createdAt;
    private String assetCode;
    private EquipmentUnitStatusEnum status;
    private String maintenanceNote;
}
