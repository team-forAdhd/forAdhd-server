package com.project.foradhd.domain.medicine.persistence.enums;

public enum MedicineIngredient {
    METHYLPHENIDATE("메틸페니데이트"),
    ATOMOXETINE("아토목세틴"),
    CLONIDINE("클로니딘");

    private final String ingredient;

    MedicineIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public static boolean isRecognizedIngredient(String ingredient) {
        for (MedicineIngredient mi : values()) {
            if (mi.ingredient.equals(ingredient)) {
                return true;
            }
        }
        return false;
    }
}
