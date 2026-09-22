package com.br.kesyo.carmoloc_api.exceptions;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String username) {
        super("Já existe um usuário com o username: " + username);
    }
}
