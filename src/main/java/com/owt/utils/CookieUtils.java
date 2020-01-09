package com.owt.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class CookieUtils {
    private static final String DEFAULT_COOKIE_DOMAIN = "*";
    private static final String DEFAULT_COOKIE_PATH = "/";

    private CookieUtils() {


    }

    public static Cookie getCookie(final HttpServletRequest request, final String name) {
        return org.springframework.web.util.WebUtils.getCookie(request, name);
    }

    public static void deleteCookie(final HttpServletResponse response, final String cookieName, final String cookiePath, final String domain) {
        addCookie(response, cookieName, 0, null, cookiePath, domain);
    }

    public static void deleteCookie(final HttpServletResponse response, final String cookieName) {
        addCookie(response, cookieName, 0, null, DEFAULT_COOKIE_PATH, DEFAULT_COOKIE_DOMAIN);
    }

    public static void addCookie(final HttpServletResponse response, final String cookieName, final int maxAge, final String value) {
        addCookie(response, cookieName, maxAge, value, DEFAULT_COOKIE_PATH, DEFAULT_COOKIE_DOMAIN);
    }

    public static void addCookie(final HttpServletResponse response, final String cookieName, final int maxAge, final String value, final String cookiePath, final String domain) {
        final Cookie cookie = new Cookie(cookieName, value);
        cookie.setMaxAge(maxAge);
        cookie.setSecure(true);
        cookie.setDomain(domain);
        cookie.setPath(cookiePath);
        response.addCookie(cookie);
    }
}
