package com.br.kesyo.carmoloc_api.repositories;

import com.br.kesyo.carmoloc_api.entities.EquipmentUnitEntity;
import com.br.kesyo.carmoloc_api.enums.EquipmentCategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EquipmentUnitRepository extends JpaRepository<EquipmentUnitEntity, UUID> {

    List<EquipmentUnitEntity> findByEquipmentIdAndActiveTrue(UUID equipmentId);

    int countByEquipmentIdAndActiveTrue(UUID equipmentId);

    @Query("""
        SELECT COUNT(u) FROM EquipmentUnitEntity u
            WHERE u.equipment.id = :equipmentId
            AND u.status = "AVAILABLE"
            AND u.active = true
    """)
    int countAvailableUnits(@Param("equipmentId") UUID equipmentId);

    @Query("""
        SELECT u.assetCode FROM EquipmentUnitEntity u
        WHERE u.equipment.category = :category
        AND u.assetCode LIKE CONCAT(:prefix, '-%')
    """)
    List<String> findAssetCodesByCategoryPrefix(
            @Param("category")EquipmentCategoryEnum category,
            @Param("prefix") String prefix
    );
}
