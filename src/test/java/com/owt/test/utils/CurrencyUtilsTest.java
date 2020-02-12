package com.owt.test.utils;

import org.junit.Test;

import java.util.Locale;

import static com.owt.utils.CurrencyUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Open Web Technology.
 *
 * @author Loïc Bernollin Open Web Technology
 * @since 5 nov. 2015
 */
public class CurrencyUtilsTest {

    @Test
    public void testFormatAmount() {

        assertEquals("0", formatAmount(0.00, "#.##"));
        assertEquals("1", formatAmount(1.0, "#.##"));
        assertEquals("12", formatAmount(12.0, "#.##"));
        assertEquals("1000000000000", formatAmount((double) 1000000000000L, "#.##"));
        assertEquals("3.14", formatAmount(3.14159, "#.##"));
        assertEquals("314159", formatAmount(314159.0, "#.##"));
        assertEquals("314159.14", formatAmount(314159.14159, "#.##"));
        assertEquals("1234.1234", formatAmount(1234.1234, "#.####"));
        assertEquals("1234.1234", formatAmount(1234.1234, "##.####"));
        assertEquals("1.1234", formatAmount(1.1234, "##.####"));
        assertEquals("11234", formatAmount(11234.1234, "#"));

        assertEquals(".00", formatAmount(0.00, "#.00"));
        assertEquals("1.00", formatAmount(1.0, "#.00"));
        assertEquals("12.00", formatAmount(12.0, "#.00"));
        assertEquals("1000000000000.00", formatAmount((double) 1000000000000L, "#.00"));
        assertEquals("3.14", formatAmount(3.14159, "#.00"));
        assertEquals("314159.00", formatAmount(314159.0, "#.00"));
        assertEquals("314159.14", formatAmount(314159.14159, "#.00"));
        assertEquals("1234.1200", formatAmount(1234.12, "##.0000"));
        assertEquals("1234.1234", formatAmount(1234.1234, "##.0000"));
        assertEquals("11234", formatAmount(11234.1234, "0"));

        assertEquals("0", formatAmount(0.0, "0"));
        assertEquals("1.00", formatAmount(1.0, "0.00"));
        assertEquals("12.00", formatAmount(12.0, "0.00"));
        assertEquals("1000000000000.00", formatAmount((double) 1000000000000L, "0.00"));
        assertEquals("3.14", formatAmount(3.14159, "0.00"));
        assertEquals("314159.00", formatAmount(314159.0, "00.00"));
        assertEquals("314159.14", formatAmount(314159.14159, "0.00"));
        assertEquals("1234.12", formatAmount(1234.12, "00.00"));
        assertEquals("1234.1200", formatAmount(1234.12, "00.0000"));
        assertEquals("11234", formatAmount(11234.1234, "0"));
        assertEquals(".12", formatAmount(00.122346546, ".00"));
        assertEquals("123132132.56", formatAmount(123132132.56464654, "0.00"));
    }


    @Test
    public void testZeroAmount() {
        final String targetAmount = convertAmount(0.00, 0.00, new Locale("en", "US"));
        assertNotNull(targetAmount);
        assertEquals("", targetAmount);
    }

    @Test
    public void testCorrectAmount() {
        final String targetAmount = convertAmount(1.00, 0.90, new Locale("en", "US"));
        assertNotNull(targetAmount);
        assertEquals("0.90", targetAmount);
    }

    @Test
    public void testAmountUS() {
        final String targetAmount = convertAmount(2.5, 1.0, new Locale("en", "US"));
        assertNotNull(targetAmount);
        assertEquals("targetAmount:" + targetAmount, "2.50", targetAmount);
    }

    @Test
    public void testAmountFR() {
        final String targetAmount = convertAmount(2.5, 1.0, new Locale("fr", "FR"));
        assertNotNull(targetAmount);
        assertEquals("targetAmount:" + targetAmount, "2,50", targetAmount);
    }

    @Test
    public void testRound1() {
        final double result = roundAmount(19.99 / 2.00);
        assertEquals("9.99", formatAmountByLocal(result, new Locale("en", "US")));
        assertEquals(9.99, result, 0.001);
    }

    @Test
    public void testRound2() {
        final double result = roundAmount(5.99 / 2.00);
        assertEquals("2.99", formatAmountByLocal(result, new Locale("en", "US")));
        assertEquals(2.99, result, 0.001);
    }

    @Test
    public void testRound3() {
        final double result = roundAmount(19.99 - 1.00);
        assertEquals("18.99", formatAmountByLocal(result, new Locale("en", "US")));
        assertEquals(18.99, result, 0.001);
    }
}
