package com.rafeeq.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private TextView textEmptyPantry;

    private IngredientAdapter ingredientAdapter;
    private PantryDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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

        recyclerViewPantry =
                findViewById(R.id.recyclerViewPantry);

        textEmptyPantry =
                findViewById(R.id.textEmptyPantry);

        FloatingActionButton addIngredientButton =
                findViewById(R.id.fabAddIngredient);

        BottomNavigationView bottomNavigation =
                findViewById(R.id.bottomNavigation);

        setupIngredientList();

        addIngredientButton.setOnClickListener(view -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        bottomNavigation.setSelectedItemId(
                R.id.nav_pantry
        );

        bottomNavigation.setOnItemSelectedListener(item -> {
            int selectedItemId = item.getItemId();

            if (selectedItemId == R.id.nav_pantry) {
                return true;
            }

            if (selectedItemId == R.id.nav_recipes) {
                Intent intent = new Intent(
                        MainActivity.this,
                        SuggestedRecipesActivity.class
                );

                startActivity(intent);
                return true;
            }

            if (selectedItemId == R.id.nav_settings) {
                Intent intent = new Intent(
                        MainActivity.this,
                        SettingsActivity.class
                );

                startActivity(intent);
                return true;
            }

            return false;
        });
    }

    private void setupIngredientList() {
        ingredientAdapter =
                new IngredientAdapter(
                        new IngredientAdapter
                                .IngredientActionListener() {

                            @Override
                            public void onEditIngredient(
                                    Ingredient ingredient
                            ) {
                                Intent intent = new Intent(
                                        MainActivity.this,
                                        AddEditIngredientActivity.class
                                );

                                intent.putExtra(
                                        "ingredient_id",
                                        ingredient.getId()
                                );

                                intent.putExtra(
                                        "ingredient_name",
                                        ingredient.getName()
                                );

                                intent.putExtra(
                                        "ingredient_quantity",
                                        ingredient.getQuantity()
                                );

                                intent.putExtra(
                                        "ingredient_unit",
                                        ingredient.getUnit()
                                );

                                intent.putExtra(
                                        "ingredient_expiry",
                                        ingredient.getExpiryDate()
                                );

                                startActivity(intent);
                            }

                            @Override
                            public void onDeleteIngredient(
                                    Ingredient ingredient
                            ) {
                                showDeleteConfirmation(
                                        ingredient
                                );
                            }
                        }
                );

        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewPantry.setAdapter(
                ingredientAdapter
        );
    }

    private void loadIngredients() {
        List<Ingredient> ingredients =
                databaseHelper.getAllIngredients();

        ingredientAdapter.setIngredients(
                ingredients
        );

        if (ingredients.isEmpty()) {
            recyclerViewPantry.setVisibility(
                    View.GONE
            );

            textEmptyPantry.setVisibility(
                    View.VISIBLE
            );
        } else {
            recyclerViewPantry.setVisibility(
                    View.VISIBLE
            );

            textEmptyPantry.setVisibility(
                    View.GONE
            );
        }
    }

    private void showDeleteConfirmation(
            Ingredient ingredient
    ) {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage(
                        "Are you sure you want to delete " +
                                ingredient.getName() +
                                "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {
                            databaseHelper.deleteIngredient(
                                    ingredient.getId()
                            );

                            loadIngredients();
                        }
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadIngredients();
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}