package com.owt.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.CacheControl;

/**
 * WebUtils, utility class web related
 *
 * @author DBO, Open Web Technology
 * @since 26 October 2015
 *
 */
public final class WebUtils
{
    private static final String XHR = "XMLHttpRequest";

    private static final String HTTP_PROTOCOL = "http";
    private static final String HTTP_PREFIX = HTTP_PROTOCOL + "://";

    /* HTTP HEADERS */
    private static final String HTTP_HEADER_X_REQUESTED_WITH = "X-Requested-With";

    /* Header Cache Constants */
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_CACHE_PRAGMA = "Pragma";

    private static final String HEADER_CACHE_VALUE_NO_CACHE = "no-cache";
    private static final String HEADER_CACHE_CONTROL_VALUE = "max-age=0, private, must-revalidate, no-cache, no-store";

    private static final String HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    private static final String HEADER_CACHE_DATE_EXPIRES = "Expires";
    private static final Integer HEADER_CACHE_DATE_EXPIRES_ZERO = 0;

    // Official RFC 5322 regex implementation with limitation on domain name
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    private WebUtils()
    {

    }

    public static Object getSessionAttribute(final HttpServletRequest request, final String name)
    {
        return org.springframework.web.util.WebUtils.getSessionAttribute(request, name);
    }

    public static void setSessionAttribute(final HttpServletRequest request, final String name, final Object value)
    {
        org.springframework.web.util.WebUtils.setSessionAttribute(request, name, value);
    }

    /**
     * Set Response without cache.
     *
     * @param response response to set
     * @throws Exception if invalide Argument
     */
    public static void setNoCache(final HttpServletResponse response)
    {
        if (response != null) {
            response.setHeader(HEADER_CACHE_CONTROL, HEADER_CACHE_CONTROL_VALUE); // HTTP 1.1
            response.setHeader(HEADER_CACHE_PRAGMA, HEADER_CACHE_VALUE_NO_CACHE); // HTTP 1.
            response.setDateHeader(HEADER_CACHE_DATE_EXPIRES, HEADER_CACHE_DATE_EXPIRES_ZERO); // Proxies.
        }
    }

    /**
     * Set Response with cache.
     *
     * @param response response to set
     * @throws Exception if invalide Argument
     */
    public static void setCache(final HttpServletResponse response, final CacheControl cacheControl)
    {
        if (response != null) {
            if (cacheControl != null) {
                response.setHeader(HEADER_CACHE_CONTROL, cacheControl.getHeaderValue()); // HTTP
            } else {
                setNoCache(response);
            }
        }
    }

    /**
     * Set the access control headers.
     *
     * @param routeService the routeService
     * @param request the HTTP request
     * @param response the HTTP response
     */
    public static void setAccessControlHeader(final String url, final HttpServletResponse response)
    {
        response.setHeader(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, url);
        response.setHeader(HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
    }

    public static String addHttpPrefixToUrlIfMissing(final String url)
    {
        return StringUtils.isBlank(url) || url.toLowerCase().startsWith(HTTP_PROTOCOL) ? url : HTTP_PREFIX + url;
    }

    /**
     * Test if current request is xhr (ajax) or not
     *
     * @param request request to check
     * @return true if ajax
     */
    public static boolean isXhr(final HttpServletRequest request)
    {
        return XHR.equals(request.getHeader(HTTP_HEADER_X_REQUESTED_WITH));
    }

    public static boolean isValidEmail(final String email)
    {
        final Pattern regexPattern = Pattern.compile(EMAIL_REGEX);
        final Matcher match = regexPattern.matcher(email);
        return match.matches();
    }

    /**
     * Takes UTF-8 strings and encodes non-ASCII as ampersand-octothorpe-digits-semicolon
     * HTML-encoded characters.
     *
     * @param raw UTF-8 string to encode
     * @return HTML-encoded String
     */
    @Deprecated
    public static String htmlEncode(final String raw)
    {
        final StringBuilder html = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            final Character character = raw.charAt(i);
            if (CharUtils.isAscii(character)) {
                // Encode common HTML equivalent characters
                html.append(StringEscapeUtils.escapeHtml4(character.toString()));
            } else {
                // Why isn't this done in escapeHtml4()?
                html.append(String.format("&#x%x;", Character.codePointAt(raw, i)));
            }
        }
        return html.toString();
    }

}
