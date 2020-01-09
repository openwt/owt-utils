package com.owt.test.utils;


import com.owt.utils.Base64Utils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static com.owt.test.ThrowableAssertion.assertThrown;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class Base64UtilsTest {

    @Test
    public void testEncodeByte() {
        final byte[] bytes = {65, 66, 67};
        final byte[] encoded = Base64Utils.encode(bytes);

        assertNotNull(encoded);
        assertEquals("QUJD", new String(encoded, StandardCharsets.UTF_8));
    }

    @Test
    public void testEncodeToString() {
        final String encoded = Base64Utils.encodeToString("ABC");
        assertNotNull(encoded);
        assertEquals("QUJD", encoded);
    }

    @Test
    public void testEncodeFail() {
        assertThrown(() -> Base64Utils.encode(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testEncodeToStringWithOtherEncoding() {
        final String encoded = Base64Utils.encodeToString("ABC", StandardCharsets.ISO_8859_1);
        assertNotNull(encoded);
        assertEquals("QUJD", encoded);
    }

    @Test
    public void testDecode() {
        final byte[] decoded = Base64Utils.decode("QUJD");
        assertNotNull(decoded);
        assertEquals("ABC", new String(decoded, StandardCharsets.UTF_8));
    }

    @Test
    public void testDecodeFail() {
        assertThrown(() -> Base64Utils.decode("àààààà")).isInstanceOf(IllegalArgumentException.class);

        assertThrown(() -> Base64Utils.decode(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testDecodeString() {
        final String decoded = Base64Utils.decodeToString("QUJD");
        assertNotNull(decoded);
        assertEquals("ABC", decoded);
    }

    @Test
    public void testDecodeStringWithOtherEncoding() {
        final String decoded = Base64Utils.decodeToString("QUJD", StandardCharsets.ISO_8859_1);
        assertNotNull(decoded);
        assertEquals("ABC", decoded);
    }
}
