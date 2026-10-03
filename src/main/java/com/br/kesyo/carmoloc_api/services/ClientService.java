package com.br.kesyo.carmoloc_api.services;

import com.br.kesyo.carmoloc_api.common.PageResponseDTO;
import com.br.kesyo.carmoloc_api.dtos.client.ClientRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.client.ClientResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ClientService {

    ClientResponseDTO create(ClientRequestDTO request);

    ClientResponseDTO update(UUID id, ClientRequestDTO request);

    ClientResponseDTO findById(UUID id);

    List<ClientResponseDTO> findAllActive();

    PageResponseDTO<ClientResponseDTO> findAllActivePaged(Pageable pageable);

    void delete(UUID id);
}
