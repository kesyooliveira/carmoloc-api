package com.br.kesyo.carmoloc_api.exceptions;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String username) {
        super("Usuário não encontrado: " + username);
    }
}
