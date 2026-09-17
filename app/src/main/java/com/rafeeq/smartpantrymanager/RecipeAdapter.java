package com.rafeeq.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface RecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private final List<Recipe> recipes =
            new ArrayList<>();

    private final RecipeClickListener clickListener;

    public RecipeAdapter(
            RecipeClickListener clickListener
    ) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_recipe,
                        parent,
                        false
                );

        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position
    ) {
        Recipe recipe = recipes.get(position);

        holder.bind(recipe, clickListener);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void setRecipes(
            List<Recipe> updatedRecipes
    ) {
        recipes.clear();
        recipes.addAll(updatedRecipes);
        notifyDataSetChanged();
    }

    static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        private final TextView textName;
        private final TextView textDescription;
        private final TextView textIngredientCount;
        private final MaterialButton buttonViewRecipe;

        RecipeViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);

            textName = itemView.findViewById(
                    R.id.textRecipeName
            );

            textDescription = itemView.findViewById(
                    R.id.textRecipeDescription
            );

            textIngredientCount = itemView.findViewById(
                    R.id.textRecipeIngredientCount
            );

            buttonViewRecipe = itemView.findViewById(
                    R.id.buttonViewRecipe
            );
        }

        void bind(
                Recipe recipe,
                RecipeClickListener clickListener
        ) {
            textName.setText(recipe.getName());

            textDescription.setText(
                    recipe.getDescription()
            );

            int ingredientCount =
                    recipe.getIngredients().size();

            String countText = String.format(
                    Locale.getDefault(),
                    "%d required ingredient%s",
                    ingredientCount,
                    ingredientCount == 1 ? "" : "s"
            );

            textIngredientCount.setText(countText);

            buttonViewRecipe.setOnClickListener(
                    view -> clickListener
                            .onRecipeClick(recipe)
            );

            itemView.setOnClickListener(
                    view -> clickListener
                            .onRecipeClick(recipe)
            );
        }
    }
}