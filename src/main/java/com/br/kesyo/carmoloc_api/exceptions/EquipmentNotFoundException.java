package com.br.kesyo.carmoloc_api.exceptions;

import java.util.UUID;

public class EquipmentNotFoundException extends RuntimeException {
    public EquipmentNotFoundException(UUID id) {
        super("Equipamento não encontrado: " + id);
    }
}
