package org.openstack4j.openstack.telemetry.internal;

import org.openstack4j.api.telemetry.CapabilitiesService;
import org.openstack4j.model.telemetry.Capabilities;
import org.openstack4j.openstack.telemetry.domain.CeilometerCapabilities;

/**
 * Provides Measurements for Telemetry capabilities within an OpenStack deployment
 *
 * @author Shital Patil
 */
public class CapabilitiesServiceImpl extends BaseTelemetryServices implements CapabilitiesService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Capabilities get() {
        CeilometerCapabilities capabilities = get(CeilometerCapabilities.class, uri("/capabilities")).execute();
        return capabilities;
    }

}
