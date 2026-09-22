package com.br.kesyo.carmoloc_api.dtos.user;

import com.br.kesyo.carmoloc_api.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDTO {

    @NotBlank(message = "fullName is required")
    @Size(max = 100)
    private String fullName;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    private String email;

    @NotNull(message = "role is required")
    private RoleEnum role;
}
