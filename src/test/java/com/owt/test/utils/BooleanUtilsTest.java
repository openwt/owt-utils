package com.owt.test.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.owt.utils.BooleanUtils;

/**
 * @author DBO Open Web Technology
 * @since 15 Feb 2016
 * 
 */
public class BooleanUtilsTest
{
    @Test
    public void testIsFalse()
    {
        assertTrue(BooleanUtils.isFalse(false));
        assertFalse(BooleanUtils.isFalse(true));
        assertTrue(BooleanUtils.isFalse(null));
    }

    @Test
    public void testIsTrue()
    {
        assertFalse(BooleanUtils.isTrue(false));
        assertTrue(BooleanUtils.isTrue(true));
        assertFalse(BooleanUtils.isTrue(null));
    }

    @Test
    public void testCast()
    {
        assertFalse(BooleanUtils.toBoolean(""));
        assertFalse(BooleanUtils.toBoolean(0));
        assertFalse(BooleanUtils.toBoolean(null));
        assertFalse(BooleanUtils.toBoolean("trololo"));
        assertFalse(BooleanUtils.toBoolean("false"));
        assertFalse(BooleanUtils.toBoolean("False"));
        assertFalse(BooleanUtils.toBoolean("FALSE"));
        assertFalse(BooleanUtils.toBoolean("truue"));
        assertFalse(BooleanUtils.toBoolean("0"));
        assertFalse(BooleanUtils.toBoolean("1"));

        assertTrue(BooleanUtils.toBoolean(1));
        assertTrue(BooleanUtils.toBoolean("true"));
        assertTrue(BooleanUtils.toBoolean("True"));
        assertTrue(BooleanUtils.toBoolean("TRUE"));

    }

}
