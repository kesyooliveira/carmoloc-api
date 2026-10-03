package com.br.kesyo.carmoloc_api.repositories;

import com.br.kesyo.carmoloc_api.entities.RentalOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RentalOrderRepository extends JpaRepository<RentalOrderEntity, UUID> {
    List<RentalOrderEntity> findByClientId(UUID clientId);

    List<RentalOrderEntity> findByActiveTrue();

    Page<RentalOrderEntity> findByActiveTrue(Pageable pageable);
}
