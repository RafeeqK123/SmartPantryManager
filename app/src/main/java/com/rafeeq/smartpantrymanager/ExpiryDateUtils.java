package com.rafeeq.smartpantrymanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExpiryDateUtils {

    private static final String DATE_FORMAT =
            "dd/MM/yyyy";

    public static boolean isExpired(
            String expiryDate
    ) {
        if (expiryDate == null ||
                expiryDate.trim().isEmpty()) {
            return false;
        }

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(
                        DATE_FORMAT,
                        Locale.getDefault()
                );

        dateFormat.setLenient(false);

        try {
            Date ingredientExpiry =
                    dateFormat.parse(expiryDate);

            Calendar today =
                    Calendar.getInstance();

            today.set(
                    Calendar.HOUR_OF_DAY,
                    0
            );

            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            return ingredientExpiry != null &&
                    ingredientExpiry.before(
                            today.getTime()
                    );

        } catch (ParseException exception) {
            return false;
        }
    }

    private ExpiryDateUtils() {
    }
}