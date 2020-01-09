package com.owt.utils;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

/**
 * ArrayUtils, utility class for arrays
 *
 * @author DBO Open Web Technology
 */
public final class ArrayUtils {

    private ArrayUtils() {

    }

    public static String[] concat(final String[] a, final String[] b) {
        return Stream.concat(Stream.of(a), Stream.of(b)).toArray(String[]::new);
    }

    public static int[] concat(final int[] a, final int[] b) {
        return IntStream.concat(IntStream.of(a), IntStream.of(b)).toArray();
    }

    public static <T> T last(final T[] array) {
        return isNotEmpty(array) ? array[array.length - 1] : null;
    }

}
