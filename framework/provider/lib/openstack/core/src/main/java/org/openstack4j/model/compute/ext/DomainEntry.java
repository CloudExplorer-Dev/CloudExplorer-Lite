package org.openstack4j.model.compute.ext;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openstack4j.model.ModelEntity;

/**
 * A Floating IP DNS Extension - Domain Entry
 *
 * @author Jeremy Unruh
 */
@Deprecated
public interface DomainEntry extends ModelEntity {

    /**
     * The associated Availability Zone or NULL
     *
     * @return availability zone or null
     */
    String getAvailabilityZone();

    /**
     * The FQDN domain name
     *
     * @return the domain name
     */
    String getDomain();

    /**
     * The associated project name
     *
     * @return the project or null if not defined
     */
    String getProject();

    /**
     * The scope of this domain name
     *
     * @return the current scope
     */
    Scope getScope();

    @Deprecated
    public enum Scope {
        PUBLIC,
        PRIVATE;

        @JsonCreator
        public static Scope forValue(String value) {
            if (value != null) {
                for (Scope s : Scope.values()) {
                    if (s.name().equalsIgnoreCase(value))
                        return s;
                }
            }
            return Scope.PUBLIC;
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }

}
