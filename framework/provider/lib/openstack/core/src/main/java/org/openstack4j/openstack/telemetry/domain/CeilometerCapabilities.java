package org.openstack4j.openstack.telemetry.domain;

import org.openstack4j.model.telemetry.Capabilities;

import java.util.Map;

/**
 * A single Representation for capabilities.
 *
 * @author Shital Patil
 */

public class CeilometerCapabilities implements Capabilities {

    private static final long serialVersionUID = 1L;

    Map<String, Boolean> api;

    Map<String, Boolean> storage;

    Map<String, Boolean> eventStorage;

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Boolean> getAPI() {
        return api;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Boolean> getStorage() {
        return storage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Boolean> getEventStorage() {
        return eventStorage;
    }

}
