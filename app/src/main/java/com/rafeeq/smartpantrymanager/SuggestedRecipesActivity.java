package com.rafeeq.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Locale;

public class SuggestedRecipesActivity
        extends AppCompatActivity {

    private RecyclerView recyclerViewRecipes;
    private TextView textEmptyRecipes;
    private TextView textRecipeSummary;

    private RecipeAdapter recipeAdapter;
    private PantryDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_suggested_recipes
        );

        databaseHelper =
                new PantryDatabaseHelper(this);

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

        recyclerViewRecipes =
                findViewById(
                        R.id.recyclerViewRecipes
                );

        textEmptyRecipes =
                findViewById(
                        R.id.textEmptyRecipes
                );

        textRecipeSummary =
                findViewById(
                        R.id.textRecipeSummary
                );

        BottomNavigationView bottomNavigation =
                findViewById(
                        R.id.bottomNavigation
                );

        setupRecipeList();
        setupBottomNavigation(bottomNavigation);
    }

    private void setupRecipeList() {
        recipeAdapter = new RecipeAdapter(
                recipe -> {
                    Intent intent = new Intent(
                            SuggestedRecipesActivity.this,
                            RecipeDetailActivity.class
                    );

                    intent.putExtra(
                            "recipe_name",
                            recipe.getName()
                    );

                    startActivity(intent);
                }
        );

        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewRecipes.setAdapter(
                recipeAdapter
        );
    }

    private void setupBottomNavigation(
            BottomNavigationView bottomNavigation
    ) {
        bottomNavigation.setSelectedItemId(
                R.id.nav_recipes
        );

        bottomNavigation.setOnItemSelectedListener(
                item -> {
                    int selectedItemId =
                            item.getItemId();

                    if (selectedItemId ==
                            R.id.nav_recipes) {
                        return true;
                    }

                    if (selectedItemId ==
                            R.id.nav_pantry) {
                        finish();
                        return true;
                    }

                    if (selectedItemId ==
                            R.id.nav_settings) {
                        Intent intent = new Intent(
                                SuggestedRecipesActivity.this,
                                SettingsActivity.class
                        );

                        startActivity(intent);
                        return true;
                    }

                    return false;
                }
        );
    }

    private void loadMatchingRecipes() {
        List<Ingredient> pantryIngredients =
                databaseHelper.getAllIngredients();

        List<Recipe> allRecipes =
                RecipeRepository.getRecipes();

        List<Recipe> matchingRecipes =
                RecipeMatcher.findMatchingRecipes(
                        allRecipes,
                        pantryIngredients
                );

        recipeAdapter.setRecipes(
                matchingRecipes
        );

        String summaryText = String.format(
                Locale.getDefault(),
                "%d recipe%s match your pantry",
                matchingRecipes.size(),
                matchingRecipes.size() == 1 ? "" : "s"
        );

        textRecipeSummary.setText(summaryText);

        if (matchingRecipes.isEmpty()) {
            recyclerViewRecipes.setVisibility(
                    View.GONE
            );

            textEmptyRecipes.setVisibility(
                    View.VISIBLE
            );
        } else {
            recyclerViewRecipes.setVisibility(
                    View.VISIBLE
            );

            textEmptyRecipes.setVisibility(
                    View.GONE
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMatchingRecipes();
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}