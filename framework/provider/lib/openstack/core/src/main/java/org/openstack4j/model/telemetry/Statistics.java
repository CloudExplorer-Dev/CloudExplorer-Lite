package org.openstack4j.model.telemetry;

import org.openstack4j.model.ModelEntity;

import java.util.Date;

/**
 * Computed Statistics for a Query against a Meter
 *
 * @author Jeremy Unruh
 */
public interface Statistics extends ModelEntity {

    /**
     * @return The average of all of the volume values seen in the data
     */
    Double getAvg();

    /**
     * @return The number of samples seen
     */
    Integer getCount();

    /**
     * @return The difference, in seconds, between the oldest and newest timestamp
     */
    Double getDuration();

    /**
     * @return UTC date and time of the earliest timestamp, or the query start time
     */
    Date getDurationStart();

    /**
     * @return UTC date and time of the oldest timestamp, or the query end time
     */
    Date getDurationEnd();

    /**
     * @return The maximum volume seen in the data
     */
    Double getMax();

    /**
     * @return The minimum volume seen in the data
     */
    Double getMin();

    /**
     * @return The total of all of the volume values seen in the data
     */
    Double getSum();

    /**
     * @return The difference, in seconds, between the period start and end
     */
    Integer getPeriod();

    /**
     * @return UTC date and time of the period start
     */
    Date getPeriodStart();

    /**
     * @return UTC date and time of the period end
     */
    Date getPeriodEnd();

    /**
     * @return The unit type of the data set
     */
    String getUnit();

    /**
     * @return The group-by of the data set
     */
    String getGroupBy();
}
