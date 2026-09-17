package com.br.kesyo.carmoloc_api.controllers;

import com.br.kesyo.carmoloc_api.dtos.equipment.AddEquipmentUnitsRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.equipment.EquipmentRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.equipment.EquipmentResponseDTO;
import com.br.kesyo.carmoloc_api.dtos.equipment.EquipmentUnitResponseDTO;
import com.br.kesyo.carmoloc_api.enums.EquipmentUnitStatusEnum;
import com.br.kesyo.carmoloc_api.services.EquipmentService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<EquipmentResponseDTO> create(@Valid @RequestBody EquipmentRequestDTO request) {
        EquipmentResponseDTO created = this.equipmentService.create(request);
        return ResponseEntity.created(URI.create("/api/equipment" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.equipmentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponseDTO>> findAll() {
        return ResponseEntity.ok(this.equipmentService.findAllActive());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponseDTO> update(
        @PathVariable UUID id,
        @Valid @RequestBody EquipmentRequestDTO request
    ) {
        return ResponseEntity.ok(this.equipmentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.equipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/units")
    public ResponseEntity<List<EquipmentUnitResponseDTO>> findUnits(@PathVariable UUID id) {
        return ResponseEntity.ok(equipmentService.findUnitsByEquipmentId(id));
    }

    @PatchMapping("/units/{unitId}/status")
    public ResponseEntity<EquipmentUnitResponseDTO> updateUnitStatus(
        @PathVariable UUID unitId,
        @Valid @RequestBody UpdateUnitStatusRequestDTO request
    ) {
        return ResponseEntity.ok(
            this.equipmentService.updateUnitStatus(unitId, request.getStatus(), request.getMaintenanceNote())
        );
    }

    @PostMapping("/{id}/units")
    public ResponseEntity<List<EquipmentUnitResponseDTO>> addUnits(
        @PathVariable UUID id,
        @Valid @RequestBody AddEquipmentUnitsRequestDTO request
    ) {
        List<EquipmentUnitResponseDTO> created = this.equipmentService.addUnits(
            id, request.getQuantity(), request.getAssetCodes()
        );

        return ResponseEntity
            .created(URI.create("/api/equipment" + id + "/units"))
            .body(created);
    }

    @Getter
    @Setter
    public static class UpdateUnitStatusRequestDTO {
        private EquipmentUnitStatusEnum status;
        private String maintenanceNote;
    }
}
