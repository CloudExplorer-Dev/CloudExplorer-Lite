package org.openstack4j.model.manila;

import org.openstack4j.core.transport.HttpMethod;

import java.util.Date;
import java.util.List;

/**
 * Rate limits control the frequency at which users can issue specific API requests.
 * Administrators use rate limiting to configure limits on the type and number of API calls that can be made in a
 * specific time interval.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface RateLimit {
    /**
     * @return a human-readable URI of a rate limit
     */
    String getUri();

    /**
     * @return an API regular expression
     */
    String getRegex();

    /**
     * @return the limit object of this rate limit
     */
    List<? extends Limit> getLimit();

    enum TimeIntervalUnit {
        SECOND, MINUTE, HOUR, DAY;
    }

    interface Limit {
        /**
         * @return the number of API requests that are allowed during a time interval
         */
        int getValue();

        /**
         * @return the HTTP method for the API request
         */
        HttpMethod getVerb();

        /**
         * @return the remaining number of allowed requests
         */
        int getRemaining();

        /**
         * @return the time interval during which a number of API requests are allowed
         */
        TimeIntervalUnit getUnit();

        /**
         * @return the date and time stamp when next issues are available
         */
        Date getNextAvailable();
    }
}
