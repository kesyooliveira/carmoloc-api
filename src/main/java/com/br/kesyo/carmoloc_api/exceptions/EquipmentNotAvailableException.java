package com.br.kesyo.carmoloc_api.exceptions;

import com.br.kesyo.carmoloc_api.enums.EquipmentStatusEnum;

import java.util.UUID;

public class EquipmentNotAvailableException extends RuntimeException {
    public EquipmentNotAvailableException(UUID equipmentId, EquipmentStatusEnum status) {
        super("Equipamento %s não está disponível para locação (status: %s)".formatted(equipmentId, status));
    }
}
