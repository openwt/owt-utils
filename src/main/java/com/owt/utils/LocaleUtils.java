package com.owt.utils;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LocaleUtils Utility class for parsing and validation based on standard ISO-639 Created by Open
 * Web Technology.
 *
 * @author DBO, Open Web Technology
 * @since 15 jan 2015
 *
 */
public final class LocaleUtils
{
    private static final int MAX_LOCAL_PARTS = 4;
    private static final Logger LOGGER = LoggerFactory.getLogger(LocaleUtils.class);
    private static final Set<String> ISO_LANGUAGES = new HashSet<>(Arrays.asList(Locale.getISOLanguages()));
    private static final Set<String> ISO_COUNTRIES = new HashSet<>(Arrays.asList(Locale.getISOCountries()));

    private LocaleUtils()
    {
    }

    public static boolean isValidISO639Language(final String s)
    {
        return ISO_LANGUAGES.contains(s);
    }

    public static boolean isValidISO639Country(final String s)
    {
        return ISO_COUNTRIES.contains(s);
    }

    private static Locale parseLocale(final String locale)
    {
        if (isNotEmpty(locale)) {

            final String[] parts = locale.split("_");

            if (parts.length > 0 && parts.length < MAX_LOCAL_PARTS) {

                final String language = parts.length > 0 ? parts[0] : "";
                final String country = parts.length > 1 ? parts[1] : "";
                final String variant = parts.length > 2 ? parts[2] : "";

                if (isNotEmpty(language)) {
                    return new Locale(language, country, variant);
                }
            }
        }

        throw new IllegalArgumentException("Invalid locale: " + locale);
    }

    public static boolean isValidLocaleString(final String localeStr)
    {
        try {
            final Locale locale = parseLocale(localeStr);
            return locale.getISO3Language() != null && locale.getISO3Country() != null;
        }
        catch (final RuntimeException e) {
            LOGGER.debug("isValidLocaleString - Exception occured", e);
            return false;
        }
    }

    public static boolean isValidLocale(final Locale locale)
    {
        try {
            return locale != null && locale.getISO3Language() != null && locale.getISO3Country() != null;
        }
        catch (final MissingResourceException e) {
            LOGGER.debug("IsValidLocale - Exception occured", e);
            return false;
        }
    }
}
