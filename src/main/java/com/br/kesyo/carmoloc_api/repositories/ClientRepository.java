package com.br.kesyo.carmoloc_api.repositories;

import com.br.kesyo.carmoloc_api.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<ClientEntity, UUID> {

    Optional<ClientEntity> findByDocument(String document);

    boolean existsByDocument(String document);

    List<ClientEntity> findByActiveTrue();
}
