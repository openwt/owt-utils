package com.owt.utils;

import java.util.stream.Stream;

/**
 * NumberUtils, utility class for numbers
 *
 * @author Vsevolod Kokurin Open Web Technology
 * @since 2/27/17
 *
 */
public final class NumberUtils
{
    private NumberUtils()
    {

    }

    /**
     * this method check if all of the parameters are positive
     *
     * @param values
     * @return boolean
     */
    public static boolean isAllPositive(final Number... values)
    {
        return Stream.of(values).allMatch(number -> Math.signum(number.doubleValue()) > 0);
    }

    /**
     * this method check if one of the parameters is negative or zero
     *
     * @param values
     * @return boolean
     */
    public static boolean isAnyNegative(final Number... values)
    {
        return Stream.of(values).anyMatch(NumberUtils::isNegative);
    }

    /**
     * This method accepts any of the wrapper classes (Integer, Long, Float and Double) and thanks
     * to auto-boxing any of the primitive numeric types (int, long, float and double) and simply
     * checks it the high bit, which in all types is the sign bit, is set.
     *
     * It returns true when passed any of:
     *
     * any negative int/Integer
     * any negative long/Long
     * any negative float/Float
     * any negative double/Double
     * Double.NEGATIVE_INFINITY
     * Float.NEGATIVE_INFINITY
     *
     * and false otherwise.
     *
     * @param number
     * @return boolean
     */
    public static boolean isNegative(final Number number)
    {
        return (Double.doubleToLongBits(number.doubleValue()) & Long.MIN_VALUE) == Long.MIN_VALUE;
    }
}
