package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.HealthMonitorUpdate;
import org.openstack4j.model.network.ext.builder.HealthMonitorUpdateBuilder;

/**
 * An entity used to update a healthmonitor
 *
 * @author liujunpeng
 */
@JsonRootName("health_monitor")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronHealthMonitorUpdate implements HealthMonitorUpdate {

    private static final long serialVersionUID = 1L;

    /**
     * The type of probe sent by the load balancer to verify the member state,
     * which is PING, TCP, HTTP, or HTTPS.
     */

    private Integer delay;

    private Integer timeout;
    /**
     * Number of allowed connection failures before changing the status of the
     * member to INACTIVE. A valid value is from 1 to 10.
     */
    @JsonProperty("max_retries")
    private Integer maxRetries;

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
     * The administrative state of the health monitor, which is up (true) or
     * down (false)
     */
    @JsonProperty("admin_state_up")
    private boolean adminStateUp;

    public static HealthMonitorUpdateBuilder builder() {
        return new HealthMonitorUpdateConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HealthMonitorUpdateBuilder toBuilder() {
        return new HealthMonitorUpdateConcreteBuilder(this);
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("delay", delay)
                .add("urlPath", urlPath)
                .add("adminStateUp", adminStateUp)
                .add("expectedCodes", expectedCodes)
                .add("httpMethod", httpMethod)
                .add("maxRetries", maxRetries)
                .add("timeout", timeout)
                .toString();
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

    public static class HealthMonitorUpdateConcreteBuilder implements
            HealthMonitorUpdateBuilder {

        NeutronHealthMonitorUpdate m;

        public HealthMonitorUpdateConcreteBuilder() {
            this(new NeutronHealthMonitorUpdate());
        }

        public HealthMonitorUpdateConcreteBuilder(NeutronHealthMonitorUpdate m) {
            this.m = m;
        }

        @Override
        public HealthMonitorUpdate build() {
            return m;
        }

        @Override
        public HealthMonitorUpdateBuilder from(HealthMonitorUpdate in) {
            m = (NeutronHealthMonitorUpdate) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorUpdateBuilder delay(Integer delay) {
            m.delay = delay;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorUpdateBuilder urlPath(String urlPath) {
            m.urlPath = urlPath;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorUpdateBuilder expectedCodes(String expectedCodes) {
            m.expectedCodes = expectedCodes;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorUpdateBuilder httpMethod(String httpMethod) {
            m.httpMethod = httpMethod;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorUpdateBuilder maxRetries(Integer maxRetries) {
            m.maxRetries = maxRetries;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorUpdateBuilder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorUpdateBuilder timeout(Integer timeout) {
            m.timeout = timeout;
            return this;
        }

    }
}
