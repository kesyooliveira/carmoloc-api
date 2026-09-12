package com.br.kesyo.carmoloc_api.repositories;

import com.br.kesyo.carmoloc_api.entities.RentalOrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface RentalOrderItemRepository extends JpaRepository<RentalOrderItemEntity, UUID> {

    @Query("""
        SELECT COALESCE(SUM(i.quantity), 0)
        FROM RentalOrderItemEntity i
        WHERE i.equipment.id = :equipmentId
        AND i.rentalOrder.status = "ACTIVE"
        AND i.startDateTime <= :endDateTime
        AND i.endDateTime >= :startDateTime
    """
    )
    int sumReservedQuantity(
        @Param("equipmentId") UUID equipmentId,
        @Param("startDateTime") LocalDateTime startDateTime,
        @Param("endDateTime") LocalDateTime endDateTime
    );

    @Query("""
        SELECT COUNT(i) > 0
        FROM RentalOrderItemEntity i
        WHERE i.equipment.id = :equipmentId
        AND i.rentalOrder.status = "ACTIVE"
    """)
    boolean existsActiveRentalFotEquipment(@Param("equipmentId") UUID equipmentId);
}
