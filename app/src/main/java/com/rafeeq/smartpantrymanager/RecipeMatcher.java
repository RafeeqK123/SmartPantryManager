package com.rafeeq.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeMatcher {

    public static List<Recipe> findMatchingRecipes(
            List<Recipe> recipes,
            List<Ingredient> pantryIngredients
    ) {
        List<Recipe> matchingRecipes =
                new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (canMakeRecipe(
                    recipe,
                    pantryIngredients
            )) {
                matchingRecipes.add(recipe);
            }
        }

        return matchingRecipes;
    }

    public static boolean canMakeRecipe(
            Recipe recipe,
            List<Ingredient> pantryIngredients
    ) {
        for (RecipeIngredient required :
                recipe.getIngredients()) {

            double availableQuantity =
                    findAvailableQuantity(
                            required,
                            pantryIngredients
                    );

            double requiredQuantity =
                    convertToBaseQuantity(
                            required.getRequiredQuantity(),
                            required.getUnit()
                    );

            if (availableQuantity < requiredQuantity) {
                return false;
            }
        }

        return true;
    }

    private static double findAvailableQuantity(
            RecipeIngredient required,
            List<Ingredient> pantryIngredients
    ) {
        double totalAvailable = 0;

        String requiredName =
                normaliseIngredientName(
                        required.getIngredientName()
                );

        String requiredUnitType =
                getUnitType(required.getUnit());

        for (Ingredient pantryItem :
                pantryIngredients) {

            String pantryName =
                    normaliseIngredientName(
                            pantryItem.getName()
                    );

            String pantryUnitType =
                    getUnitType(
                            pantryItem.getUnit()
                    );

            boolean namesMatch =
                    requiredName.equals(pantryName);

            boolean unitsMatch =
                    requiredUnitType.equals(
                            pantryUnitType
                    );

            if (namesMatch && unitsMatch) {
                totalAvailable +=
                        convertToBaseQuantity(
                                pantryItem.getQuantity(),
                                pantryItem.getUnit()
                        );
            }
        }

        return totalAvailable;
    }

    private static String normaliseIngredientName(
            String ingredientName
    ) {
        if (ingredientName == null) {
            return "";
        }

        String normalised = ingredientName
                .trim()
                .toLowerCase(Locale.ROOT);

        switch (normalised) {
            case "eggs":
                return "egg";

            case "tomatoes":
                return "tomato";

            case "potatoes":
                return "potato";

            case "beans":
                return "bean";

            case "bananas":
                return "banana";

            case "apples":
                return "apple";

            case "oranges":
                return "orange";

            case "onions":
                return "onion";

            case "oat":
                return "oats";

            default:
                return normalised;
        }
    }

    private static String getUnitType(
            String unit
    ) {
        String normalisedUnit =
                normaliseUnit(unit);

        switch (normalisedUnit) {
            case "g":
            case "kg":
                return "mass";

            case "ml":
            case "l":
                return "volume";

            case "item":
                return "item";

            case "slice":
                return "slice";

            default:
                return "unknown:" +
                        normalisedUnit;
        }
    }

    private static double convertToBaseQuantity(
            double quantity,
            String unit
    ) {
        String normalisedUnit =
                normaliseUnit(unit);

        switch (normalisedUnit) {
            case "kg":
                return quantity * 1000;

            case "l":
                return quantity * 1000;

            default:
                return quantity;
        }
    }

    private static String normaliseUnit(
            String unit
    ) {
        if (unit == null) {
            return "";
        }

        String normalised = unit
                .trim()
                .toLowerCase(Locale.ROOT);

        switch (normalised) {
            case "grams":
            case "gram":
                return "g";

            case "kilograms":
            case "kilogram":
            case "kgs":
                return "kg";

            case "millilitres":
            case "millilitre":
            case "milliliters":
            case "milliliter":
                return "ml";

            case "litres":
            case "litre":
            case "liters":
            case "liter":
                return "l";

            case "items":
            case "pieces":
            case "piece":
                return "item";

            case "slices":
                return "slice";

            default:
                return normalised;
        }
    }
}