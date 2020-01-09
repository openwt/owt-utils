package com.owt.test.utils;

import com.owt.utils.NetUtils;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NetUtilsTest {

    @Test
    public void TestIsValidIP() {
        assertTrue(NetUtils.isValidIPv4("1.1.1.1"));
        assertTrue(NetUtils.isValidIPv4("255.255.255.255"));
        assertTrue(NetUtils.isValidIPv4("192.168.1.1"));
        assertTrue(NetUtils.isValidIPv4("10.10.1.1"));
        assertTrue(NetUtils.isValidIPv4("132.254.111.10"));
        assertTrue(NetUtils.isValidIPv4("26.10.2.10"));
        assertTrue(NetUtils.isValidIPv4("127.0.0.1"));

        assertFalse(NetUtils.isValidIPv4("10.10.10"));
        assertFalse(NetUtils.isValidIPv4("10.10"));
        assertFalse(NetUtils.isValidIPv4("10"));
        assertFalse(NetUtils.isValidIPv4("PLOP"));
        assertFalse(NetUtils.isValidIPv4(null));
        assertFalse(NetUtils.isValidIPv4(""));
        assertFalse(NetUtils.isValidIPv4("a.a.a.a"));
        assertFalse(NetUtils.isValidIPv4("10.0.0.a"));
        assertFalse(NetUtils.isValidIPv4("10.10.10.256"));
        assertFalse(NetUtils.isValidIPv4("222.222.2.999"));
        assertFalse(NetUtils.isValidIPv4("999.10.10.20"));
        assertFalse(NetUtils.isValidIPv4("2222.22.22.22"));
        assertFalse(NetUtils.isValidIPv4("22.2222.22.2"));
    }

    @Test
    public void TestIsNotValidIP() {
        assertFalse(NetUtils.isNotValidIPv4("1.1.1.1"));
        assertFalse(NetUtils.isNotValidIPv4("255.255.255.255"));
        assertFalse(NetUtils.isNotValidIPv4("192.168.1.1"));
        assertFalse(NetUtils.isNotValidIPv4("10.10.1.1"));
        assertFalse(NetUtils.isNotValidIPv4("132.254.111.10"));
        assertFalse(NetUtils.isNotValidIPv4("26.10.2.10"));
        assertFalse(NetUtils.isNotValidIPv4("127.0.0.1"));

        assertTrue(NetUtils.isNotValidIPv4("10.10.10"));
        assertTrue(NetUtils.isNotValidIPv4("10.10"));
        assertTrue(NetUtils.isNotValidIPv4("10"));
        assertTrue(NetUtils.isNotValidIPv4("PLOP"));
        assertTrue(NetUtils.isNotValidIPv4(null));
        assertTrue(NetUtils.isNotValidIPv4(""));
        assertTrue(NetUtils.isNotValidIPv4("a.a.a.a"));
        assertTrue(NetUtils.isNotValidIPv4("10.0.0.a"));
        assertTrue(NetUtils.isNotValidIPv4("10.10.10.256"));
        assertTrue(NetUtils.isNotValidIPv4("222.222.2.999"));
        assertTrue(NetUtils.isNotValidIPv4("999.10.10.20"));
        assertTrue(NetUtils.isNotValidIPv4("2222.22.22.22"));
        assertTrue(NetUtils.isNotValidIPv4("22.2222.22.2"));
    }

}
