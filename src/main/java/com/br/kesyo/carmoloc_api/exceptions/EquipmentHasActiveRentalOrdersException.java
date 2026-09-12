package com.br.kesyo.carmoloc_api.exceptions;

import java.util.UUID;

public class EquipmentHasActiveRentalOrdersException extends RuntimeException {
    public EquipmentHasActiveRentalOrdersException(UUID equipmentId) {
        super("Equipamento %s não pode ser removido: possui ordens ativas".formatted(equipmentId));
    }
}
