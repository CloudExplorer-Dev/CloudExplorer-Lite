package org.openstack4j.api.telemetry;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.telemetry.Alarm;

import java.util.List;

/**
 * Provides alarms within an OpenStack deployment
 *
 * @author Massimiliano Romano
 */
public interface AlarmService extends RestService {

    /**
     * Return all alarms.
     *
     * @return list of all alarms
     */
    List<? extends Alarm> list();

    /**
     * Return a specified alarm
     *
     * @return the alarm
     */
    Alarm getById(String id);

    /**
     * Update a specified alarm
     */
    void update(String id, Alarm a);

    /**
     * Create an alarm
     */
    Alarm create(Alarm alarm);

    /**
     * Delete a specified alarm
     */
    ActionResponse delete(String id);
}
