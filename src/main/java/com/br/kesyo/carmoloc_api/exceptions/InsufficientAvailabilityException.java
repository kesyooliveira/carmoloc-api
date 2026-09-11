package com.br.kesyo.carmoloc_api.exceptions;

import java.util.UUID;

public class InsufficientAvailabilityException extends RuntimeException {
    public InsufficientAvailabilityException(UUID equipmentId, int requested, int available) {
        super("Equipamento %s indisponível: solicitado %d, disponível %d"
                .formatted(equipmentId, requested, available));
    }
}
