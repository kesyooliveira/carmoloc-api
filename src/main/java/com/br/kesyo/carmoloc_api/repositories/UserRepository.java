package com.br.kesyo.carmoloc_api.repositories;

import com.br.kesyo.carmoloc_api.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsernameAndActiveTrue(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
