package com.br.kesyo.carmoloc_api.exceptions;

import java.util.UUID;

public class NotEnoughEquipmentUnitsException extends RuntimeException {
    public NotEnoughEquipmentUnitsException(UUID equipmentId, Integer requestQuantity, int availableUnits) {
        super("Quantidade indisponível para o equipamento %s: solicitado %d, disponível %d"
            .formatted(equipmentId, requestQuantity, availableUnits)
        );
    }
}
