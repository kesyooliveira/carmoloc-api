package com.br.kesyo.carmoloc_api.exceptions;

import java.util.UUID;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(UUID id) {
        super("Cliente não encontrato: " + id);
    }
}
