package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.HealthMonitor;
import org.openstack4j.model.network.ext.HealthMonitorType;
import org.openstack4j.model.network.ext.builder.HealthMonitorBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A health monitor entity
 *
 * @author liujunpeng
 */
@JsonRootName("health_monitor")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronHealthMonitor implements HealthMonitor {

    private static final long serialVersionUID = 1L;

    private String id;
    @JsonProperty("tenant_id")
    private String tenantId;

    private HealthMonitorType type;
    private Integer delay;
    private Integer timeout;
    /**
     * Number of allowed connection failures before changing the status of the member to INACTIVE. A valid value is from 1 to 10.
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
     * The administrative state of the health monitor, which is up (true) or down (false)
     */
    @JsonProperty("admin_state_up")
    private boolean adminStateUp;

    /**
     * The status of the monitor. Indicates whether the VIP is operational.
     */
    private String status;

    /**
     * @return HealthMonitorBuilder
     */
    public static HealthMonitorBuilder builder() {
        return new HealthMonitorConcretebuilder();
    }

    /**
     * wrap this healthMonitor to a builder
     *
     * @return HealthMonitorBuilder
     */
    @Override
    public HealthMonitorBuilder toBuilder() {
        return new HealthMonitorConcretebuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantId() {
        return tenantId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HealthMonitorType getType() {
        return type;
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
    public String getUrlPath() {
        return urlPath;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("adminStateUp", adminStateUp)
                .add("delay", delay)
                .add("expectedCodes", expectedCodes)
                .add("httpMethod", httpMethod)
                .add("maxRetries", maxRetries)
                .add("status", status)
                .add("tenantId", tenantId)
                .add("timeout", timeout)
                .add("type", type)
                .add("urlPath", urlPath)
                .toString();
    }

    public static class HealthMonitors extends ListResult<NeutronHealthMonitor> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("health_monitors")
        List<NeutronHealthMonitor> healthMonitors;

        @Override
        public List<NeutronHealthMonitor> value() {
            return healthMonitors;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).omitNullValues()
                    .add("healthMonitors", healthMonitors).toString();
        }
    }

    public static class HealthMonitorConcretebuilder implements HealthMonitorBuilder {
        NeutronHealthMonitor m;

        public HealthMonitorConcretebuilder() {
            this(new NeutronHealthMonitor());
        }

        public HealthMonitorConcretebuilder(NeutronHealthMonitor m) {
            this.m = m;
        }

        @Override
        public HealthMonitor build() {
            return m;
        }

        @Override
        public HealthMonitorBuilder from(HealthMonitor in) {
            this.m = (NeutronHealthMonitor) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorBuilder tenantId(String tenantId) {
            m.tenantId = tenantId;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorBuilder type(HealthMonitorType type) {
            m.type = type;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorBuilder delay(Integer delay) {
            m.delay = delay;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorBuilder timeout(Integer timeout) {
            m.timeout = timeout;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorBuilder maxRetries(Integer maxRetries) {
            m.maxRetries = maxRetries;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorBuilder urlPath(String urlPath) {
            m.urlPath = urlPath;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorBuilder expectedCodes(String expectedCodes) {
            m.expectedCodes = expectedCodes;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorBuilder httpMethod(String httpMethod) {
            m.httpMethod = httpMethod;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public HealthMonitorBuilder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }
    }

}
