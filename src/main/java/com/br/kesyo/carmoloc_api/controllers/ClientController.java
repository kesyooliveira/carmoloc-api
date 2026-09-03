package com.br.kesyo.carmoloc_api.controllers;

import com.br.kesyo.carmoloc_api.dtos.client.ClientRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.client.ClientResponseDTO;
import com.br.kesyo.carmoloc_api.services.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientRequestDTO request) {
        ClientResponseDTO created = this.clientService.create(request);
        return ResponseEntity.created(URI.create("/api/clients/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.clientService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ClientRequestDTO request
    ) {
        return ResponseEntity.ok(this.clientService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.clientService.delete(id);
        return ResponseEntity.ok().build();
    }
}
