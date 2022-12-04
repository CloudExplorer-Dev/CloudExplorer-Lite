package org.openstack4j.model.gbp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.gbp.builder.PolicyActionCreateBuilder;

/**
 * Policy Action Model Entity
 *
 * @author vinod borole
 */
public interface PolicyAction extends Buildable<PolicyActionCreateBuilder>, Resource {

    /**
     * Gets the Action value
     *
     * @return the Action value
     */
    String getActionValue();

    /**
     * Gets the Action Type
     *
     * @return the Action Type
     */
    PolicyActionProtocol getActionType();

    /**
     * Is Policy Action shared
     *
     * @return the true if shared and false if not shared
     */
    boolean isShared();

    /**
     * Gets the description
     *
     * @return the description
     */
    String getDescription();

    public enum PolicyActionProtocol {
        ALLOW,
        REDIRECT,
        COPY,
        LOG,
        QoS,
        UNRECOGNIZED;

        @JsonCreator
        public static PolicyActionProtocol forValue(String value) {
            if (value != null) {
                for (PolicyActionProtocol s : PolicyActionProtocol.values()) {
                    if (s.name().equalsIgnoreCase(value))
                        return s;
                }
            }
            return PolicyActionProtocol.UNRECOGNIZED;
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }

}
