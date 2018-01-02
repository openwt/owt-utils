package com.owt.utils;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NetUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NetUtils.class);

    private static final String HEADER_HTTP_X_FORWARDED_FOR = "X-Forwarded-For";

    private NetUtils()
    {

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
            if (isEmpty(ipAddress) || isNotValidIPv4(ipAddress)) {
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
    public static boolean isNotValidIPv4(final String ipAddress)
    {
        return !isValidIPv4(ipAddress);
    }

    /**
     * Checks if the specified string is a valid IP address.
     *
     * @param ipAddress the string to validate
     * @return true if the string validates as an IP address
     */
    public static boolean isValidIPv4(final String ipAddress)
    {
        return InetAddressValidator.getInstance().isValidInet4Address(ipAddress);
    }

    /**
     * Checks if the specified string is a valid IP address.
     *
     * @param ipAddress the string to validate
     * @return true if the string validates as an IP address
     */
    public static boolean isIPInRange(final String ipAddress, final String cidrSubnet)
    {
        if (isNoneBlank(ipAddress, cidrSubnet)) {
            try {
                final String[] paxSubnetAddresses = new SubnetUtils(cidrSubnet).getInfo().getAllAddresses();
                return Stream.of(paxSubnetAddresses).anyMatch(paxSubnetAddress -> paxSubnetAddress.equals(ipAddress));
            }
            catch (final Exception e) {
                LOGGER.warn("Unable to parse ip {} or CIDR {}", ipAddress, cidrSubnet, e);
            }
        }

        return false;
    }
}
