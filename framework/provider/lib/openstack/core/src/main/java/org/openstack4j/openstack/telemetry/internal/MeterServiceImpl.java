package org.openstack4j.openstack.telemetry.internal;

import org.openstack4j.api.telemetry.MeterService;
import org.openstack4j.model.telemetry.Meter;
import org.openstack4j.model.telemetry.MeterSample;
import org.openstack4j.model.telemetry.SampleCriteria;
import org.openstack4j.model.telemetry.SampleCriteria.NameOpValue;
import org.openstack4j.model.telemetry.Statistics;
import org.openstack4j.openstack.common.ListEntity;
import org.openstack4j.openstack.telemetry.domain.CeilometerMeter;
import org.openstack4j.openstack.telemetry.domain.CeilometerMeterSample;
import org.openstack4j.openstack.telemetry.domain.CeilometerStatistics;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides Measurements against Meters within an OpenStack deployment
 *
 * @author Jeremy Unruh
 */
public class MeterServiceImpl extends BaseTelemetryServices implements MeterService {

    private static final String FIELD = "q.field";
    private static final String OPER = "q.op";
    private static final String VALUE = "q.value";
    private static final String LIMIT = "limit";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Meter> list() {
        CeilometerMeter[] meters = get(CeilometerMeter[].class, uri("/meters")).execute();
        return wrapList(meters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends MeterSample> samples(String meterName) {
        checkNotNull(meterName);

        CeilometerMeterSample[] samples = get(CeilometerMeterSample[].class, uri("/meters/%s", meterName)).execute();
        return wrapList(samples);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends MeterSample> samples(String meterName, SampleCriteria criteria) {
        checkNotNull(meterName);
        checkNotNull(criteria);
        Invocation<CeilometerMeterSample[]> invocation = get(CeilometerMeterSample[].class, uri("/meters/%s", meterName));
        if (criteria.getLimit() > 0) {
            invocation.param(LIMIT, criteria.getLimit());
        }
        if (!criteria.getCriteriaParams().isEmpty()) {
            for (NameOpValue c : criteria.getCriteriaParams()) {
                invocation.param(FIELD, c.getField());
                invocation.param(OPER, c.getOperator().getQueryValue());
                invocation.param(VALUE, c.getValue());
            }
        }

        CeilometerMeterSample[] samples = invocation.execute();
        return wrapList(samples);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Statistics> statistics(String meterName) {
        return statistics(meterName, null, 0);
    }

    @Override
    public List<? extends Statistics> statistics(String meterName, int period) {
        return statistics(meterName, null, period);
    }

    @Override
    public List<? extends Statistics> statistics(String meterName, SampleCriteria criteria) {
        return statistics(meterName, criteria, 0);
    }

    @Override
    public List<? extends Statistics> statistics(String meterName, SampleCriteria criteria, int period) {
        checkNotNull(meterName);
        checkNotNull(criteria);
        Invocation<CeilometerStatistics[]> invocation = get(CeilometerStatistics[].class, uri("/meters/%s/statistics", meterName))
                .param(period > 0, "period", period);
        if (criteria.getLimit() > 0) {
            invocation.param(LIMIT, criteria.getLimit());
        }
        if (!criteria.getCriteriaParams().isEmpty()) {
            for (NameOpValue c : criteria.getCriteriaParams()) {
                invocation.param(FIELD, c.getField());
                invocation.param(OPER, c.getOperator().getQueryValue());
                invocation.param(VALUE, c.getValue());
            }
        }
        CeilometerStatistics[] stats = invocation.execute();
        return wrapList(stats);
    }

    @Override
    public void putSamples(List<MeterSample> sampleList, String meterName) {
        ListEntity<MeterSample> listEntity = new ListEntity<MeterSample>(sampleList);
        post(Void.class, uri("/meters/%s", meterName)).entity(listEntity).execute();
    }
}
