package com.br.kesyo.carmoloc_api.dtos.user;

import com.br.kesyo.carmoloc_api.enums.RoleEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class UserResponseDTO {

    private UUID id;
    private String username;
    private String fullName;
    private String email;
    private RoleEnum role;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
