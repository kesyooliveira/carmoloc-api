package com.br.kesyo.carmoloc_api.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequestDTO {

    @NotBlank(message = "refreshToken is required")
    private String refreshToken;
}
