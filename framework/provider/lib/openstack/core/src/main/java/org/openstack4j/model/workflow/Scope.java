package org.openstack4j.model.workflow;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Renat Akhmerov.
 */
public enum Scope {
    /**
     * Entities with this scope belong to all tenants.
     */
    @JsonProperty("public")
    PUBLIC,

    /**
     * Entities with this scope belong to one tenant.
     */
    @JsonProperty("private")
    PRIVATE;
}
