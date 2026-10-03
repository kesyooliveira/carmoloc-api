package com.br.kesyo.carmoloc_api.repositories;

import com.br.kesyo.carmoloc_api.entities.EquipmentEntity;
import com.br.kesyo.carmoloc_api.enums.EquipmentCategoryEnum;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EquipmentRepository extends JpaRepository<EquipmentEntity, UUID> {

    List<EquipmentEntity> findByActiveTrue();

    List<EquipmentEntity> findByCategoryAndActiveTrue(EquipmentCategoryEnum category);

    boolean existsById(@NonNull UUID id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM EquipmentEntity e WHERE e.id = :id")
    Optional<EquipmentEntity> findByIdForUpdate(@Param("id") UUID id);

    Page<EquipmentEntity> findByActiveTrue(Pageable pageable);
}
