package com.br.kesyo.carmoloc_api.mapper;

import com.br.kesyo.carmoloc_api.dtos.user.UserRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.user.UserResponseDTO;
import com.br.kesyo.carmoloc_api.entities.UserEntity;

public class UserMapper {

    private UserMapper() {};

    public static UserEntity toEntity(UserRequestDTO dto) {
        UserEntity entity = new UserEntity();
        applyToEntity(dto, entity);
        return entity;
    }

    private static void applyToEntity(UserRequestDTO dto, UserEntity entity) {
        entity.setUsername(dto.getUsername());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
    }

    public static UserResponseDTO toResponseDTO(UserEntity entity) {
        return UserResponseDTO.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .fullName(entity.getFullName())
            .email(entity.getEmail())
            .role(entity.getRole())
            .active(entity.isActive())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
