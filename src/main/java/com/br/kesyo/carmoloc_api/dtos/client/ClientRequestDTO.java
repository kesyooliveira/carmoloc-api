package com.br.kesyo.carmoloc_api.dtos.client;

import com.br.kesyo.carmoloc_api.enums.ClientDocumentTypeEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClientRequestDTO {

    @NotBlank(message = "name is required")
    @Size(max = 150)
    private String name;

    @NotNull(message = "documentType is required")
    private ClientDocumentTypeEnum documentType;

    @NotBlank(message = "document is required")
    @Size(min = 11, max = 14, message = "document must be 11 (CPF) or 14 (CNPJ) digits")
    private String document;

    @NotBlank(message = "phone is required")
    private String phone;

    @Email(message = "email must be valid")
    private String email;

    private String description;

    @Valid
    private AddressDTO address;
}
