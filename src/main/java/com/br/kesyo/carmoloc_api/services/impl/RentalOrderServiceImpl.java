package com.br.kesyo.carmoloc_api.services.impl;

import com.br.kesyo.carmoloc_api.dtos.rentalorder.RentalOrderItemRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.rentalorder.RentalOrderRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.rentalorder.RentalOrderResponseDTO;
import com.br.kesyo.carmoloc_api.entities.ClientEntity;
import com.br.kesyo.carmoloc_api.entities.EquipmentEntity;
import com.br.kesyo.carmoloc_api.entities.RentalOrderEntity;
import com.br.kesyo.carmoloc_api.entities.RentalOrderItemEntity;
import com.br.kesyo.carmoloc_api.enums.RentalOrderStatusEnum;
import com.br.kesyo.carmoloc_api.exceptions.*;
import com.br.kesyo.carmoloc_api.mapper.RentalOrderMapper;
import com.br.kesyo.carmoloc_api.repositories.ClientRepository;
import com.br.kesyo.carmoloc_api.repositories.EquipmentRepository;
import com.br.kesyo.carmoloc_api.repositories.EquipmentUnitRepository;
import com.br.kesyo.carmoloc_api.repositories.RentalOrderRepository;
import com.br.kesyo.carmoloc_api.services.AvailabilityService;
import com.br.kesyo.carmoloc_api.services.RentalOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentalOrderServiceImpl implements RentalOrderService {

    private final RentalOrderRepository rentalOrderRepository;
    private final ClientRepository clientRepository;
    private final EquipmentRepository equipmentRepository;
    private final AvailabilityService availabilityService;
    private final EquipmentUnitRepository equipmentUnitRepository;

    @Override
    @Transactional
    public RentalOrderResponseDTO create(RentalOrderRequestDTO request) {
        ClientEntity client = this.clientRepository.findById(request.getClientId())
            .orElseThrow(() -> new ClientNotFoundException(request.getClientId()));

        for (RentalOrderItemRequestDTO itemRequest : request.getItems()) {
            if (!this.equipmentRepository.existsById(itemRequest.getEquipmentId())) throw new EquipmentNotFoundException(itemRequest.getEquipmentId());

            int availableUnits = this.equipmentUnitRepository.countAvailableUnits(itemRequest.getEquipmentId());
            if (availableUnits < itemRequest.getQuantity()) {
                throw new NotEnoughEquipmentUnitsException(itemRequest.getEquipmentId(), itemRequest.getQuantity(), availableUnits);
            }
        }

        RentalOrderEntity order = new RentalOrderEntity();
        order.setClient(client);
        order.setStatus(RentalOrderStatusEnum.QUOTE);

        for (RentalOrderItemRequestDTO itemRequest : request.getItems()) {
            order.addItem(this.buildItem(itemRequest));
        }

        RentalOrderEntity saved = this.rentalOrderRepository.save(order);
        return RentalOrderMapper.toResponseDTO(saved);
    }

    private RentalOrderItemEntity buildItem(RentalOrderItemRequestDTO itemRequest) {
        if (!itemRequest.getEndDateTime().isAfter(itemRequest.getStartDateTime())) {
            throw new InvalidRentalPeriodException("endDateTime deve ser posterior a startDateTime");
        }

        EquipmentEntity equipment = this.equipmentRepository.findById(itemRequest.getEquipmentId())
            .orElseThrow(() -> new EquipmentNotFoundException(itemRequest.getEquipmentId()));

        RentalOrderItemEntity item = new RentalOrderItemEntity();
        item.setEquipment(equipment);
        item.setQuantity(itemRequest.getQuantity());
        item.setDailyPriceSnapshot(equipment.getDailyPrice());
        item.setHalfDayPriceSnapshot(equipment.getHalfDayPrice());
        item.applyRentalPeriod(itemRequest.getStartDateTime(), itemRequest.getEndDateTime());
        item.calculateSubtotal();

        return item;
    }


    // confirma uma ordem que estava em cotação
    @Override
    @Transactional
    public RentalOrderResponseDTO confirm(UUID id) {
        RentalOrderEntity order = this.findEntityById(id);

        if (order.getStatus() != RentalOrderStatusEnum.QUOTE) {
            throw new InvalidOrderStatusTransitionException(
                "Só é possível confirmar uma ordem em cotação (status atual: %s)".formatted(order.getStatus())
            );
        }

        for (RentalOrderItemEntity item : order.getItems()) {
            this.availabilityService.ensureAvailability(
                item.getEquipment().getId(),
                item.getStartDateTime(),
                item.getEndDateTime(),
                item.getQuantity()
            );
        }

        order.setStatus(RentalOrderStatusEnum.ACTIVE);
        return RentalOrderMapper.toResponseDTO(
            this.rentalOrderRepository.save(order)
        );
    }

    // finaliza uma ordem ativa
    @Override
    @Transactional
    public RentalOrderResponseDTO finish(UUID id) {
        RentalOrderEntity order = this.findEntityById(id);

        if (order.getStatus() != RentalOrderStatusEnum.ACTIVE) {
            throw new InvalidOrderStatusTransitionException(
                "Só é possível finalizar uma ordem ativa (status atual: %s)".formatted(order.getStatus())
            );
        }

        order.setStatus(RentalOrderStatusEnum.FINISHED);
        return RentalOrderMapper.toResponseDTO(
            this.rentalOrderRepository.save(order)
        );
    }

    // cancela uma ordem
    @Override
    @Transactional
    public RentalOrderResponseDTO cancel(UUID id) {
        RentalOrderEntity order = this.findEntityById(id);

        if (order.getStatus() == RentalOrderStatusEnum.FINISHED
            || order.getStatus() == RentalOrderStatusEnum.CANCELLED) {
            throw  new InvalidOrderStatusTransitionException(
                "Não é possível cancelar uma ordem com status %s".formatted(order.getStatus())
            );
        }

        order.setStatus(RentalOrderStatusEnum.CANCELLED);
        return RentalOrderMapper.toResponseDTO(
            this.rentalOrderRepository.save(order)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RentalOrderResponseDTO findById(UUID id) {
        return RentalOrderMapper.toResponseDTO(this.findEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalOrderResponseDTO> findAll() {
        return this.rentalOrderRepository.findAll()
            .stream()
            .map(RentalOrderMapper::toResponseDTO)
            .toList();
    }

    private RentalOrderEntity findEntityById(UUID id) {
        return this.rentalOrderRepository.findById(id)
            .orElseThrow(() -> new RentalOrderNotFoundException(id));
    }
}
