package com.owt.test.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

import com.owt.utils.LocaleUtils;

public class LocaleUtilsTest
{

    @Test
    public void testValidLanguage()
    {
        assertTrue(LocaleUtils.isValidISO639Language("en"));
        assertTrue(LocaleUtils.isValidISO639Language("ar"));
    }

    @Test
    public void testInvalidLanguage()
    {
        assertFalse(LocaleUtils.isValidISO639Language("en1234"));
        assertFalse(LocaleUtils.isValidISO639Language("eng"));
        assertFalse(LocaleUtils.isValidISO639Language("english"));
        assertFalse(LocaleUtils.isValidISO639Language("ararbic"));
        assertFalse(LocaleUtils.isValidISO639Language("en-EN"));
        assertFalse(LocaleUtils.isValidISO639Language(null));
        assertFalse(LocaleUtils.isValidISO639Language(""));

    }

    @Test
    public void testValidLocaleString()
    {
        assertTrue(LocaleUtils.isValidLocaleString("en"));
        assertTrue(LocaleUtils.isValidLocaleString("ar"));
        assertTrue(LocaleUtils.isValidLocaleString("eng"));
        assertTrue(LocaleUtils.isValidLocaleString("eng_US"));
        assertTrue(LocaleUtils.isValidLocaleString("ar_AR"));
        assertTrue(LocaleUtils.isValidLocaleString("en_gb_gb"));
        assertTrue(LocaleUtils.isValidLocaleString("en__"));
        assertTrue(LocaleUtils.isValidLocaleString("en__us"));

    }

    @Test
    public void testInvalidLocaleString()
    {
        assertFalse(LocaleUtils.isValidLocaleString("en1234"));
        assertFalse(LocaleUtils.isValidLocaleString("english"));
        assertFalse(LocaleUtils.isValidLocaleString("ararbic"));
        assertFalse(LocaleUtils.isValidLocaleString(null));
        assertFalse(LocaleUtils.isValidLocaleString(""));
        assertFalse(LocaleUtils.isValidLocaleString("___"));
        assertFalse(LocaleUtils.isValidLocaleString("_1234_"));
        assertFalse(LocaleUtils.isValidLocaleString("dd_trolo_olol_lo"));
        assertFalse(LocaleUtils.isValidLocaleString("trolo_olol_lo"));
        assertFalse(LocaleUtils.isValidLocaleString("en___________us"));
    }

    @Test
    public void testValidLocale()
    {
        assertTrue(LocaleUtils.isValidLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isValidLocale(Locale.KOREA));
        assertTrue(LocaleUtils.isValidLocale(new Locale("en")));
        assertTrue(LocaleUtils.isValidLocale(new Locale("en", "US")));
    }

    @Test
    public void testInvalidLocale()
    {
        assertFalse(LocaleUtils.isValidLocale(null));
        assertFalse(LocaleUtils.isValidLocale(new Locale("TROLOLOLO")));
    }
}
