package com.br.kesyo.carmoloc_api.dtos.equipment;

import com.br.kesyo.carmoloc_api.enums.EquipmentStatusEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class EquipmentUnitResponseDTO {

    private UUID id;
    private String assetCode;
    private EquipmentStatusEnum status;
    private String maintenanceNote;
}
