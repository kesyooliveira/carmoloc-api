package com.br.kesyo.carmoloc_api.services;

import com.br.kesyo.carmoloc_api.common.PageResponseDTO;
import com.br.kesyo.carmoloc_api.dtos.equipment.EquipmentRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.equipment.EquipmentResponseDTO;
import com.br.kesyo.carmoloc_api.dtos.equipment.EquipmentUnitResponseDTO;
import com.br.kesyo.carmoloc_api.enums.EquipmentUnitStatusEnum;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface EquipmentService {

    EquipmentResponseDTO create(EquipmentRequestDTO request);

    EquipmentResponseDTO update(UUID id, EquipmentRequestDTO request);

    EquipmentResponseDTO findById(UUID id);

    List<EquipmentResponseDTO> findAllActive();

    PageResponseDTO<EquipmentResponseDTO> findAllActivePaged(Pageable pageable);

    void delete(UUID id);

    List<EquipmentUnitResponseDTO> findUnitsByEquipmentId(UUID equipmentId);

    List<EquipmentUnitResponseDTO> addUnits(UUID equipmentId, Integer quantity, List<String> assetCodes);

    EquipmentUnitResponseDTO updateUnitStatus(UUID unitId, EquipmentUnitStatusEnum status, String maintenanceNote);
}
