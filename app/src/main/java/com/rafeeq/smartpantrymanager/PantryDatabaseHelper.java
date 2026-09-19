package com.rafeeq.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PantryDatabaseHelper
        extends SQLiteOpenHelper {

    private static final String DATABASE_NAME =
            "smart_pantry.db";

    // Version 2 adds recipes and recipe ingredients.
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_INGREDIENTS =
            "ingredients";

    private static final String TABLE_RECIPES =
            "recipes";

    private static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_QUANTITY =
            "quantity";

    public static final String COLUMN_UNIT = "unit";

    public static final String COLUMN_EXPIRY_DATE =
            "expiry_date";

    private static final String COLUMN_RECIPE_NAME =
            "recipe_name";

    private static final String COLUMN_DESCRIPTION =
            "description";

    private static final String COLUMN_INSTRUCTIONS =
            "instructions";

    private static final String COLUMN_RECIPE_ID =
            "recipe_id";

    private static final String COLUMN_INGREDIENT_NAME =
            "ingredient_name";

    private static final String COLUMN_REQUIRED_QUANTITY =
            "required_quantity";

    public PantryDatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    @Override
    public void onConfigure(
            SQLiteDatabase database
    ) {
        super.onConfigure(database);

        // This will enforce the relationship between recipes
        // and their required ingredients.
        database.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(
            SQLiteDatabase database
    ) {
        createIngredientsTable(database);
        createRecipeTables(database);
        seedRecipes(database);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase database,
            int oldVersion,
            int newVersion
    ) {
        if (oldVersion < 2) {
            // This will keep the user's pantry ingredients and add
            // the new recipe related tables.
            createRecipeTables(database);
            seedRecipes(database);
        }
    }

    private void createIngredientsTable(
            SQLiteDatabase database
    ) {
        String createIngredientsTable =
                "CREATE TABLE " +
                        TABLE_INGREDIENTS +
                        " (" +
                        COLUMN_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME +
                        " TEXT NOT NULL, " +
                        COLUMN_QUANTITY +
                        " REAL NOT NULL, " +
                        COLUMN_UNIT +
                        " TEXT NOT NULL, " +
                        COLUMN_EXPIRY_DATE +
                        " TEXT" +
                        ")";

        database.execSQL(
                createIngredientsTable
        );
    }

    private void createRecipeTables(
            SQLiteDatabase database
    ) {
        String createRecipesTable =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_RECIPES +
                        " (" +
                        COLUMN_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_NAME +
                        " TEXT NOT NULL UNIQUE, " +
                        COLUMN_DESCRIPTION +
                        " TEXT NOT NULL, " +
                        COLUMN_INSTRUCTIONS +
                        " TEXT NOT NULL" +
                        ")";

        database.execSQL(
                createRecipesTable
        );

        String createRecipeIngredientsTable =
                "CREATE TABLE IF NOT EXISTS " +
                        TABLE_RECIPE_INGREDIENTS +
                        " (" +
                        COLUMN_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_ID +
                        " INTEGER NOT NULL, " +
                        COLUMN_INGREDIENT_NAME +
                        " TEXT NOT NULL, " +
                        COLUMN_REQUIRED_QUANTITY +
                        " REAL NOT NULL, " +
                        COLUMN_UNIT +
                        " TEXT NOT NULL, " +
                        "FOREIGN KEY (" +
                        COLUMN_RECIPE_ID +
                        ") REFERENCES " +
                        TABLE_RECIPES +
                        "(" +
                        COLUMN_ID +
                        ") ON DELETE CASCADE" +
                        ")";

        database.execSQL(
                createRecipeIngredientsTable
        );
    }

    // This will insert the built-in recipes only when the recipe
    // table is empty, to prevent duplicate records.
    private void seedRecipes(
            SQLiteDatabase database
    ) {
        if (getRecipeCount(database) > 0) {
            return;
        }

        List<Recipe> recipes =
                RecipeRepository.getRecipes();

        for (Recipe recipe : recipes) {
            ContentValues recipeValues =
                    new ContentValues();

            recipeValues.put(
                    COLUMN_RECIPE_NAME,
                    recipe.getName()
            );

            recipeValues.put(
                    COLUMN_DESCRIPTION,
                    recipe.getDescription()
            );

            recipeValues.put(
                    COLUMN_INSTRUCTIONS,
                    recipe.getInstructions()
            );

            long recipeId = database.insertOrThrow(
                    TABLE_RECIPES,
                    null,
                    recipeValues
            );

            for (RecipeIngredient ingredient :
                    recipe.getIngredients()) {

                ContentValues ingredientValues =
                        new ContentValues();

                ingredientValues.put(
                        COLUMN_RECIPE_ID,
                        recipeId
                );

                ingredientValues.put(
                        COLUMN_INGREDIENT_NAME,
                        ingredient.getIngredientName()
                );

                ingredientValues.put(
                        COLUMN_REQUIRED_QUANTITY,
                        ingredient.getRequiredQuantity()
                );

                ingredientValues.put(
                        COLUMN_UNIT,
                        ingredient.getUnit()
                );

                database.insertOrThrow(
                        TABLE_RECIPE_INGREDIENTS,
                        null,
                        ingredientValues
                );
            }
        }
    }

    private int getRecipeCount(
            SQLiteDatabase database
    ) {
        Cursor cursor = database.rawQuery(
                "SELECT COUNT(*) FROM " +
                        TABLE_RECIPES,
                null
        );

        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }

        return 0;
    }

    public long addIngredient(
            Ingredient ingredient
    ) {
        SQLiteDatabase database =
                getWritableDatabase();

        ContentValues values =
                createIngredientValues(
                        ingredient
                );

        return database.insert(
                TABLE_INGREDIENTS,
                null,
                values
        );
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients =
                new ArrayList<>();

        SQLiteDatabase database =
                getReadableDatabase();

        Cursor cursor = database.query(
                TABLE_INGREDIENTS,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME +
                        " COLLATE NOCASE ASC"
        );

        try {
            while (cursor.moveToNext()) {
                ingredients.add(
                        createIngredientFromCursor(
                                cursor
                        )
                );
            }
        } finally {
            cursor.close();
        }

        return ingredients;
    }

    // This will read every seeded recipe and attach its required
    // ingredients from the related database table.
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes =
                new ArrayList<>();

        SQLiteDatabase database =
                getReadableDatabase();

        Cursor cursor = database.query(
                TABLE_RECIPES,
                null,
                null,
                null,
                null,
                null,
                COLUMN_RECIPE_NAME +
                        " COLLATE NOCASE ASC"
        );

        try {
            while (cursor.moveToNext()) {
                long recipeId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_ID
                        )
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_RECIPE_NAME
                        )
                );

                String description = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_DESCRIPTION
                        )
                );

                String instructions = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_INSTRUCTIONS
                        )
                );

                Recipe recipe = new Recipe(
                        recipeId,
                        name,
                        description,
                        instructions
                );

                recipe.setIngredients(
                        getRecipeIngredients(
                                database,
                                recipeId
                        )
                );

                recipes.add(recipe);
            }
        } finally {
            cursor.close();
        }

        return recipes;
    }

    private List<RecipeIngredient>
    getRecipeIngredients(
            SQLiteDatabase database,
            long recipeId
    ) {
        List<RecipeIngredient> ingredients =
                new ArrayList<>();

        Cursor cursor = database.query(
                TABLE_RECIPE_INGREDIENTS,
                null,
                COLUMN_RECIPE_ID + " = ?",
                new String[]{
                        String.valueOf(recipeId)
                },
                null,
                null,
                COLUMN_ID + " ASC"
        );

        try {
            while (cursor.moveToNext()) {
                long ingredientId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_ID
                        )
                );

                String ingredientName =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_INGREDIENT_NAME
                                )
                        );

                double requiredQuantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_REQUIRED_QUANTITY
                                )
                        );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                COLUMN_UNIT
                        )
                );

                ingredients.add(
                        new RecipeIngredient(
                                ingredientId,
                                recipeId,
                                ingredientName,
                                requiredQuantity,
                                unit
                        )
                );
            }
        } finally {
            cursor.close();
        }

        return ingredients;
    }

    public int updateIngredient(
            Ingredient ingredient
    ) {
        SQLiteDatabase database =
                getWritableDatabase();

        ContentValues values =
                createIngredientValues(
                        ingredient
                );

        return database.update(
                TABLE_INGREDIENTS,
                values,
                COLUMN_ID + " = ?",
                new String[]{
                        String.valueOf(
                                ingredient.getId()
                        )
                }
        );
    }

    public int deleteIngredient(
            long ingredientId
    ) {
        SQLiteDatabase database =
                getWritableDatabase();

        return database.delete(
                TABLE_INGREDIENTS,
                COLUMN_ID + " = ?",
                new String[]{
                        String.valueOf(
                                ingredientId
                        )
                }
        );
    }

    public int deleteAllIngredients() {
        SQLiteDatabase database =
                getWritableDatabase();

        return database.delete(
                TABLE_INGREDIENTS,
                null,
                null
        );
    }

    private ContentValues createIngredientValues(
            Ingredient ingredient
    ) {
        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                ingredient.getName()
        );

        values.put(
                COLUMN_QUANTITY,
                ingredient.getQuantity()
        );

        values.put(
                COLUMN_UNIT,
                ingredient.getUnit()
        );

        values.put(
                COLUMN_EXPIRY_DATE,
                ingredient.getExpiryDate()
        );

        return values;
    }

    private Ingredient createIngredientFromCursor(
            Cursor cursor
    ) {
        long id = cursor.getLong(
                cursor.getColumnIndexOrThrow(
                        COLUMN_ID
                )
        );

        String name = cursor.getString(
                cursor.getColumnIndexOrThrow(
                        COLUMN_NAME
                )
        );

        double quantity = cursor.getDouble(
                cursor.getColumnIndexOrThrow(
                        COLUMN_QUANTITY
                )
        );

        String unit = cursor.getString(
                cursor.getColumnIndexOrThrow(
                        COLUMN_UNIT
                )
        );

        String expiryDate = cursor.getString(
                cursor.getColumnIndexOrThrow(
                        COLUMN_EXPIRY_DATE
                )
        );

        return new Ingredient(
                id,
                name,
                quantity,
                unit,
                expiryDate
        );
    }
}