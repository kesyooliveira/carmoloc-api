package com.br.kesyo.carmoloc_api.services;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AvailabilityService {

    int getAvailableQuantity(UUID equipmentId, LocalDateTime start, LocalDateTime end);
    void ensureAvailability(UUID equipmentId, LocalDateTime start, LocalDateTime end, int requestedQuantity);
}
