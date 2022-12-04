package org.openstack4j.model.artifact;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * A Glare Artifact Type Enum
 *
 * @author Pavan Vadavi
 */
public enum ArtifactType {

    ALL, HEAT_ENVIRONMENTS, HEAT__TEMPLATES, IMAGES, TOSCA_TEMPLATES, MURANO_PACKAGES;

    @JsonCreator
    public static ArtifactType value(String v) {
        if (v == null) return ALL;
        try {
            return valueOf(v.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ALL;
        }
    }

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }
}
