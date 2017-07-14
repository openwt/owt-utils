package com.owt.utils;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base64Utils, utility class for encode or decode base 64 strings
 *
 * @author DBO Open Web Technology
 *
 */
public final class Base64Utils
{
    private static final String BASE64_ERROR_MSG = "BASE64: Exception occured";
    private static final Logger LOGGER = LoggerFactory.getLogger(Base64Utils.class);
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * Base64Utils constructor
     */
    private Base64Utils()
    {
    }

    /**
     * decode to byte array
     *
     * @param string, base64 string to decode
     * @return decoded byte array
     */
    public static byte[] decode(final String string)
    {
        return Base64.getDecoder().decode(string);
    }

    /**
     * decode to string with default encoding
     *
     * @param string, base64 string to decode
     * @return decoded string or null if an exception occurred
     */
    public static String decodeToString(final String string)
    {
        return decodeToString(string, DEFAULT_ENCODING);
    }

    /**
     * decode to string with specified encoding
     *
     * @param string, base64 string to decode
     * @param encoding
     * @return decoded string or null if an exception occurred
     */
    public static String decodeToString(final String string, final String encoding)
    {
        try {
            return new String(Base64.getDecoder().decode(string), encoding);
        }
        catch (final Exception e) {
            LOGGER.warn(BASE64_ERROR_MSG, e);
            return null;
        }
    }

    /**
     * encode with default encoding
     *
     * @param bytes, bytes to encode
     * @return encoded string or null if an exception occurred
     */
    public static byte[] encode(final byte[] bytes)
    {
        return Base64.getEncoder().encode(bytes);
    }

    /**
     * encode with default encoding
     *
     * @param bytes, base64 bytes to encode
     * @return encoded string or null if an exception occurred
     */
    public static String encodeToString(final byte[] bytes)
    {
        try {
            return Base64.getEncoder().encodeToString(bytes);
        }
        catch (final Exception e) {
            LOGGER.warn(BASE64_ERROR_MSG, e);
            return null;
        }
    }

    /**
     * encode with default encoding
     *
     * @param string, base64 string to encode
     * @return encoded string or null if an exception occurred
     */
    public static String encodeToString(final String string)
    {
        return encodeToString(string, DEFAULT_ENCODING);
    }

    /**
     * encode
     *
     * @param string, base64 string to encode
     * @param encoding
     * @return encoded string or null if an exception occurred
     */
    public static String encodeToString(final String string, final String encoding)
    {
        try {
            return Base64.getEncoder().encodeToString(string.getBytes(encoding));
        }
        catch (final Exception e) {
            LOGGER.warn(BASE64_ERROR_MSG, e);
            return null;
        }
    }
}
