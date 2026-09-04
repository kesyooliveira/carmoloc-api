package com.br.kesyo.carmoloc_api.enums;

public enum EquipmentCategoryEnum {
    BETONEIRA("BET"),
    COMPACTADOR("CMP"),
    ESCORAMENTO("ESC"),
    CONTAINER("CNT"),
    ANDAIME("AND");

    private final String assetCodePrefix;

    EquipmentCategoryEnum(String assetCodePrefix) {
        this.assetCodePrefix = assetCodePrefix;
    }

    public String getAssetCodePrefix() {
        return assetCodePrefix;
    }
}
