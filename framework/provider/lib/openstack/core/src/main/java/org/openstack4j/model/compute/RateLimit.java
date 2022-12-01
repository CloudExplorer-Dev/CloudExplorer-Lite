package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;

import java.util.Date;
import java.util.List;

/**
 * Rate limits are specified in terms of both a human-readable wild-card URI and a machine-processable regular expression.
 * The human-readable limit is intended for displaying in graphical user interfaces. The machine-processable form is
 * intended to be used directly by client applications.
 *
 * @author Jeremy Unruh
 */
public interface RateLimit extends ModelEntity {

    /**
     * @return the regular expression used for this rate limit
     */
    String getRegex();

    /**
     * @return the URI associated with the rate limit
     */
    String getUri();

    List<? extends LimitEntry> getLimit();

    public interface LimitEntry {

        /**
         * @return the nextAvailable date/time
         */
        Date getNextAvailable();

        /**
         * @return the unit of time for limiting
         */
        String getUnit();

        /**
         * @return the verb (action type)
         */
        String getVerb();

        /**
         * @return the remaining limits
         */
        int getRemaining();

        /**
         * @return the available limit slots
         */
        int getAvailable();

        /**
         * @return the value for the last limit
         */
        int getValue();
    }

}
