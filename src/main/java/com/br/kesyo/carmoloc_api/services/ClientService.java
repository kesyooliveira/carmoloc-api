package com.br.kesyo.carmoloc_api.services;

import com.br.kesyo.carmoloc_api.dtos.client.ClientRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.client.ClientResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ClientService {

    ClientResponseDTO create(ClientRequestDTO request);

    ClientResponseDTO update(UUID id, ClientRequestDTO request);

    ClientResponseDTO findById(UUID id);

    List<ClientResponseDTO> findAllActive();

    void delete(UUID id);
}
