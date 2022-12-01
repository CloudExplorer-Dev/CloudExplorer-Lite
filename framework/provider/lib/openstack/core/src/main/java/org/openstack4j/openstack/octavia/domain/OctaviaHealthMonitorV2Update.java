package org.openstack4j.openstack.octavia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.octavia.HealthMonitorV2Update;
import org.openstack4j.model.octavia.builder.HealthMonitorV2UpdateBuilder;

/**
 * Entity used to update lbaas v2 heathmonitor
 *
 * @author wei
 */
@JsonRootName("healthmonitor")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OctaviaHealthMonitorV2Update implements HealthMonitorV2Update {

    private static final long serialVersionUID = 1L;

    private Integer delay;

    private Integer timeout;

    /**
     * Number of allowed connection failures before changing the status of the member to INACTIVE. A valid value is from 1 to 10.
     */
    @JsonProperty("max_retries")
    private Integer maxRetries;

    /**
     * Number of allowed connection failures before changing the status of the member to INACTIVE. A valid value is from 1 to 10.
     */
    @JsonProperty("max_retries_down")
    private Integer maxRetriesDown;

    /**
     * The HTTP method that the monitor uses for requests.
     */
    @JsonProperty("http_method")
    private String httpMethod;

    /**
     * URL
     */
    @JsonProperty("url_path")
    private String urlPath;

    /**
     * default 200
     */
    @JsonProperty("expected_codes")
    private String expectedCodes;

    /**
     * The administrative state of the health monitor, which is up (true) or down (false)
     */
    @JsonProperty("admin_state_up")
    private boolean adminStateUp = true;

    public static HealthMonitorV2UpdateBuilder builder() {
        return new HealthMonitorV2UpdateConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getDelay() {
        return delay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlPath() {
        return urlPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getMaxRetries() {
        return maxRetries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getMaxRetriesDown() {
        return maxRetriesDown;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHttpMethod() {
        return httpMethod;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExpectedCodes() {
        return expectedCodes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAdminStateUp() {
        return adminStateUp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("delay", delay)
                .add("urlPath", urlPath)
                .add("adminStateUp", adminStateUp)
                .add("expectedCodes", expectedCodes)
                .add("httpMethod", httpMethod)
                .add("maxRetries", maxRetries)
                .add("maxRetriesDown", maxRetriesDown)
                .add("timeout", timeout)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HealthMonitorV2UpdateBuilder toBuilder() {
        return new HealthMonitorV2UpdateConcreteBuilder(this);
    }

    public static class HealthMonitorV2UpdateConcreteBuilder implements HealthMonitorV2UpdateBuilder {
        OctaviaHealthMonitorV2Update m;

        public HealthMonitorV2UpdateConcreteBuilder() {
            this(new OctaviaHealthMonitorV2Update());
        }

        public HealthMonitorV2UpdateConcreteBuilder(OctaviaHealthMonitorV2Update m) {
            this.m = m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorV2Update build() {
            return m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorV2UpdateBuilder from(HealthMonitorV2Update in) {
            m = (OctaviaHealthMonitorV2Update) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorV2UpdateBuilder delay(Integer delay) {
            m.delay = delay;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorV2UpdateBuilder urlPath(String urlPath) {
            m.urlPath = urlPath;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorV2UpdateBuilder expectedCodes(String expectedCodes) {
            m.expectedCodes = expectedCodes;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorV2UpdateBuilder httpMethod(String httpMethod) {
            m.httpMethod = httpMethod;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorV2UpdateBuilder maxRetries(Integer maxRetries) {
            m.maxRetries = maxRetries;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorV2UpdateBuilder maxRetriesDown(Integer maxRetriesDown) {
            m.maxRetriesDown = maxRetriesDown;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorV2UpdateBuilder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorV2UpdateBuilder timeout(Integer timeout) {
            m.timeout = timeout;
            return this;
        }
    }
}
