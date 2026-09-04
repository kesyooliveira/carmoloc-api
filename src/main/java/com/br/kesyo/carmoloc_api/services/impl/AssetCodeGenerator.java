package com.br.kesyo.carmoloc_api.services.impl;

import com.br.kesyo.carmoloc_api.enums.EquipmentCategoryEnum;
import com.br.kesyo.carmoloc_api.repositories.EquipmentUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class AssetCodeGenerator {

    private final EquipmentUnitRepository equipmentUnitRepository;

    public List<String> generate(EquipmentCategoryEnum category, int quantity) {
        String prefix = category.getAssetCodePrefix();
        int nextNumber = findNextNumber(prefix, category);

        return IntStream.range(0, quantity)
            .mapToObj(i -> "%s-%03d".formatted(prefix, nextNumber + i))
            .toList();
    }

    private int findNextNumber(String prefix, EquipmentCategoryEnum category) {

        List<String> existingCodes = this.equipmentUnitRepository.findAssetCodesByCategoryPrefix(category, prefix);

        return existingCodes.stream()
                .map(code -> code.substring(prefix.length() + 1))
                .filter(suffix -> suffix.chars().allMatch(Character::isDigit))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;
    }
}
