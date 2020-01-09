package com.owt.test.utils;

import com.owt.utils.NumberUtils;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Open Web Technology.
 *
 * @author Vsevolod Kokurin Open Web Technology
 * @since 2/27/17
 */
public class NumberUtilsTest {

    @Test
    public void testisAllPositive_OK() {
        final long a = 1;
        final long b = 2;

        assertTrue(NumberUtils.isAllPositive(a, b));
    }

    @Test
    public void testisAllPositive_NOK() {
        assertFalse(NumberUtils.isAllPositive(-1, 2));
        assertFalse(NumberUtils.isAllPositive(0, 2));
    }

    @Test
    public void testisAnyNegative_OK() {
        final long a = -1;
        final long b = 2;

        assertTrue(NumberUtils.isAnyNegative(a, b));
    }

    @Test
    public void testisAnyNegative_NOK() {
        final long a = 1;
        final long b = 2;

        assertFalse(NumberUtils.isAnyNegative(a, b));
    }

    @Test
    public void testIsNegative_OK() {
        final long a = -1L;
        assertTrue(NumberUtils.isNegative(a));

        final int b = -1;
        assertTrue(NumberUtils.isNegative(b));

        final double c = -0.0000000000001;
        assertTrue(NumberUtils.isNegative(c));

        final float d = -3333.4f;
        assertTrue(NumberUtils.isNegative(d));

        final Double e = -33.33;
        assertTrue(NumberUtils.isNegative(e));

        final Integer f = -4;
        assertTrue(NumberUtils.isNegative(f));

        final Long g = -10L;
        assertTrue(NumberUtils.isNegative(g));

        final Float h = -0.444f;
        assertTrue(NumberUtils.isNegative(h));

        final Double z = -0.0;
        assertTrue(NumberUtils.isNegative(z));
    }

    @Test
    public void testIsNegative_NOK() {
        final long a = 1L;
        assertFalse(NumberUtils.isNegative(a));

        final int b = 1;
        assertFalse(NumberUtils.isNegative(b));

        final double c = 0.0000000000001;
        assertFalse(NumberUtils.isNegative(c));

        final float d = 3333.4f;
        assertFalse(NumberUtils.isNegative(d));

        final Double e = 33.33;
        assertFalse(NumberUtils.isNegative(e));

        final Integer f = 4;
        assertFalse(NumberUtils.isNegative(f));

        final Long g = 10L;
        assertFalse(NumberUtils.isNegative(g));

        final Float h = 0.444f;
        assertFalse(NumberUtils.isNegative(h));

        final Double z = 0.0;
        assertFalse(NumberUtils.isNegative(z));
    }

}
