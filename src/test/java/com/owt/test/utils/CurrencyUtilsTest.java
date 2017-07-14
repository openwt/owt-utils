package com.owt.test.utils;

import static com.owt.utils.CurrencyUtils.convertAmount;
import static com.owt.utils.CurrencyUtils.formatAmountByLocal;
import static com.owt.utils.CurrencyUtils.roundAmount;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.junit.Test;

/**
 * Created by Open Web Technology.
 *
 * @author Loïc Bernollin Open Web Technology
 * @since 5 nov. 2015
 * 
 */
public class CurrencyUtilsTest
{

    @Test
    public void testZeroAmount()
    {
        final String targetAmount = convertAmount(0.00, 0.00, new Locale("en", "US"));
        assertNotNull(targetAmount);
        assertEquals(targetAmount, "");
    }

    @Test
    public void testCorrectAmount()
    {
        final String targetAmount = convertAmount(1.00, 0.90, new Locale("en", "US"));
        assertNotNull(targetAmount);
        assertEquals(targetAmount, "0.90");
    }

    @Test
    public void testAmountUS()
    {
        final String targetAmount = convertAmount(2.5, 1.0, new Locale("en", "US"));
        assertNotNull(targetAmount);
        assertEquals("targetAmount:" + targetAmount, "2.50", targetAmount);
    }

    @Test
    public void testAmountFR()
    {
        final String targetAmount = convertAmount(2.5, 1.0, new Locale("fr", "FR"));
        assertNotNull(targetAmount);
        assertEquals("targetAmount:" + targetAmount, "2,50", targetAmount);
    }

    @Test
    public void testRound1()
    {
        final double result = roundAmount(19.99 / 2.00);
        assertEquals("9.99", formatAmountByLocal(result, new Locale("en", "US")));
        assertEquals(9.99, result, 0.001);
    }

    @Test
    public void testRound2()
    {
        final double result = roundAmount(5.99 / 2.00);
        assertEquals("2.99", formatAmountByLocal(result, new Locale("en", "US")));
        assertEquals(2.99, result, 0.001);
    }

    @Test
    public void testRound3()
    {
        final double result = roundAmount(19.99 - 1.00);
        assertEquals("18.99", formatAmountByLocal(result, new Locale("en", "US")));
        assertEquals(18.99, result, 0.001);
    }
}
