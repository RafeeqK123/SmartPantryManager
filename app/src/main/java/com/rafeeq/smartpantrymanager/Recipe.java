package com.rafeeq.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private long id;
    private String name;
    private String description;
    private String instructions;

    private List<RecipeIngredient> ingredients =
            new ArrayList<>();

    public Recipe() {
    }

    public Recipe(
            String name,
            String description,
            String instructions
    ) {
        this.name = name;
        this.description = description;
        this.instructions = instructions;
    }

    public Recipe(
            long id,
            String name,
            String description,
            String instructions
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.instructions = instructions;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description
    ) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(
            String instructions
    ) {
        this.instructions = instructions;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(
            List<RecipeIngredient> ingredients
    ) {
        this.ingredients = ingredients;
    }

    public void addIngredient(
            RecipeIngredient ingredient
    ) {
        ingredients.add(ingredient);
    }
}