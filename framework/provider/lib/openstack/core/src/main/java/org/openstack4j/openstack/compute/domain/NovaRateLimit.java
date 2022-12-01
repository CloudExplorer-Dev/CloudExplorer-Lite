package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.RateLimit;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Rate limits are specified in terms of both a human-readable wild-card URI and a machine-processable regular expression.
 * The human-readable limit is intended for displaying in graphical user interfaces. The machine-processable form is
 * intended to be used directly by client applications.
 *
 * @author Jeremy Unruh
 */
public class NovaRateLimit implements RateLimit {

    private static final long serialVersionUID = 1L;

    private String regex;
    private String uri;
    private List<NovaLimitEntry> limit;

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
    public String getUri() {
        return uri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("limit", limit).add("regex", regex).add("uri", uri)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends LimitEntry> getLimit() {
        return limit;
    }

    public static class NovaLimitEntry implements LimitEntry, Serializable {

        private static final long serialVersionUID = 1L;

        @JsonProperty("next-available")
        private Date nextAvailable;
        private String unit;
        private String verb;
        private int remaining;
        private int available;
        private int value;

        /**
         * {@inheritDoc}
         */
        @Override
        public Date getNextAvailable() {
            return nextAvailable;
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
        public String getVerb() {
            return verb;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getRemaining() {
            return remaining;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getAvailable() {
            return available;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues()
                    .add("next-available", nextAvailable).add("remaining", remaining).add("unit", unit)
                    .add("value", value).add("available", available).add("verb", verb)
                    .toString();
        }

    }

}
