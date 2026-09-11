package com.br.kesyo.carmoloc_api.exceptions;

import java.util.UUID;

public class RentalOrderNotFoundException extends RuntimeException {
    public RentalOrderNotFoundException(UUID id) {
        super("Ordem de locação não encontrada: " + id);
    }
}
