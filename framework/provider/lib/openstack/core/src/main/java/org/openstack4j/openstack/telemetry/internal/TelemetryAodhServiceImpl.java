package org.openstack4j.openstack.telemetry.internal;

import org.openstack4j.api.Apis;
import org.openstack4j.api.telemetry.*;

/**
 * @author zhangjianweibj
 */
public class TelemetryAodhServiceImpl implements TelemetryAodhService {

    /**
     * {@inheritDoc}
     */
    @Override
    public MeterService meters() {
        return Apis.get(MeterService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AlarmAodhService alarms() {
        return Apis.get(AlarmAodhService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventService events() {
        return Apis.get(EventService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SampleService samples() {
        return Apis.get(SampleService.class);
    }

    @Override
    public ResourceService resources() {
        return Apis.get(ResourceService.class);
    }

    @Override
    public CapabilitiesService capabilities() {
        return Apis.get(CapabilitiesService.class);
    }

}
