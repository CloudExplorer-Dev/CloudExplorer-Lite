package org.openstack4j.openstack.telemetry.internal;

import org.openstack4j.api.telemetry.SampleService;
import org.openstack4j.model.telemetry.Sample;
import org.openstack4j.model.telemetry.SampleCriteria;
import org.openstack4j.openstack.telemetry.domain.CeiloMeterSample;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides Measurements for Samples within an OpenStack deployment
 *
 * @author Shital Patil
 */

public class SampleServiceImpl extends BaseTelemetryServices implements SampleService {

    private static final String FIELD = "q.field";
    private static final String OPER = "q.op";
    private static final String VALUE = "q.value";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Sample> list() {
        CeiloMeterSample[] samples = get(CeiloMeterSample[].class, uri("/samples")).execute();
        return wrapList(samples);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Sample> list(SampleCriteria criteria) {
        checkNotNull(criteria);
        Invocation<CeiloMeterSample[]> invocation = get(CeiloMeterSample[].class, uri("/samples"));
        if (criteria.getLimit() > 0) {
            invocation.param("limit", criteria.getLimit());
        }

        for (SampleCriteria.NameOpValue c : criteria.getCriteriaParams()) {
            invocation.param(FIELD, c.getField());
            invocation.param(OPER, c.getOperator().getQueryValue());
            invocation.param(VALUE, c.getValue());
        }
        CeiloMeterSample[] samples = invocation.execute();
        return wrapList(samples);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sample get(String sampleId) {
        checkNotNull(sampleId);

        CeiloMeterSample sample = get(CeiloMeterSample.class, uri("/samples/%s", sampleId)).execute();
        return sample;
    }

}
