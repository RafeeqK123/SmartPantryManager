package com.rafeeq.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets systemBars =
                    insets.getInsets(WindowInsetsCompat.Type.systemBars());

            view.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );

            return insets;
        });

        FloatingActionButton addIngredientButton =
                findViewById(R.id.fabAddIngredient);

        BottomNavigationView bottomNavigation =
                findViewById(R.id.bottomNavigation);

        // Open the Add/Edit Ingredient screen.
        addIngredientButton.setOnClickListener(view -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
            );

            startActivity(intent);
        });

        // Keep Pantry selected because this is the Pantry List screen.
        bottomNavigation.setSelectedItemId(R.id.nav_pantry);

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
}