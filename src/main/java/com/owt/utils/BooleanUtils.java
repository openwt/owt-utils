package com.owt.utils;

/**
 * BooleanUtils, utility class for boolean
 *
 * @author LBE Open Web Technology
 * 
 */
public final class BooleanUtils
{

    private BooleanUtils()
    {

    }

    /**
     * <p>
     * Checks if a {@code Boolean} value is {@code true}, handling {@code null} by returning
     * {@code false}.
     * </p>
     *
     */
    public static boolean isTrue(final Boolean bool)
    {
        return bool != null && Boolean.TRUE.equals(bool);
    }

    /**
     * <p>
     * Checks if a {@code Boolean} value is {@code false}, handling {@code null} by returning
     * {@code true}.
     * </p>
     */
    public static boolean isFalse(final Boolean bool)
    {
        return bool == null || Boolean.FALSE.equals(bool);
    }

    /**
     * <p>
     * Converts a String to a boolean (optimised for performance).
     * </p>
     *
     * @param str
     * @return
     */
    public static Boolean toBoolean(final String str)
    {
        return Boolean.parseBoolean(str);
    }

    /**
     * <p>
     * Converts an int to a boolean using the convention that {@code zero} is {@code false}.
     * </p>
     */
    public static boolean toBoolean(final int value)
    {
        return value != 0;
    }

}
