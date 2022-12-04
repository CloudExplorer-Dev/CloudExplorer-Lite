package org.openstack4j.model.octavia;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * SessionPersistence type
 *
 * @author wei
 */
public enum SessionPersistenceType {
    APP_COOKIE,
    HTTP_COOKIE,
    SOURCE_IP;

    @JsonCreator
    public static SessionPersistenceType forValue(String value) {
        if (value != null) {
            for (SessionPersistenceType s : SessionPersistenceType.values()) {
                if (s.name().equalsIgnoreCase(value)) {
                    return s;
                }
            }
        }
        return SessionPersistenceType.APP_COOKIE;
    }
}
