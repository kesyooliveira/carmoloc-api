package com.br.kesyo.carmoloc_api.repositories;

import com.br.kesyo.carmoloc_api.entities.EquipmentEntity;
import com.br.kesyo.carmoloc_api.enums.EquipmentCategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EquipmentRepository extends JpaRepository<EquipmentEntity, UUID> {

    List<EquipmentEntity> findByActiveTrue();

    List<EquipmentEntity> findByCategoryAndActiveTrue(EquipmentCategoryEnum category);
}
