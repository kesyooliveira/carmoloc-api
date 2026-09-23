package com.br.kesyo.carmoloc_api.exceptions;

public class SelfDeleteNotAllowedException extends RuntimeException {
    public SelfDeleteNotAllowedException() {
        super("Você não pode excluir seu próprio usuário!");
    }
}
