package com.br.kesyo.carmoloc_api.controllers;

import com.br.kesyo.carmoloc_api.dtos.client.ClientRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.client.ClientResponseDTO;
import com.br.kesyo.carmoloc_api.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Cria um novo cliente", description = "Valida documento único (CPF/CNPJ) antes de criar")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    @ApiResponse(responseCode = "409", description = "Já existe cliente com esse documento")
    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientRequestDTO request) {
        ClientResponseDTO created = this.clientService.create(request);
        return ResponseEntity.created(URI.create("/api/clients/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.clientService.findById(id));
    }

    @Operation(summary = "Edita um cliente", description = "Valida documento único (CPF/CNPJ) antes de editar")
    @ApiResponse(responseCode = "200", description = "Cliente editado com sucesso")
    @ApiResponse(responseCode = "409", description = "Já existe um cliente cadastrado com esse documento")
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ClientRequestDTO request
    ) {
        return ResponseEntity.ok(this.clientService.update(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.clientService.delete(id);
        return ResponseEntity.ok().build();
    }
}
