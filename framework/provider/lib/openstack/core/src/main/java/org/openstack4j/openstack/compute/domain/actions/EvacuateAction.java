package org.openstack4j.openstack.compute.domain.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.compute.actions.EvacuateOptions;

/**
 * Evacuate for a server from a failed host to a new host
 */
@JsonRootName("evacuate")
public class EvacuateAction implements ServerAction {

    private static final long serialVersionUID = 1L;

    @JsonProperty("host")
    private String host;

    @JsonProperty("adminPass")
    private String adminPass;

    @JsonProperty("onSharedStorage")
    private boolean onSharedStorage;

    public static EvacuateAction create(EvacuateOptions options) {
        EvacuateAction action = new EvacuateAction();
        action.host = options.getHost();
        action.adminPass = options.getAdminPass();
        action.onSharedStorage = options.isOnSharedStorage();
        return action;
    }

    public String getHost() {
        return host;
    }

    @JsonIgnore
    public String getAdminPass() {
        return adminPass;
    }

    @JsonIgnore
    public boolean isOnSharedStorage() {
        return onSharedStorage;
    }

    @Override
    public String toString() {
        return "evacuate";
    }

}
