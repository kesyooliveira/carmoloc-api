package com.br.kesyo.carmoloc_api.entities;

import com.br.kesyo.carmoloc_api.common.BaseEntity;
import com.br.kesyo.carmoloc_api.enums.EquipmentUnitStatusEnum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "equipment_unit")
public class EquipmentUnitEntity extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private EquipmentEntity equipment;

    @Column(name = "asset_code", length = 30)
    private String assetCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentUnitStatusEnum status = EquipmentUnitStatusEnum.AVAILABLE;

    @Column(name = "maintenance_note", length = 300)
    private String maintenanceNote;
}
