package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.AbsoluteLimit;
import org.openstack4j.model.compute.Limits;
import org.openstack4j.model.compute.RateLimit;

import java.util.List;

/**
 * Accounts may be pre-configured with a set of thresholds (or limits) to manage capacity and prevent abuse of the system.
 * The system recognizes two kinds of limits: rate limits and absolute limits. Rate limits are thresholds that are reset after a
 * certain amount of time passes. Absolute limits are fixed.
 *
 * @author Jeremy Unruh
 * @see http://docs.openstack.org/api/openstack-compute/2/content/Limits-d1e846.html
 */
@JsonRootName("limits")
public class NovaLimits implements Limits {

    private static final long serialVersionUID = 1L;

    private List<NovaRateLimit> rate;
    private NovaAbsoluteLimit absolute;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends RateLimit> getRate() {
        return rate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbsoluteLimit getAbsolute() {
        return absolute;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("rate", rate).add("absolute", absolute)
                .toString();
    }

}
