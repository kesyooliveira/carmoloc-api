package com.br.kesyo.carmoloc_api.exceptions;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Já existe um usuário com o email: " + email);
    }
}
