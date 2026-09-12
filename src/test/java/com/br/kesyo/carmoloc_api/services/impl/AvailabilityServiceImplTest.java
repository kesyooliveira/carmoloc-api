package com.br.kesyo.carmoloc_api.services.impl;

import com.br.kesyo.carmoloc_api.exceptions.InsufficientAvailabilityException;
import com.br.kesyo.carmoloc_api.repositories.EquipmentUnitRepository;
import com.br.kesyo.carmoloc_api.repositories.RentalOrderItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailabilityServiceImplTest {

    @Mock
    private EquipmentUnitRepository equipmentUnitRepository;

    @Mock
    private RentalOrderItemRepository rentalOrderItemRepository;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

    private final UUID equipmentId = UUID.randomUUID();
    private final LocalDateTime start = LocalDateTime.of(2026, 9, 1, 8, 0);
    private final LocalDateTime end = LocalDateTime.of(2026, 9, 5, 8, 0);

    @Test
    @DisplayName("disponibilidade deve ser unidades operacionais menos reservadas")
    void shouldCalculateAvailableAsOperationalMinusReserved() {
        when(equipmentUnitRepository.countAvailableUnits(equipmentId)).thenReturn(6);
        when(rentalOrderItemRepository.sumReservedQuantity(equipmentId, start, end)).thenReturn(2);

        int available = availabilityService.getAvailableQuantity(equipmentId, start, end);

        assertThat(available).isEqualTo(4);
    }

    @Test
    @DisplayName("ensureAvailability não deve lançar exceção quando há unidades suficientes")
    void shouldNotThrowWhenAvailabilityIsSufficient() {
        when(equipmentUnitRepository.countAvailableUnits(equipmentId)).thenReturn(6);
        when(rentalOrderItemRepository.sumReservedQuantity(equipmentId, start, end)).thenReturn(2);

        availabilityService.ensureAvailability(equipmentId, start, end, 4);

        // se chegou até aqui sem lançar exceção, o teste passou —
        // não precisa de assert explícito nesse caso
    }

    @Test
    @DisplayName("ensureAvailability deve lançar exceção quando quantidade solicitada excede disponível")
    void shouldThrowWhenRequestedQuantityExceedsAvailable() {
        when(equipmentUnitRepository.countAvailableUnits(equipmentId)).thenReturn(6);
        when(rentalOrderItemRepository.sumReservedQuantity(equipmentId, start, end)).thenReturn(5);

        assertThatThrownBy(() ->
                availabilityService.ensureAvailability(equipmentId, start, end, 3)
        ).isInstanceOf(InsufficientAvailabilityException.class)
                .hasMessageContaining("indisponível");
    }

    @Test
    @DisplayName("disponibilidade zero quando todas as unidades estão em manutenção")
    void shouldReturnZeroWhenAllUnitsUnderMaintenance() {
        when(equipmentUnitRepository.countAvailableUnits(equipmentId)).thenReturn(0);
        when(rentalOrderItemRepository.sumReservedQuantity(equipmentId, start, end)).thenReturn(0);

        int available = availabilityService.getAvailableQuantity(equipmentId, start, end);

        assertThat(available).isZero();
    }
}
