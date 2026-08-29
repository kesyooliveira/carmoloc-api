package com.br.kesyo.carmoloc_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Address {

    private String street;
    private String number;
    private String neighborhood;
    private String city;

    @Column(length = 2)
    private String state;

    @Column(name = "zip_code", length = 8)
    private String zipCode;

}
