package com.br.kesyo.carmoloc_api.entities;

import com.br.kesyo.carmoloc_api.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshTokenEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private boolean revoked = false;

    public boolean isExpired() {
        return this.expiryDate.isBefore(Instant.now());
    }

    public boolean isValid() {
        return !this.revoked && !this.isExpired() && this.isActive();
    }
}
