package com.rafeeq.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PantryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME =
            "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_INGREDIENTS =
            "ingredients";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY_DATE =
            "expiry_date";

    public PantryDatabaseHelper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String createIngredientsTable =
                "CREATE TABLE " + TABLE_INGREDIENTS + " (" +
                        COLUMN_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_QUANTITY + " REAL NOT NULL, " +
                        COLUMN_UNIT + " TEXT NOT NULL, " +
                        COLUMN_EXPIRY_DATE + " TEXT" +
                        ")";

        database.execSQL(createIngredientsTable);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase database,
            int oldVersion,
            int newVersion
    ) {
        database.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_INGREDIENTS
        );

        onCreate(database);
    }

    public long addIngredient(Ingredient ingredient) {
        SQLiteDatabase database =
                getWritableDatabase();

        ContentValues values =
                createIngredientValues(ingredient);

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
                COLUMN_NAME + " COLLATE NOCASE ASC"
        );

        try {
            while (cursor.moveToNext()) {
                ingredients.add(
                        createIngredientFromCursor(cursor)
                );
            }
        } finally {
            cursor.close();
        }

        return ingredients;
    }

    public int updateIngredient(Ingredient ingredient) {
        SQLiteDatabase database =
                getWritableDatabase();

        ContentValues values =
                createIngredientValues(ingredient);

        return database.update(
                TABLE_INGREDIENTS,
                values,
                COLUMN_ID + " = ?",
                new String[]{
                        String.valueOf(ingredient.getId())
                }
        );
    }

    public int deleteIngredient(long ingredientId) {
        SQLiteDatabase database =
                getWritableDatabase();

        return database.delete(
                TABLE_INGREDIENTS,
                COLUMN_ID + " = ?",
                new String[]{
                        String.valueOf(ingredientId)
                }
        );
    }

    private ContentValues createIngredientValues(
            Ingredient ingredient
    ) {
        ContentValues values = new ContentValues();

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
                cursor.getColumnIndexOrThrow(COLUMN_ID)
        );

        String name = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_NAME)
        );

        double quantity = cursor.getDouble(
                cursor.getColumnIndexOrThrow(
                        COLUMN_QUANTITY
                )
        );

        String unit = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_UNIT)
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