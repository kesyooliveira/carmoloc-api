package com.br.kesyo.carmoloc_api.mapper;

import com.br.kesyo.carmoloc_api.dtos.client.AddressDTO;
import com.br.kesyo.carmoloc_api.dtos.client.ClientRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.client.ClientResponseDTO;
import com.br.kesyo.carmoloc_api.entities.Address;
import com.br.kesyo.carmoloc_api.entities.ClientEntity;

public class ClientMapper {

    private ClientMapper() {}

    public static ClientEntity toEntity(ClientRequestDTO dto) {
        ClientEntity entity = new ClientEntity();
        applyToEntity(dto, entity);
        return entity;
    }

    public static void applyToEntity(ClientRequestDTO dto, ClientEntity entity) {
        entity.setName(dto.getName());
        entity.setDocumentType(dto.getDocumentType());
        entity.setDocument(dto.getDocument());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setDescription(dto.getDescription());

        if (dto.getAddress() != null) {
            Address address = new Address();
            address.setStreet(dto.getAddress().getStreet());
            address.setNumber(dto.getAddress().getNumber());
            address.setNeighborhood(dto.getAddress().getNeighborhood());
            address.setCity(dto.getAddress().getCity());
            address.setState(dto.getAddress().getState());
            address.setZipCode(dto.getAddress().getZipCode());
            entity.setAddress(address);
        }
    }

    public static ClientResponseDTO toResponseDTO(ClientEntity entity) {
        AddressDTO addressDTO = null;
        if (entity.getAddress() != null) {
            addressDTO = new AddressDTO();
            addressDTO.setStreet(entity.getAddress().getStreet());
            addressDTO.setNumber(entity.getAddress().getNumber());
            addressDTO.setNeighborhood(entity.getAddress().getNeighborhood());
            addressDTO.setCity(entity.getAddress().getCity());
            addressDTO.setState(entity.getAddress().getState());
            addressDTO.setZipCode(entity.getAddress().getZipCode());
        }

        return ClientResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .documentType(entity.getDocumentType())
                .document(entity.getDocument())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .description(entity.getDescription())
                .address(addressDTO)
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
