package org.openstack4j.model.gbp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Protocol enum
 *
 * @author vinod borole
 */
public enum Protocol {
    TCP,
    UDP,
    ICMP,
    HTTP,
    HTTPS,
    SMTP,
    DNS,
    FTP,
    NONE,
    UNRECOGNIZED;

    @JsonCreator
    public static Protocol forValue(String value) {
        if (value != null) {
            for (Protocol s : Protocol.values()) {
                if (s.name().equalsIgnoreCase(value))
                    return s;
            }
        }
        return Protocol.UNRECOGNIZED;
    }

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }
}
