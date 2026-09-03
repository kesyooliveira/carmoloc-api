package com.br.kesyo.carmoloc_api.exceptions;

public class DuplicateDocumentException extends RuntimeException {
    public DuplicateDocumentException(String document) {
        super("Já existe um cliente cadastrado com o documento: " + document);
    }
}
