package com.owt.test.utils;

import static com.owt.test.ThrowableAssertion.assertThrown;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.owt.utils.Base64Utils;

public final class Base64UtilsTest
{
    @Test
    public void testEncodeByte() throws UnsupportedEncodingException
    {
        final byte[] bytes = { 65, 66, 67 };
        final byte[] encoded = Base64Utils.encode(bytes);

        assertNotNull(encoded);
        assertEquals("QUJD", new String(encoded, "UTF-8"));
    }

    @Test
    public void testEncodeToString()
    {
        final String encoded = Base64Utils.encodeToString("ABC");
        assertNotNull(encoded);
        assertEquals("QUJD", encoded);
    }

    @Test
    public void testEncodeFail()
    {
        assertThrown(() -> Base64Utils.encode(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testEncodeToStringWithOtherEncoding()
    {
        final String encoded = Base64Utils.encodeToString("ABC", Charset.forName("ISO-8859-1"));
        assertNotNull(encoded);
        assertEquals("QUJD", encoded);
    }

    @Test
    public void testDecode() throws UnsupportedEncodingException
    {
        final byte[] decoded = Base64Utils.decode("QUJD");
        assertNotNull(decoded);
        assertEquals("ABC", new String(decoded, "UTF-8"));
    }

    @Test
    public void testDecodeFail()
    {
        assertThrown(() -> Base64Utils.decode("àààààà")).isInstanceOf(IllegalArgumentException.class);

        assertThrown(() -> Base64Utils.decode(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testDecodeString()
    {
        final String decoded = Base64Utils.decodeToString("QUJD");
        assertNotNull(decoded);
        assertEquals("ABC", decoded);
    }

    @Test
    public void testDecodeStringWithOtherEncoding()
    {
        final String decoded = Base64Utils.decodeToString("QUJD", Charset.forName("ISO-8859-1"));
        assertNotNull(decoded);
        assertEquals("ABC", decoded);
    }
}
