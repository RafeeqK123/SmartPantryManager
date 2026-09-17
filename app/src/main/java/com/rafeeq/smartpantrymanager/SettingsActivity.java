package com.rafeeq.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class SettingsActivity
        extends AppCompatActivity {

    private TextView textPantryCount;
    private MaterialButton buttonClearPantry;

    private PantryDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(
                R.layout.activity_settings
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

        textPantryCount =
                findViewById(R.id.textPantryCount);

        buttonClearPantry =
                findViewById(R.id.buttonClearPantry);

        BottomNavigationView bottomNavigation =
                findViewById(R.id.bottomNavigation);

        buttonClearPantry.setOnClickListener(
                view -> showClearPantryConfirmation()
        );

        setupBottomNavigation(bottomNavigation);
    }

    private void setupBottomNavigation(
            BottomNavigationView bottomNavigation
    ) {
        bottomNavigation.setSelectedItemId(
                R.id.nav_settings
        );

        bottomNavigation.setOnItemSelectedListener(
                item -> {
                    int selectedItemId =
                            item.getItemId();

                    if (selectedItemId ==
                            R.id.nav_settings) {
                        return true;
                    }

                    if (selectedItemId ==
                            R.id.nav_pantry) {
                        Intent intent = new Intent(
                                SettingsActivity.this,
                                MainActivity.class
                        );

                        intent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                        );

                        startActivity(intent);
                        finish();
                        return true;
                    }

                    if (selectedItemId ==
                            R.id.nav_recipes) {
                        Intent intent = new Intent(
                                SettingsActivity.this,
                                SuggestedRecipesActivity.class
                        );

                        startActivity(intent);
                        finish();
                        return true;
                    }

                    return false;
                }
        );
    }

    private void updatePantryCount() {
        int ingredientCount =
                databaseHelper
                        .getAllIngredients()
                        .size();

        String countText = String.format(
                Locale.getDefault(),
                "%d ingredient%s stored",
                ingredientCount,
                ingredientCount == 1 ? "" : "s"
        );

        textPantryCount.setText(countText);

        buttonClearPantry.setEnabled(
                ingredientCount > 0
        );
    }

    private void showClearPantryConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Clear pantry")
                .setMessage(
                        "Are you sure you want to permanently " +
                                "delete all pantry ingredients?"
                )
                .setPositiveButton(
                        "Clear pantry",
                        (dialog, which) ->
                                clearPantry()
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }

    private void clearPantry() {
        databaseHelper.deleteAllIngredients();

        updatePantryCount();

        Toast.makeText(
                this,
                "Pantry cleared successfully",
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePantryCount();
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}