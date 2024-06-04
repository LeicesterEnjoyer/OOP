package oop.dataformat;

import java.text.DecimalFormat;
import java.util.Currency;

/**
 * Utility class for formatting numbers.
 */
public class NumberFormatter {
    /** 
     * The default currency. 
     */
    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("BGN");
    /**
     * The default accuracy for floating-point numbers.
     */
    private static final int DEFAULT_ACCURACY = 2;

    /**
     * Formats the given amount of money with the specified currency.
     *
     * @param amount        The amount of money to be formatted.
     * @param currency      The currency name.
     * @return              The formatted money as a string.
     */
    public static String formatMoney(float amount, String currency) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        if (currency == null || currency.isEmpty())
            return decimalFormat.format(amount) + " " + DEFAULT_CURRENCY.getCurrencyCode();
        return decimalFormat.format(amount) + " " + currency;
    }

    /**
     * Formats the given amount of money with the default currency.
     *
     * @param amount        The amount of money to be formatted.
     * @return              The formatted money with the default currency as a string.
     */
    public static String formatMoney(float amount) {
        return formatMoney(amount, DEFAULT_CURRENCY.getCurrencyCode());
    }

    /**
     * Formats the given floating-point value with the specified accuracy.
     *
     * @param value         The floating-point value to be formatted.
     * @param accuracy      The number of decimal places to round to.
     * @return              The formatted floating-point value as a string.
     */
    public static String formatFloat(float value, int accuracy) {
        String pattern = "#,##0." + "0".repeat(accuracy);
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(value);
    }

    /**
     * Formats the given floating-point value with the default accuracy.
     *
     * @param value         The floating-point value to be formatted.
     * @return              The formatted floating-point value with the default accuracy as a string.
     */
    public static String formatFloat(float value) {
        return formatFloat(value, DEFAULT_ACCURACY);
    }
}