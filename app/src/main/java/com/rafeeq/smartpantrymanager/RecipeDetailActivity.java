package com.rafeeq.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

import java.text.DecimalFormat;
import java.util.List;

public class RecipeDetailActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_recipe_detail
        );

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (view, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        MaterialToolbar toolbar =
                findViewById(R.id.toolbar);

        TextView textRecipeName =
                findViewById(
                        R.id.textDetailRecipeName
                );

        TextView textDescription =
                findViewById(
                        R.id.textDetailDescription
                );

        TextView textIngredients =
                findViewById(
                        R.id.textDetailIngredients
                );

        TextView textInstructions =
                findViewById(
                        R.id.textDetailInstructions
                );

        toolbar.setNavigationOnClickListener(
                view -> finish()
        );

        String selectedRecipeName =
                getIntent().getStringExtra(
                        "recipe_name"
                );

        Recipe selectedRecipe =
                findRecipe(selectedRecipeName);

        if (selectedRecipe == null) {
            Toast.makeText(
                    this,
                    "Unable to load recipe",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        textRecipeName.setText(
                selectedRecipe.getName()
        );

        textDescription.setText(
                selectedRecipe.getDescription()
        );

        textIngredients.setText(
                createIngredientList(
                        selectedRecipe
                )
        );

        textInstructions.setText(
                selectedRecipe.getInstructions()
        );
    }

    private Recipe findRecipe(
            String recipeName
    ) {
        if (recipeName == null) {
            return null;
        }

        List<Recipe> recipes =
                RecipeRepository.getRecipes();

        for (Recipe recipe : recipes) {
            if (recipe.getName().equals(
                    recipeName
            )) {
                return recipe;
            }
        }

        return null;
    }

    private String createIngredientList(
            Recipe recipe
    ) {
        StringBuilder ingredientList =
                new StringBuilder();

        DecimalFormat quantityFormat =
                new DecimalFormat("0.##");

        for (RecipeIngredient ingredient :
                recipe.getIngredients()) {

            ingredientList
                    .append("\u2022 ")
                    .append(
                            quantityFormat.format(
                                    ingredient
                                            .getRequiredQuantity()
                            )
                    )
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append(" ")
                    .append(
                            ingredient
                                    .getIngredientName()
                    )
                    .append("\n");
        }

        return ingredientList.toString().trim();
    }
}