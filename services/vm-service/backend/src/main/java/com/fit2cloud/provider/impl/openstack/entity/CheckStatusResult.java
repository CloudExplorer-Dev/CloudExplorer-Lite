package com.fit2cloud.provider.impl.openstack.entity;

import lombok.Data;
import org.openstack4j.model.compute.Server;

@Data
public class CheckStatusResult {

    private boolean success;
    private String fault;
    private Server server;

    private CheckStatusResult(boolean success, Server server, String fault) {
        this.success = success;
        this.server = server;
        this.fault = fault;
    }

    public static CheckStatusResult success(Server server) {
        return new CheckStatusResult(true, server, null);
    }

    public static CheckStatusResult fail(String fault) {
        return fail(fault, null);
    }

    public static CheckStatusResult fail(String fault, Server server) {
        return new CheckStatusResult(false, server, fault);
    }
}
