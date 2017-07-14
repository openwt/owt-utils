package com.owt.utils;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * ArrayUtils, utility class for arrays
 *
 * @author DBO Open Web Technology
 *
 */
public final class ArrayUtils
{

    private ArrayUtils()
    {

    }

    public static String[] concat(final String[] a, final String[] b)
    {
        return Stream.concat(Arrays.stream(a), Arrays.stream(b)).toArray(size -> new String[size]);
    }

    public static int[] concat(final int[] a, final int[] b)
    {
        return IntStream.concat(Arrays.stream(a), Arrays.stream(b)).toArray();
    }

}
