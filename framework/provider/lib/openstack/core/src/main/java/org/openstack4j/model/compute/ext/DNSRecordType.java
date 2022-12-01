package org.openstack4j.model.compute.ext;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * DNS Record Types
 *
 * @author Jeremy Unruh
 */
public enum DNSRecordType {

    /**
     * a host address
     */
    A,
    /**
     * an authoritative name server
     */
    NS,
    /**
     * a mail destination (Obsolete - use MX)
     */
    MD,
    /**
     * a mail forwarder (Obsolete - use MX)
     */
    MF,
    /**
     * the canonical name for an alias
     */
    CNAME,
    /**
     * marks the start of a zone of authority
     */
    SOA,
    /**
     * a mailbox domain name (EXPERIMENTAL)
     */
    MB,
    /**
     * a mail group member (EXPERIMENTAL)
     */
    MG,
    /**
     * a mail rename domain name (EXPERIMENTAL)
     */
    MR,
    /**
     * a null RR (EXPERIMENTAL)
     */
    NULL,
    /**
     * a well known service description
     */
    WKS,
    /**
     * a domain name pointer
     */
    PTR,
    /**
     * host information
     */
    HINFO,
    /**
     * mailbox or mail list information
     */
    MINFO,
    /**
     * mail exchange
     */
    MX,
    /**
     * text strings
     */
    TXT;

    @JsonCreator
    public static DNSRecordType forValue(String value) {
        if (value != null) {
            for (DNSRecordType rt : DNSRecordType.values()) {
                if (rt.name().equalsIgnoreCase(value))
                    return rt;
            }
        }
        return DNSRecordType.A;
    }

    @JsonValue
    public String value() {
        return name();
    }
}
