package com.br.kesyo.carmoloc_api.repositories;

import com.br.kesyo.carmoloc_api.entities.ClientEntity;
import com.br.kesyo.carmoloc_api.entities.EquipmentEntity;
import com.br.kesyo.carmoloc_api.entities.RentalOrderEntity;
import com.br.kesyo.carmoloc_api.entities.RentalOrderItemEntity;
import com.br.kesyo.carmoloc_api.enums.ClientDocumentTypeEnum;
import com.br.kesyo.carmoloc_api.enums.EquipmentCategoryEnum;
import com.br.kesyo.carmoloc_api.enums.PricingTypeEnum;
import com.br.kesyo.carmoloc_api.enums.RentalOrderStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RentalOrderItemRepositoryIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private RentalOrderItemRepository rentalOrderItemRepository;

    @Autowired
    private RentalOrderRepository rentalOrderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    private EquipmentEntity equipment;
    private ClientEntity client;

    @BeforeEach
    void setUp() {
        client = new ClientEntity();
        client.setName("João da Silva");
        client.setDocumentType(ClientDocumentTypeEnum.CPF);
        client.setDocument("12345678900");
        client.setPhone("31999999999");
        client = clientRepository.save(client);

        equipment = new EquipmentEntity();
        equipment.setName("Betoneira 400L");
        equipment.setCategory(EquipmentCategoryEnum.BETONEIRA);
        equipment.setPricingType(PricingTypeEnum.DAILY_ONLY);
        equipment.setDailyPrice(new BigDecimal("80.00"));
        equipment = equipmentRepository.save(equipment);
    }

    @Test
    @DisplayName("deve somar quantidade reservada apenas de ordens ACTIVE que se sobrepõem ao período")
    void shouldSumOnlyActiveOverlappingOrders() {
        saveOrderWithItem(RentalOrderStatusEnum.ACTIVE,
            LocalDateTime.of(2026, 9, 1, 8, 0),
            LocalDateTime.of(2026, 9, 5, 8, 0),
            2);

        saveOrderWithItem(RentalOrderStatusEnum.QUOTE, // não deve contar
            LocalDateTime.of(2026, 9, 2, 8, 0),
            LocalDateTime.of(2026, 9, 3, 8, 0),
            3);

        saveOrderWithItem(RentalOrderStatusEnum.FINISHED, // não deve contar
            LocalDateTime.of(2026, 9, 2, 8, 0),
            LocalDateTime.of(2026, 9, 3, 8, 0),
            1);

        int reserved = rentalOrderItemRepository.sumReservedQuantity(
            equipment.getId(),
            LocalDateTime.of(2026, 9, 2, 0, 0),
            LocalDateTime.of(2026, 9, 3, 0, 0)
        );

        assertThat(reserved).isEqualTo(2); // só a ACTIVE conta
    }

    @Test
    @DisplayName("não deve somar reserva de ordem ACTIVE em período que não se sobrepõe")
    void shouldNotSumNonOverlappingPeriod() {
        saveOrderWithItem(RentalOrderStatusEnum.ACTIVE,
            LocalDateTime.of(2026, 9, 1, 8, 0),
            LocalDateTime.of(2026, 9, 3, 8, 0),
            2
        );

        int reserved = rentalOrderItemRepository.sumReservedQuantity(
            equipment.getId(),
            LocalDateTime.of(2026, 9, 10, 0, 0),
            LocalDateTime.of(2026, 9, 12, 0, 0)
        );

        assertThat(reserved).isZero();
    }

    @Test
    @DisplayName("deve considerar sobreposição parcial nas bordas do período")
    void shouldConsiderPartialOverlapAtBoundaries() {
        saveOrderWithItem(RentalOrderStatusEnum.ACTIVE,
            LocalDateTime.of(2026, 9, 1, 8, 0),
            LocalDateTime.of(2026, 9, 5, 8, 0),
            2
        );

        // pedido de disponibilidade começa 1 dia antes do fim da reserva existente
        int reserved = rentalOrderItemRepository.sumReservedQuantity(
            equipment.getId(),
            LocalDateTime.of(2026, 9, 4, 0, 0),
            LocalDateTime.of(2026, 9, 10, 0, 0)
        );

        assertThat(reserved).isEqualTo(2); // ainda se sobrepõe em 1 dia
    }

    private void saveOrderWithItem(RentalOrderStatusEnum status, LocalDateTime start, LocalDateTime end, int quantity) {
        RentalOrderEntity order = new RentalOrderEntity();
        order.setClient(client);
        order.setStatus(status);

        RentalOrderItemEntity item = new RentalOrderItemEntity();
        item.setEquipment(equipment);
        item.setQuantity(quantity);
        item.setDailyPriceSnapshot(equipment.getDailyPrice());
        item.applyRentalPeriod(start, end);
        item.calculateSubtotal();

        order.addItem(item);
        rentalOrderRepository.saveAndFlush(order); // salva o pedido, cascade salva o item
    }
}
