package org.openstack4j.openstack.telemetry.internal;

import org.openstack4j.api.telemetry.AlarmService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.telemetry.Alarm;
import org.openstack4j.openstack.telemetry.domain.CeilometerAlarm;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides Measurements against Meters within an OpenStack deployment
 *
 * @author Massimiliano Romano
 */
public class AlarmServiceImpl extends BaseTelemetryServices implements AlarmService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Alarm> list() {
        CeilometerAlarm[] alarms = get(CeilometerAlarm[].class, uri("/alarms")).execute();
        return wrapList(alarms);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Alarm getById(String alarmId) {
        checkNotNull(alarmId);
        return get(CeilometerAlarm.class, uri("/alarms/%s", alarmId)).execute();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(String alarmId, Alarm alarm) {
        checkNotNull(alarmId);
        checkNotNull(alarm);

        put(CeilometerAlarm.class, uri("/alarms/%s", alarmId)).entity(alarm).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Alarm create(Alarm alarm) {
        checkNotNull(alarm);
        return post(CeilometerAlarm.class, uri("/alarms")).entity((alarm)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String alarmId) {
        checkNotNull(alarmId);
        return deleteWithResponse(uri("/alarms/%s", alarmId)).execute();
    }
}
