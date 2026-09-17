package com.rafeeq.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    public interface IngredientActionListener {
        void onEditIngredient(Ingredient ingredient);

        void onDeleteIngredient(Ingredient ingredient);
    }

    private final List<Ingredient> ingredients =
            new ArrayList<>();

    private final IngredientActionListener actionListener;

    public IngredientAdapter(
            IngredientActionListener actionListener
    ) {
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_ingredient,
                        parent,
                        false
                );

        return new IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IngredientViewHolder holder,
            int position
    ) {
        Ingredient ingredient =
                ingredients.get(position);

        holder.bind(
                ingredient,
                actionListener
        );
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void setIngredients(
            List<Ingredient> updatedIngredients
    ) {
        ingredients.clear();
        ingredients.addAll(updatedIngredients);
        notifyDataSetChanged();
    }

    static class IngredientViewHolder
            extends RecyclerView.ViewHolder {

        private final TextView textName;
        private final TextView textQuantity;
        private final TextView textExpiry;

        private final MaterialButton buttonEdit;
        private final MaterialButton buttonDelete;

        IngredientViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            textName = itemView.findViewById(
                    R.id.textIngredientName
            );

            textQuantity = itemView.findViewById(
                    R.id.textIngredientQuantity
            );

            textExpiry = itemView.findViewById(
                    R.id.textIngredientExpiry
            );

            buttonEdit = itemView.findViewById(
                    R.id.buttonEditIngredient
            );

            buttonDelete = itemView.findViewById(
                    R.id.buttonDeleteIngredient
            );
        }

        void bind(
                Ingredient ingredient,
                IngredientActionListener actionListener
        ) {
            textName.setText(ingredient.getName());

            DecimalFormat quantityFormat =
                    new DecimalFormat("0.##");

            String quantityText =
                    "Quantity: " +
                            quantityFormat.format(
                                    ingredient.getQuantity()
                            ) +
                            " " +
                            ingredient.getUnit();

            textQuantity.setText(quantityText);

            String expiryDate =
                    ingredient.getExpiryDate();

            if (expiryDate == null ||
                    expiryDate.isEmpty()) {
                textExpiry.setText(
                        "Expiry date: Not provided"
                );
            } else {
                textExpiry.setText(
                        "Expiry date: " + expiryDate
                );
            }

            buttonEdit.setOnClickListener(
                    view -> actionListener
                            .onEditIngredient(ingredient)
            );

            buttonDelete.setOnClickListener(
                    view -> actionListener
                            .onDeleteIngredient(ingredient)
            );
        }
    }
}