package org.openstack4j.api.telemetry;

import org.openstack4j.common.RestService;

/**
 * Telemetry allows collection of Alarms and Measurements.
 *
 * @author Jeremy Unruh
 */
public interface TelemetryService extends RestService {

    /**
     * Access to Metering (Measurements) API
     *
     * @return the Meter Service API
     */
    MeterService meters();

    /**
     * Access to Alarms API
     *
     * @return the Alarm Service API
     */
    AlarmService alarms();

    /**
     * Access to Events API
     *
     * @return the Event Service API
     */
    EventService events();

    /**
     * Access to Samples API
     *
     * @return the Sample Service API
     */
    SampleService samples();

    /**
     * Access to resource API
     *
     * @return the Resource service API
     */
    ResourceService resources();

    /**
     * Access to capabilities service API
     *
     * @return the capabilities service API
     */
    CapabilitiesService capabilities();
}
