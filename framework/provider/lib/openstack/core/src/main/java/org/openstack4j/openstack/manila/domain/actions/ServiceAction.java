package org.openstack4j.openstack.manila.domain.actions;

import org.openstack4j.model.ModelEntity;

/**
 * Actions to enable/disable manila services
 */
public class ServiceAction implements ModelEntity {
    private String binary;
    private String host;

    private ServiceAction(String binary, String host) {
        this.binary = binary;
        this.host = host;
    }

    public static ServiceAction enable(String binary, String host) {
        return new ServiceAction(binary, host);
    }

    public static ServiceAction disable(String binary, String host) {
        return new ServiceAction(binary, host);
    }

    public String getBinary() {
        return binary;
    }

    public String getHost() {
        return host;
    }
}
