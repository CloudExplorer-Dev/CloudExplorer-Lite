package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.AllowedAddressPair;

/**
 * A Fixed IP Address
 *
 * @author Jeremy Unruh
 */
public class NeutronAllowedAddressPair implements AllowedAddressPair {

    private static final long serialVersionUID = 1L;

    @JsonProperty("ip_address")
    private String ipAddress;

    @JsonProperty("mac_address")
    private String macAddress;

    public NeutronAllowedAddressPair() {
    }

    public NeutronAllowedAddressPair(String ipAddress, String macAddress) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("ipAddress", ipAddress).add("macAddress", macAddress).toString();
    }

}
