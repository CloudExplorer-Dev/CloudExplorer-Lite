package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.compute.ServerCreate;

import java.util.Map;

/**
 * An entity for build a Server with Scheduler hints
 *
 * @author octopus zhang
 */
public class NovaServerCreateWithHintsWrapper implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("os:scheduler_hints")
    private Map<String, Object> schedulerHints;
    private ServerCreate server;

    public static NovaServerCreateWithHintsWrapper wrap(ServerCreate server) {
        NovaServerCreateWithHintsWrapper wrapper = new NovaServerCreateWithHintsWrapper();
        wrapper.server = server;
        wrapper.schedulerHints = server.getSchedulerHints();
        return wrapper;
    }

    public Map<String, Object> getSchedulerHints() {
        return schedulerHints;
    }

    public ServerCreate getServer() {
        return server;
    }

}
