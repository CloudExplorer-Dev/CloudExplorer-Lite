package org.openstack4j.openstack.telemetry.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.telemetry.Statistics;
import org.openstack4j.openstack.common.TelemetryDateDeserializer;

import java.util.Date;

/**
 * Computed Statistics for a Query against a Meter
 *
 * @author Jeremy Unruh
 */
public class CeilometerStatistics implements Statistics {

    private static final long serialVersionUID = 1L;

    private Double avg;

    private Integer count;

    private Double duration;

    @JsonDeserialize(using = TelemetryDateDeserializer.class)
    @JsonProperty("duration_start")
    private Date durationStart;

    @JsonDeserialize(using = TelemetryDateDeserializer.class)
    @JsonProperty("duration_end")
    private Date durationEnd;

    private Double max;

    private Double min;

    private Integer period;

    @JsonDeserialize(using = TelemetryDateDeserializer.class)
    @JsonProperty("period_start")
    private Date periodStart;

    @JsonDeserialize(using = TelemetryDateDeserializer.class)
    @JsonProperty("period_end")
    private Date periodEnd;

    private Double sum;

    private String unit;

    private String groupby;

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getAvg() {
        return avg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getCount() {
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getDuration() {
        return duration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDurationStart() {
        return durationStart;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDurationEnd() {
        return durationEnd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getMax() {
        return max;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getMin() {
        return min;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getSum() {
        return sum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPeriod() {
        return period;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getPeriodStart() {
        return periodStart;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getPeriodEnd() {
        return periodEnd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUnit() {
        return unit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGroupBy() {
        return groupby;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("avg", avg).add("count", count).add("duration", duration)
                .add("durationStart", durationStart).add("durationEnd", durationEnd)
                .add("min", min).add("max", max).add("sum", sum).add("period", period)
                .add("periodStart", periodStart).add("periodEnd", periodEnd).add("unit", unit)
                .add("groupby", groupby)
                .toString();
    }

}
