package com.br.kesyo.carmoloc_api.services.impl;

import com.br.kesyo.carmoloc_api.entities.RefreshTokenEntity;
import com.br.kesyo.carmoloc_api.entities.UserEntity;
import com.br.kesyo.carmoloc_api.exceptions.InvalidRefreshTokenException;
import com.br.kesyo.carmoloc_api.repositories.RefreshTokenRepository;
import com.br.kesyo.carmoloc_api.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    @Override
    @Transactional
    public RefreshTokenEntity createForUser(UserEntity user) {
        this.refreshTokenRepository.deleteByUserId(user.getId());

        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(this.refreshExpirationMs));

        return this.refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshTokenEntity validateAndGet(String token) {
        RefreshTokenEntity refreshToken = this.refreshTokenRepository.findByToken(token)
            .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token inválido"));

        if(!refreshToken.isValid()) {
            throw new InvalidRefreshTokenException("Refresh token expirado ou revogado");
        }

        return refreshToken;
    }

    @Override
    @Transactional
    public void revokeAllForUser(UUID userId) {
        this.refreshTokenRepository.deleteByUserId(userId);
    }
}
