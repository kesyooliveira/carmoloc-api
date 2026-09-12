package com.br.kesyo.carmoloc_api.controllers;

import com.br.kesyo.carmoloc_api.dtos.rentalorder.RentalOrderRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.rentalorder.RentalOrderResponseDTO;
import com.br.kesyo.carmoloc_api.services.RentalOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rental-orders")
@RequiredArgsConstructor
public class RentalOrderController {

    private final RentalOrderService rentalOrderService;

    @PostMapping
    public ResponseEntity<RentalOrderResponseDTO> create(@Valid @RequestBody RentalOrderRequestDTO request) {
        RentalOrderResponseDTO created = this.rentalOrderService.create(request);
        return ResponseEntity.created(URI.create("/api/rental-orders/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalOrderResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.rentalOrderService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<RentalOrderResponseDTO>> findAll() {
        return ResponseEntity.ok(this.rentalOrderService.findAll());
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<RentalOrderResponseDTO> confirm(@PathVariable UUID id) {
        return ResponseEntity.ok(this.rentalOrderService.confirm(id));
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<RentalOrderResponseDTO> finish(@PathVariable UUID id) {
        return ResponseEntity.ok(this.rentalOrderService.finish(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<RentalOrderResponseDTO> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(this.rentalOrderService.cancel(id));
    }
}
