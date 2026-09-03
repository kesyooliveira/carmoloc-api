package com.br.kesyo.carmoloc_api.dtos.client;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {

    private String street;
    private String number;
    private String neighborhood;
    private String city;

    @Pattern(regexp = "[A-Z]{2}", message = "state must be a 2-letter uppercase UF code. Example: MG")
    private String state;

    @Pattern(regexp = "\\d{8}", message = "zipCode must contain exactly 8 digits. Example: 12345678")
    private String zipCode;
}
