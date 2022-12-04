package org.openstack4j.openstack.telemetry.builder;


import org.openstack4j.model.telemetry.builder.AlarmBuilder;
import org.openstack4j.model.telemetry.builder.TelemetryBuilders;
import org.openstack4j.openstack.telemetry.domain.CeilometerAlarm;

/**
 * The Ceilometer V3 Builders
 */
public class CeilometerBuilders implements TelemetryBuilders {

    @Override
    public AlarmBuilder alarm() {
        return CeilometerAlarm.builder();
    }
}
