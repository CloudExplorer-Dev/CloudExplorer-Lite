package org.openstack4j.model.network;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The IPv6 router advertisement specifies whether the networking service should transmit ICMPv6 packets, for a subnet
 *
 * @author Taemin
 */
public enum Ipv6RaMode {
    SLAAC("slaac"),
    DHCPV6_STATEFUL("dhcpv6-stateful"),
    DHCPV6_STATELESS("dhcpv6-stateless");

    private final String ipv6RaMode;

    private Ipv6RaMode(String ipv6RaMode) {
        this.ipv6RaMode = ipv6RaMode;
    }

    @JsonCreator
    public static Ipv6RaMode forValue(String value) {
        if (value != null) {
            for (Ipv6RaMode v : Ipv6RaMode.values()) {
                if (v.ipv6RaMode.equalsIgnoreCase(value))
                    return v;
            }
        }
        return null;
    }

    @JsonValue
    public String getIpv6RaMode() {
        return ipv6RaMode;
    }
}
