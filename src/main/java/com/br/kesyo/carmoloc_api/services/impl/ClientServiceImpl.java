package com.br.kesyo.carmoloc_api.services.impl;

import com.br.kesyo.carmoloc_api.dtos.client.ClientRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.client.ClientResponseDTO;
import com.br.kesyo.carmoloc_api.entities.ClientEntity;
import com.br.kesyo.carmoloc_api.exceptions.ClientNotFoundException;
import com.br.kesyo.carmoloc_api.exceptions.DuplicateDocumentException;
import com.br.kesyo.carmoloc_api.mapper.ClientMapper;
import com.br.kesyo.carmoloc_api.repositories.ClientRepository;
import com.br.kesyo.carmoloc_api.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public ClientResponseDTO create(ClientRequestDTO request) {
        if (clientRepository.existsByDocument(request.getDocument())) {
            throw new DuplicateDocumentException(request.getDocument());
        }

        ClientEntity entity = ClientMapper.toEntity(request);
        ClientEntity saved = clientRepository.save(entity);
        return ClientMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public ClientResponseDTO update(UUID id, ClientRequestDTO request) {
        ClientEntity entity = this.findEntityById(id);

        boolean documentChanged = !entity.getDocument().equals(request.getDocument());
        if (documentChanged && clientRepository.existsByDocument(request.getDocument())) {
            throw new DuplicateDocumentException(request.getDocument());
        }

        ClientMapper.applyToEntity(request, entity);
        ClientEntity saved = clientRepository.save(entity);
        return ClientMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO findById(UUID id) {
        return ClientMapper.toResponseDTO(this.findEntityById(id));
    }

    // lista todos os clientes ativos
    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findAllActive() {
        return clientRepository.findByActiveTrue()
                .stream()
                .map(ClientMapper::toResponseDTO)
                .toList();
    }

    // soft-delete
    @Override
    @Transactional
    public void delete(UUID id) {
        ClientEntity entity = this.findEntityById(id);
        entity.setActive(false);
        clientRepository.save(entity);
    }

    private ClientEntity findEntityById(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

}
