package com.br.kesyo.carmoloc_api.entities;

import com.br.kesyo.carmoloc_api.enums.EquipmentCategoryEnum;
import com.br.kesyo.carmoloc_api.enums.PricingTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class RentalOrderItemEntityTest {

    private EquipmentEntity dailyOnlyEquipment;
    private EquipmentEntity dailyAndHalfEquipment;

    @BeforeEach
    public void setUp() {
        dailyOnlyEquipment = new EquipmentEntity();
        dailyOnlyEquipment.setCategory(EquipmentCategoryEnum.BETONEIRA);
        dailyOnlyEquipment.setPricingType(PricingTypeEnum.DAILY_ONLY);
        dailyOnlyEquipment.setDailyPrice(new BigDecimal("80.00"));

        dailyAndHalfEquipment = new EquipmentEntity();
        dailyAndHalfEquipment.setCategory(EquipmentCategoryEnum.BETONEIRA);
        dailyAndHalfEquipment.setPricingType(PricingTypeEnum.DAY_AND_HALF);
        dailyAndHalfEquipment.setDailyPrice(new BigDecimal("60.00"));
        dailyAndHalfEquipment.setHalfDayPrice(new BigDecimal("35.00"));
    }

    @Nested
    @DisplayName("applyRentalPeriod - DAILY_ONLY")
    class DailyOnlyPeriod {

        @Test
        @DisplayName("deve calcular 7 diárias para um período de 7 dias corridos")
        public void shouldCalculateSevenWholeDays() {
            RentalOrderItemEntity item = buildItem(dailyOnlyEquipment, 1);
        }

        @Test
        @DisplayName("deve aplicar mínimo de 1 diária mesmo em período menor que 1 dia")
        void shouldApplyMinimumOfOneDay() {
            RentalOrderItemEntity item = buildItem(dailyOnlyEquipment, 1);

            item.applyRentalPeriod(
                LocalDateTime.of(2026, 9, 1, 8, 0),
                LocalDateTime.of(2026, 9, 1, 14, 0)
            );

            assertThat(item.getWholeDays()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("applyRentalPeriod - DAY_AND_HALF")
    class DailyAndHalfPeriod {

        @Test
        @DisplayName("4 horas deve virar apenas meia-diária")
        void fourHoursShouldBeHalfDayOnly() {
            RentalOrderItemEntity item = buildItem(dailyAndHalfEquipment, 1);

            item.applyRentalPeriod(
                LocalDateTime.of(2026, 9, 2, 8, 0),
                LocalDateTime.of(2026, 9, 2, 12, 0)
            );

            assertThat(item.getWholeDays()).isEqualTo(0);
            assertThat(item.isHalfDayIncrement()).isTrue();
        }

        @Test
        @DisplayName("28 horas deve virar 1 diária mais meia-diária")
        void twentyEightHoursShouldBeOneDayPlusHalf() {
            RentalOrderItemEntity item = buildItem(dailyAndHalfEquipment, 1);

            item.applyRentalPeriod(
                LocalDateTime.of(2026, 9, 2, 8, 0),
                LocalDateTime.of(2026, 9, 3, 12, 0)
            );

            assertThat(item.getWholeDays()).isEqualTo(1);
            assertThat(item.isHalfDayIncrement()).isTrue();
        }

        @Test
        @DisplayName("mais de 12h de sobra deve virar diária cheia extra")
        void moreThanTwelveHoursRemainderShouldBecomeFullDay() {
            RentalOrderItemEntity item = buildItem(dailyAndHalfEquipment, 1);

            item.applyRentalPeriod(
                LocalDateTime.of(2026, 9, 2, 8, 0),
                LocalDateTime.of(2026, 9, 3, 21, 0) // 37h: 1 dia + 13h de sobra
            );

            assertThat(item.getWholeDays()).isEqualTo(2);
            assertThat(item.isHalfDayIncrement()).isFalse();
        }
    }

    @Nested
    @DisplayName("calculateSubtotal")
    class Subtotal {

        @Test
        @DisplayName("deve multiplicar diária pela quantidade de dias e pela quantidade de itens")
        void shouldCalculateDailyOnlySubtotal() {
            RentalOrderItemEntity item = buildItem(dailyOnlyEquipment, 2);
            item.applyRentalPeriod(
                LocalDateTime.of(2026, 9, 1, 0, 0),
                LocalDateTime.of(2026, 9, 7, 0, 0) // 7 dias
            );

            item.calculateSubtotal();

            // 80.00 * 7 dias * 2 unidades = 1120.00
            assertThat(item.getSubtotal()).isEqualByComparingTo("1120.00");
        }

        @Test
        @DisplayName("deve somar diária cheia + meia-diária quando ambos aplicáveis")
        void shouldCalculateDailyPlusHalfDaySubtotal() {
            RentalOrderItemEntity item = buildItem(dailyAndHalfEquipment, 1);
            item.applyRentalPeriod(
                LocalDateTime.of(2026, 9, 2, 8, 0),
                LocalDateTime.of(2026, 9, 3, 12, 0) // 1 dia + meia-diária
            );

            item.calculateSubtotal();

            // (60.00 * 1) + 35.00 = 95.00
            assertThat(item.getSubtotal()).isEqualByComparingTo("95.00");
        }
    }

    private RentalOrderItemEntity buildItem(EquipmentEntity equipment, int quantity) {
        RentalOrderItemEntity item = new RentalOrderItemEntity();
        item.setEquipment(equipment);
        item.setQuantity(quantity);
        item.setDailyPriceSnapshot(equipment.getDailyPrice());
        item.setHalfDayPriceSnapshot(equipment.getHalfDayPrice());
        return item;
    }
}
