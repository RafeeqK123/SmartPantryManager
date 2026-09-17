package com.rafeeq.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;

public class AddEditIngredientActivity extends AppCompatActivity {

    private TextInputLayout layoutIngredientName;
    private TextInputLayout layoutQuantity;
    private TextInputLayout layoutUnit;

    private TextInputEditText editIngredientName;
    private TextInputEditText editQuantity;
    private TextInputEditText editUnit;
    private TextInputEditText editExpiryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_ingredient);

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

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        MaterialButton saveButton =
                findViewById(R.id.buttonSaveIngredient);

        layoutIngredientName =
                findViewById(R.id.layoutIngredientName);
        layoutQuantity =
                findViewById(R.id.layoutQuantity);
        layoutUnit =
                findViewById(R.id.layoutUnit);

        editIngredientName =
                findViewById(R.id.editIngredientName);
        editQuantity =
                findViewById(R.id.editQuantity);
        editUnit =
                findViewById(R.id.editUnit);
        editExpiryDate =
                findViewById(R.id.editExpiryDate);

        toolbar.setNavigationOnClickListener(view -> finish());

        editExpiryDate.setOnClickListener(
                view -> showDatePicker()
        );

        saveButton.setOnClickListener(view -> validateForm());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (datePicker, selectedYear,
                         selectedMonth, selectedDay) -> {

                            String selectedDate = String.format(
                                    Locale.getDefault(),
                                    "%02d/%02d/%04d",
                                    selectedDay,
                                    selectedMonth + 1,
                                    selectedYear
                            );

                            editExpiryDate.setText(selectedDate);
                        },
                        year,
                        month,
                        day
                );

        datePickerDialog.show();
    }

    private void validateForm() {
        clearErrors();

        String name = getText(editIngredientName);
        String quantityText = getText(editQuantity);
        String unit = getText(editUnit);

        boolean isValid = true;

        if (name.isEmpty()) {
            layoutIngredientName.setError(
                    "Please enter an ingredient name"
            );
            isValid = false;
        }

        if (quantityText.isEmpty()) {
            layoutQuantity.setError(
                    "Please enter a quantity"
            );
            isValid = false;
        } else {
            try {
                double quantity =
                        Double.parseDouble(quantityText);

                if (quantity <= 0) {
                    layoutQuantity.setError(
                            "Quantity must be greater than zero"
                    );
                    isValid = false;
                }
            } catch (NumberFormatException exception) {
                layoutQuantity.setError(
                        "Please enter a valid quantity"
                );
                isValid = false;
            }
        }

        if (unit.isEmpty()) {
            layoutUnit.setError(
                    "Please enter a unit"
            );
            isValid = false;
        }

        if (isValid) {
            Toast.makeText(
                    this,
                    "Ingredient details are valid",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void clearErrors() {
        layoutIngredientName.setError(null);
        layoutQuantity.setError(null);
        layoutUnit.setError(null);
    }

    private String getText(TextInputEditText input) {
        if (input.getText() == null) {
            return "";
        }

        return input.getText().toString().trim();
    }
}