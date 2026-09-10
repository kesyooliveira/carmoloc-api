package com.br.kesyo.carmoloc_api.services.impl;

import com.br.kesyo.carmoloc_api.dtos.equipment.EquipmentRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.equipment.EquipmentResponseDTO;
import com.br.kesyo.carmoloc_api.dtos.equipment.EquipmentUnitResponseDTO;
import com.br.kesyo.carmoloc_api.entities.EquipmentEntity;
import com.br.kesyo.carmoloc_api.entities.EquipmentUnitEntity;
import com.br.kesyo.carmoloc_api.enums.EquipmentUnitStatusEnum;
import com.br.kesyo.carmoloc_api.exceptions.EquipmentNotFoundException;
import com.br.kesyo.carmoloc_api.exceptions.EquipmentUnitNotFoundException;
import com.br.kesyo.carmoloc_api.exceptions.InvalidAssetCodeListException;
import com.br.kesyo.carmoloc_api.exceptions.InvalidPricingConfigurationException;
import com.br.kesyo.carmoloc_api.mapper.EquipmentMapper;
import com.br.kesyo.carmoloc_api.repositories.EquipmentRepository;
import com.br.kesyo.carmoloc_api.repositories.EquipmentUnitRepository;
import com.br.kesyo.carmoloc_api.services.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentUnitRepository equipmentUnitRepository;
    private final AssetCodeGenerator assetCodeGenerator;

    @Override
    @Transactional
    public EquipmentResponseDTO create(EquipmentRequestDTO request) {
        EquipmentEntity entity = EquipmentMapper.toEntity(request);

        if (!entity.hasValidPricingConfiguration()) {
            throw new InvalidPricingConfigurationException(
                "halfDayPrice é obrigatório quando pricingType é DAY_AND_HALF"
            );
        }

        EquipmentEntity saved = this.equipmentRepository.save(entity);

        List<String> assetCodes = resolveAssetCodes(request, saved);
        this.createUnits(saved, assetCodes);

        return this.toResponseDTO(saved);
    }

    private List<String> resolveAssetCodes(EquipmentRequestDTO request, EquipmentEntity equipment) {
        List<String> provided = request.getAssetCodes();

        if (provided == null || provided.isEmpty()) {
            return assetCodeGenerator.generate(equipment.getCategory(), request.getQuantity());
        }

        if (provided.size() != request.getQuantity()) {
            throw new InvalidAssetCodeListException(
                "A quantidade de assetCodes informados (%d) não bate com quantity (%d)"
                    .formatted(provided.size(), request.getQuantity()));
        }

        return provided;
    }

    private void createUnits(EquipmentEntity equipment, List<String> assetCodes) {
        List<EquipmentUnitEntity> units = assetCodes.stream()
                .map(code -> {
                    EquipmentUnitEntity unit = new EquipmentUnitEntity();
                    unit.setEquipment(equipment);
                    unit.setAssetCode(code);
                    unit.setStatus(EquipmentUnitStatusEnum.AVAILABLE);
                    return unit;
                })
                .toList();

        this.equipmentUnitRepository.saveAll(units);
    }

    @Override
    @Transactional
    public EquipmentResponseDTO update(UUID id, EquipmentRequestDTO request) {
        EquipmentEntity entity = this.findEntityById(id);
        EquipmentMapper.applyToEntity(request, entity);

        if (!entity.hasValidPricingConfiguration()) {
            throw new InvalidPricingConfigurationException(
                "halfDayPrice é obrigatório quando pricingType é DAY_AND_HALF"
            );
        }

        EquipmentEntity saved = this.equipmentRepository.save(entity);
        return this.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public EquipmentResponseDTO findById(UUID id) {
        return this.toResponseDTO(this.findEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponseDTO> findAllActive() {
        return this.equipmentRepository.findByActiveTrue()
            .stream()
            .map(this::toResponseDTO)
            .toList();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        EquipmentEntity entity = this.findEntityById(id);
        entity.setActive(false);

        List<EquipmentUnitEntity> units = this.equipmentUnitRepository.findByEquipmentIdAndActiveTrue(id);
        units.forEach(unit -> unit.setActive(false));

        this.equipmentRepository.save(entity);
        if (!units.isEmpty()) {
            this.equipmentUnitRepository.saveAll(units);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentUnitResponseDTO> findUnitsByEquipmentId(UUID equipmentId) {
        this.findEntityById(equipmentId);
        return this.equipmentUnitRepository.findByEquipmentIdAndActiveTrue(equipmentId)
            .stream()
            .map(EquipmentMapper::toUnitResponseDTO)
            .toList();
    }

    @Override
    @Transactional
    public EquipmentUnitResponseDTO updateUnitStatus(UUID unitId, EquipmentUnitStatusEnum status, String maintenanceNote) {
        EquipmentUnitEntity unit = this.equipmentUnitRepository.findById(unitId)
            .orElseThrow(() -> new EquipmentUnitNotFoundException(unitId));

        unit.setStatus(status);
        unit.setMaintenanceNote(maintenanceNote);

        EquipmentUnitEntity saved = this.equipmentUnitRepository.save(unit);
        return EquipmentMapper.toUnitResponseDTO(saved);
    }

    @Override
    @Transactional
    public List<EquipmentUnitResponseDTO> addUnits(UUID equipmentId, Integer quantity, List<String> assetCodes) {
        EquipmentEntity equipment = this.findEntityById(equipmentId);

        List<String> codes = this.resolverAssetCodesForAddition(equipment, quantity, assetCodes);
        this.createUnits(equipment, codes);

        return this.findUnitsByEquipmentId(equipmentId);
    }

    private List<String> resolverAssetCodesForAddition(EquipmentEntity equipment, Integer quantity, List<String> assetCodes) {
        if (assetCodes == null || assetCodes.isEmpty()) {
            return this.assetCodeGenerator.generate(equipment.getCategory(), quantity);
        }

        if (assetCodes.size() != quantity) {
            throw new InvalidAssetCodeListException(
                "A quantidade de assetCodes informados (%d) não bate com quantity (%d)"
                    .formatted(assetCodes.size(), quantity));
        }

        return assetCodes;
    }

    private EquipmentResponseDTO toResponseDTO(EquipmentEntity entity) {
        int total = this.equipmentUnitRepository.countByEquipmentIdAndActiveTrue(entity.getId());
        int available = this.equipmentUnitRepository.countAvailableUnits(entity.getId());
        return EquipmentMapper.toResponseDTO(entity, total, available);
    }

    private EquipmentEntity findEntityById(UUID id) {
        return this.equipmentRepository.findById(id)
                .orElseThrow(() -> new EquipmentNotFoundException(id));
    }
}
