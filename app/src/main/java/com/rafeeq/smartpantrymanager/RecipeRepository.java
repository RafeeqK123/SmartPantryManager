package com.rafeeq.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;

public class RecipeRepository {

    public static List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        recipes.add(createRecipe(
                "Scrambled Eggs",
                "Soft scrambled eggs for breakfast.",
                "Beat the eggs. Melt the butter in a pan. " +
                        "Add the eggs and salt, then stir until cooked.",
                new RecipeIngredient("Eggs", 2, "items"),
                new RecipeIngredient("Butter", 10, "g"),
                new RecipeIngredient("Salt", 1, "g")
        ));

        recipes.add(createRecipe(
                "Cheese Omelette",
                "A quick omelette filled with cheese.",
                "Beat the eggs. Melt the butter in a pan. " +
                        "Cook the eggs, add cheese, fold and serve.",
                new RecipeIngredient("Eggs", 2, "items"),
                new RecipeIngredient("Cheese", 50, "g"),
                new RecipeIngredient("Butter", 10, "g")
        ));

        recipes.add(createRecipe(
                "Tomato Pasta",
                "Simple pasta cooked in a tomato sauce.",
                "Cook the pasta. Heat oil and tomatoes in a pan. " +
                        "Add salt, combine with the pasta and serve.",
                new RecipeIngredient("Pasta", 200, "g"),
                new RecipeIngredient("Tomatoes", 200, "g"),
                new RecipeIngredient("Oil", 15, "ml"),
                new RecipeIngredient("Salt", 2, "g")
        ));

        recipes.add(createRecipe(
                "Rice and Beans",
                "A filling rice and bean meal.",
                "Cook the rice until soft. Heat the beans, " +
                        "season with salt and serve over the rice.",
                new RecipeIngredient("Rice", 200, "g"),
                new RecipeIngredient("Beans", 150, "g"),
                new RecipeIngredient("Salt", 2, "g")
        ));

        recipes.add(createRecipe(
                "Chicken and Rice",
                "Pan-cooked chicken served with rice.",
                "Cook the rice. Fry the chicken in oil until cooked. " +
                        "Season with salt and serve with the rice.",
                new RecipeIngredient("Chicken", 200, "g"),
                new RecipeIngredient("Rice", 150, "g"),
                new RecipeIngredient("Oil", 15, "ml"),
                new RecipeIngredient("Salt", 2, "g")
        ));

        recipes.add(createRecipe(
                "Tuna Sandwich",
                "A quick tuna and mayonnaise sandwich.",
                "Mix the tuna and mayonnaise. Spread the mixture " +
                        "between the bread slices and serve.",
                new RecipeIngredient("Bread", 2, "slices"),
                new RecipeIngredient("Tuna", 100, "g"),
                new RecipeIngredient("Mayonnaise", 20, "g")
        ));

        recipes.add(createRecipe(
                "Grilled Cheese Sandwich",
                "A toasted sandwich with melted cheese.",
                "Butter the bread, add the cheese and grill in a pan " +
                        "until both sides are golden.",
                new RecipeIngredient("Bread", 2, "slices"),
                new RecipeIngredient("Cheese", 60, "g"),
                new RecipeIngredient("Butter", 10, "g")
        ));

        recipes.add(createRecipe(
                "Banana Oats",
                "Warm oats topped with banana.",
                "Cook the oats in milk until thick. Slice the banana, " +
                        "add it to the oats and serve.",
                new RecipeIngredient("Oats", 50, "g"),
                new RecipeIngredient("Banana", 1, "items"),
                new RecipeIngredient("Milk", 200, "ml")
        ));

        recipes.add(createRecipe(
                "Basic Pancakes",
                "Simple homemade breakfast pancakes.",
                "Mix flour, milk, egg and sugar. Pour small amounts " +
                        "into a hot pan and cook on both sides.",
                new RecipeIngredient("Flour", 100, "g"),
                new RecipeIngredient("Milk", 200, "ml"),
                new RecipeIngredient("Eggs", 1, "items"),
                new RecipeIngredient("Sugar", 10, "g")
        ));

        recipes.add(createRecipe(
                "Mashed Potatoes",
                "Creamy mashed potatoes.",
                "Boil the potatoes until soft. Drain, then mash with " +
                        "butter, milk and salt.",
                new RecipeIngredient("Potatoes", 400, "g"),
                new RecipeIngredient("Butter", 20, "g"),
                new RecipeIngredient("Milk", 50, "ml"),
                new RecipeIngredient("Salt", 2, "g")
        ));

        recipes.add(createRecipe(
                "Fruit Salad",
                "A fresh mixture of sliced fruit.",
                "Peel and chop the fruit. Mix everything together " +
                        "in a bowl and serve.",
                new RecipeIngredient("Apple", 1, "items"),
                new RecipeIngredient("Banana", 1, "items"),
                new RecipeIngredient("Orange", 1, "items")
        ));

        recipes.add(createRecipe(
                "Tomato and Cheese Sandwich",
                "A fresh tomato and cheese sandwich.",
                "Slice the tomato and place it on the bread with " +
                        "the cheese. Close the sandwich and serve.",
                new RecipeIngredient("Bread", 2, "slices"),
                new RecipeIngredient("Tomatoes", 100, "g"),
                new RecipeIngredient("Cheese", 30, "g")
        ));

        recipes.add(createRecipe(
                "Egg Fried Rice",
                "Rice stir-fried with egg.",
                "Heat the oil, scramble the eggs, add the cooked rice " +
                        "and stir-fry until hot.",
                new RecipeIngredient("Rice", 200, "g"),
                new RecipeIngredient("Eggs", 2, "items"),
                new RecipeIngredient("Oil", 15, "ml")
        ));

        recipes.add(createRecipe(
                "Chicken Tomato Pasta",
                "Pasta with chicken and tomato sauce.",
                "Cook the pasta. Fry the chicken, add the tomatoes " +
                        "and combine everything together.",
                new RecipeIngredient("Chicken", 200, "g"),
                new RecipeIngredient("Pasta", 200, "g"),
                new RecipeIngredient("Tomatoes", 150, "g"),
                new RecipeIngredient("Oil", 15, "ml")
        ));

        recipes.add(createRecipe(
                "Bean Salad",
                "A simple bean, tomato and onion salad.",
                "Chop the tomato and onion. Mix them with the beans " +
                        "and oil, then serve.",
                new RecipeIngredient("Beans", 200, "g"),
                new RecipeIngredient("Tomatoes", 100, "g"),
                new RecipeIngredient("Onion", 50, "g"),
                new RecipeIngredient("Oil", 15, "ml")
        ));

        return recipes;
    }

    private static Recipe createRecipe(
            String name,
            String description,
            String instructions,
            RecipeIngredient... ingredients
    ) {
        Recipe recipe = new Recipe(
                name,
                description,
                instructions
        );

        for (RecipeIngredient ingredient : ingredients) {
            recipe.addIngredient(ingredient);
        }

        return recipe;
    }
}