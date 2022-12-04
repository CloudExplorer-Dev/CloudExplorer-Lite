package org.openstack4j.openstack.manila.domain.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.ModelEntity;

/**
 * Actions to force down/force up manila services
 */
public class ServiceActions implements ModelEntity {
    @JsonProperty("forced_down")
    public boolean isForcedDown;
    private String binary;
    private String host;

    private ServiceActions(String binary, String host, boolean isForcedDown) {
        this.binary = binary;
        this.host = host;
        this.isForcedDown = isForcedDown;
    }

    public static ServiceActions forceUp(String binary, String host) {
        return new ServiceActions(binary, host, false);
    }

    public static ServiceActions forceDown(String binary, String host) {
        return new ServiceActions(binary, host, true);
    }

    public String getBinary() {
        return binary;
    }

    public String getHost() {
        return host;
    }

    @JsonProperty("forced_down")
    public boolean isForcedDown() {
        return isForcedDown;
    }
}
