package com.br.kesyo.carmoloc_api.dtos.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDTO {

    private String token;
    private String refreshToken;
    private String username;
    private String role;
}
