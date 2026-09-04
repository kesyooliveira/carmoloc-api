package com.br.kesyo.carmoloc_api.exceptions;

import java.util.UUID;

public class EquipmentUnitNotFoundException extends RuntimeException {
    public EquipmentUnitNotFoundException(UUID id) {
        super("Unidade de equipamento não encontrada: " + id);
    }
}
