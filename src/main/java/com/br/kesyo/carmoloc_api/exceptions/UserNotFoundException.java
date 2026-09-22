package com.br.kesyo.carmoloc_api.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("Usuário não encontrado: " + id);
    }
}
