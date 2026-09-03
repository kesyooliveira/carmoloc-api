package com.br.kesyo.carmoloc_api.dtos.client;

import com.br.kesyo.carmoloc_api.enums.ClientDocumentTypeEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class ClientResponseDTO {

    private UUID id;
    private String name;
    private ClientDocumentTypeEnum documentType;
    private String document;
    private String phone;
    private String email;
    private String description;
    private AddressDTO address;
    private boolean active;
    private Instant createdAt;
}
