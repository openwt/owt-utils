package com.owt.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * CurrencyUtils, utility class for currencies conversion
 *
 * @author Loïc Bernollin Open Web Technology
 * @since 5 nov. 2015
 *
 */
public final class CurrencyUtils
{

    private static final int SCALE_2 = 2;

    private static final int SCALE_3 = 3;

    public static final String AMOUNT_DISPLAY_PATTERN;

    private static final DecimalFormat FORMATTER;

    public static final String DEFAULT_CURRENCY = "USD";

    static {
        AMOUNT_DISPLAY_PATTERN = "##0.00";
        FORMATTER = new DecimalFormat(AMOUNT_DISPLAY_PATTERN);
        FORMATTER.setRoundingMode(RoundingMode.DOWN);
    }

    private CurrencyUtils()
    {

    }

    public static String convertAmount(final double baseAmount, final double ratePerUsd)
    {
        if (baseAmount > 0.00 && ratePerUsd > 0.00) {
            return FORMATTER.format(baseAmount * ratePerUsd);
        }
        return "";
    }

    public static String convertAmount(final double baseAmount, final double ratePerUsd, final Locale locale)
    {
        if (baseAmount > 0.00 && ratePerUsd > 0.00) {
            return formatAmountByLocal(baseAmount * ratePerUsd, locale);
        }
        return "";
    }

    public static String formatAmountByLocal(final Double amount, final Locale locale)
    {
        final DecimalFormat decimalFormat = (DecimalFormat) getCurrentCurrencyFormat(locale);
        decimalFormat.applyPattern(AMOUNT_DISPLAY_PATTERN);
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        return decimalFormat.format(amount);
    }

    public static String formatAmountByLocalWithPattern(final Double amount, final Locale locale, final String pattern)
    {
        final DecimalFormat decimalFormat = (DecimalFormat) getCurrentCurrencyFormat(locale);
        decimalFormat.applyPattern(pattern);
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        return decimalFormat.format(amount);
    }

    public static String formatAmountByLocal(final Double amount, final String localeStr)
    {
        return formatAmountByLocal(amount, new Locale(localeStr));
    }

    /**
     * getCurrentCurrencyFormat return the current currency format based on the current locale.
     *
     * @param locale
     * @return String
     */
    private static NumberFormat getCurrentCurrencyFormat(final Locale locale)
    {
        return NumberFormat.getCurrencyInstance(locale);
    }

    /**
     * roundAmount implementation of this method can be explained by reading its Unit tests
     * (CurrencyUtilsTest)
     *
     * @param amount
     * @return BigDecimal
     */
    public static double roundAmount(final double amount)
    {
        BigDecimal bd = BigDecimal.valueOf(amount).setScale(SCALE_3, RoundingMode.HALF_UP);
        bd = bd.setScale(SCALE_2, RoundingMode.HALF_DOWN);
        return bd.doubleValue();
    }

    public static String formatAmount(final Double amount, final String format)
    {
        final DecimalFormat formatter = new DecimalFormat(format);
        return formatter.format(amount);
    }

}
