package com.br.kesyo.carmoloc_api.services;

import com.br.kesyo.carmoloc_api.entities.RefreshTokenEntity;
import com.br.kesyo.carmoloc_api.entities.UserEntity;

import java.util.UUID;

public interface RefreshTokenService {

    RefreshTokenEntity createForUser(UserEntity user);
    RefreshTokenEntity validateAndGet(String token);
    void revokeAllForUser(UUID userId);
}
