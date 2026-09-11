package com.br.kesyo.carmoloc_api.services.impl;

import com.br.kesyo.carmoloc_api.exceptions.InsufficientAvailabilityException;
import com.br.kesyo.carmoloc_api.repositories.EquipmentUnitRepository;
import com.br.kesyo.carmoloc_api.repositories.RentalOrderItemRepository;
import com.br.kesyo.carmoloc_api.services.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final EquipmentUnitRepository equipmentUnitRepository;
    private final RentalOrderItemRepository rentalOrderItemRepository;

    @Override
    @Transactional(readOnly = true)
    public int getAvailableQuantity(UUID equipmentId, LocalDateTime start, LocalDateTime end) {
        int operationalUnits = this.equipmentUnitRepository.countAvailableUnits(equipmentId);
        int reserved = this.rentalOrderItemRepository.sumReservedQuantity(equipmentId, start, end);
        return operationalUnits - reserved;
    }

    @Override
    @Transactional(readOnly = true)
    public void ensureAvailability(UUID equipmentId, LocalDateTime start, LocalDateTime end, int requestedQuantity) {
        int available = this.getAvailableQuantity(equipmentId, start, end);
        if (available < requestedQuantity) {
            throw new InsufficientAvailabilityException(equipmentId, requestedQuantity, available);
        }
    }
}
