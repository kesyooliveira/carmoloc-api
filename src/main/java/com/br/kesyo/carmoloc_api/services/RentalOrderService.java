package com.br.kesyo.carmoloc_api.services;

import com.br.kesyo.carmoloc_api.common.PageResponseDTO;
import com.br.kesyo.carmoloc_api.dtos.rentalorder.RentalOrderRequestDTO;
import com.br.kesyo.carmoloc_api.dtos.rentalorder.RentalOrderResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface RentalOrderService {

    RentalOrderResponseDTO create(RentalOrderRequestDTO request);

    RentalOrderResponseDTO confirm(UUID id);

    RentalOrderResponseDTO finish(UUID id);

    RentalOrderResponseDTO cancel(UUID id);

    RentalOrderResponseDTO findById(UUID id);

    List<RentalOrderResponseDTO> findAllActive();

    PageResponseDTO<RentalOrderResponseDTO> findAllActivePaged(Pageable pageable);
}
