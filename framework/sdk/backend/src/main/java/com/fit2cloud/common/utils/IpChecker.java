package com.fit2cloud.common.utils;

import com.fit2cloud.common.constants.IpConstants;
import inet.ipaddr.IPAddressString;

public class IpChecker {

    /**
     * 判断是否为IPv4的netmask
     *
     * @param netmask
     * @return
     */
    public static boolean isNetmask(String netmask) {
        return IpConstants.ALL_NETMASK.contains(netmask);
    }

    public static boolean isIpv4(String ipaddress) {
        try {
            IPAddressString ip = new IPAddressString(ipaddress);
            return ip.isIPv4();
        } catch (Exception ignore) {
        }
        return false;
    }

    public static boolean isIpv6(String ipaddress) {
        try {
            IPAddressString ip = new IPAddressString(ipaddress);
            return ip.isIPv6();
        } catch (Exception ignore) {
        }
        return false;
    }

}
