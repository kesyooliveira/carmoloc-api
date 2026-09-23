package com.br.kesyo.carmoloc_api.services;

import com.br.kesyo.carmoloc_api.dtos.user.UserRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.user.UserResponseDTO;
import com.br.kesyo.carmoloc_api.dtos.user.UserUpdateRequestDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDTO create(UserRequestDTO request);

    UserResponseDTO update(UUID id, UserUpdateRequestDTO request);

    UserResponseDTO findById(UUID id);

    List<UserResponseDTO> findAllActive();

    void delete(UUID id);
}
