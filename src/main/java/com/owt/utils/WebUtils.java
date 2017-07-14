package com.owt.utils;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 * WebUtils, utility class web related
 *
 * @author DBO, Open Web Technology
 * @since 26 October 2015
 *
 */
public final class WebUtils
{
    public static final String HEADER_X_REQUESTED_WITH = "X-Requested-With";
    public static final String HEADER_HTTP_X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String XHR = "XMLHttpRequest";
    public static final String HTTP_JSON_RESPONSE_CONTEXT = "application/json";

    private WebUtils()
    {

    }

    /**
     * Test if current request is xhr (ajax) or not
     *
     * @param request request to check
     * @return true if ajax
     */
    public static boolean isXhr(final HttpServletRequest request)
    {
        return XHR.equals(request.getHeader(HEADER_X_REQUESTED_WITH));
    }

    /**
     * Return the client IP from headers
     *
     * @param HttpServletRequest request request to check
     * @return String
     */
    public static String getClientIP(final HttpServletRequest request)
    {
        String ipAddress = "";

        if (request != null) {
            ipAddress = request.getHeader(HEADER_HTTP_X_FORWARDED_FOR);
            if (isEmpty(ipAddress) || isNotValidIP(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
        }

        return ipAddress;
    }

    /**
     * Checks if the specified string is a not valid IP address.
     *
     * @param ipAddress the string to validate
     * @return true if the string validates as not an IP address
     */
    public static boolean isNotValidIP(final String ipAddress)
    {
        return !isValidIP(ipAddress);
    }

    /**
     * Checks if the specified string is a valid IP address.
     *
     * @param ipAddress the string to validate
     * @return true if the string validates as an IP address
     */
    public static boolean isValidIP(final String ipAddress)
    {
        // TODO: add support for ip v6
        return InetAddressValidator.getInstance().isValidInet4Address(ipAddress);
    }

    public static boolean isValidEmail(final String email)
    {
        // Official RFC 5322 regex implementation with limitation on domain name
        final String regex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        final Pattern regexPattern = Pattern.compile(regex);
        final Matcher match = regexPattern.matcher(email);
        return match.matches();
    }

    public static boolean matchRequestUri(final HttpServletRequest request, final List<String> urlPattern)
    {
        return request != null
                && request.getRequestURI() != null
                && urlPattern.stream().anyMatch(request.getRequestURI()::contains);

    }

    /**
     * Return true if the request URI is matched by the url pattern
     *
     * @param request
     * @param urlPattern
     * @return
     */
    public static boolean matchRequestUri(final HttpServletRequest request, final String... urlPattern)
    {
        return matchRequestUri(request, Arrays.asList(urlPattern));
    }

    /**
     * Takes UTF-8 strings and encodes non-ASCII as ampersand-octothorpe-digits-semicolon
     * HTML-encoded characters.
     *
     * @param raw UTF-8 string to encode
     * @return HTML-encoded String
     */
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
