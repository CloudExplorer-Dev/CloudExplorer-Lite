package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.core.transport.HttpMethod;
import org.openstack4j.model.manila.RateLimit;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Rate limits control the frequency at which users can issue specific API requests.
 * Administrators use rate limiting to configure limits on the type and number of API calls that can be made in a
 * specific time interval.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class ManilaRateLimit implements RateLimit {
    private static final long serialVersionUID = 1L;

    private String uri;
    private String regex;
    private List<ManilaLimit> limit;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUri() {
        return uri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRegex() {
        return regex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Limit> getLimit() {
        return limit;
    }

    public static class ManilaLimit implements Limit, Serializable {
        private static final long serialVersionUID = 1L;

        private int value;
        private HttpMethod verb;
        private int remaining;
        private TimeIntervalUnit unit;
        @JsonProperty("next-available")
        private Date nextAvailable;

        @Override
        public int getValue() {
            return value;
        }

        @Override
        public HttpMethod getVerb() {
            return verb;
        }

        @Override
        public int getRemaining() {
            return remaining;
        }

        @Override
        public TimeIntervalUnit getUnit() {
            return unit;
        }

        @Override
        public Date getNextAvailable() {
            return nextAvailable;
        }
    }
}
