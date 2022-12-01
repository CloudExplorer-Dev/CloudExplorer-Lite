package org.openstack4j.openstack.magnum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.magnum.Mservice;
import org.openstack4j.model.magnum.MserviceBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

@JsonRootName("mservice")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnumMservice implements Mservice {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @JsonProperty
    private String id;
    private String binary;
    @JsonProperty("created_at")
    private String createdAt;
    private String state;
    @JsonProperty("report_count")
    private int reportCount;
    @JsonProperty("updated_at")
    private String updatedAt;
    private String host;
    @JsonProperty("disabled_reason")
    private String disabledReason;


    /**
     * Magnum service builder
     *
     * @return the Magnum Service builder
     */
    public static MserviceBuilder builder() {
        return new MserviceConcreteBuilder();
    }

    @Override
    public MserviceBuilder toBuilder() {
        return new MserviceConcreteBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Set the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinary() {
        return binary;
    }

    /**
     * Set the binary
     */
    public void setBinary(String binary) {
        this.binary = binary;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Set the creation date
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getState() {
        return state;
    }

    /**
     * Set the state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getReportCount() {
        return reportCount;
    }

    /**
     * Set the report count
     */
    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Set the updated time
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHost() {
        return host;
    }

    /**
     * Set the hostname
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisabledReason() {
        return disabledReason;
    }


    /**
     * Set the disabled reason
     */
    public void setDisabledReason(String disabledReason) {
        this.disabledReason = disabledReason;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("binary", binary)
                .add("createdAt", createdAt)
                .add("state", state)
                .add("reportCount", reportCount)
                .add("updatedAt", updatedAt)
                .add("host", host)
                .add("disabledReason", disabledReason)
                .toString();
    }

    /**
     * Concrete builder	containing MagnumMservice as model
     */
    public static class MserviceConcreteBuilder implements MserviceBuilder {
        MagnumMservice model;

        public MserviceConcreteBuilder() {
            this(new MagnumMservice());
        }

        MserviceConcreteBuilder(MagnumMservice model) {
            this.model = model;
        }

        @Override
        public Mservice build() {
            return model;
        }

        @Override
        public MserviceBuilder from(Mservice in) {
            if (in != null)
                this.model = (MagnumMservice) in;
            return this;
        }

        @Override
        public MserviceBuilder id(String id) {
            model.id = id;
            return this;
        }

        @Override
        public MserviceBuilder binary(String binary) {
            model.binary = binary;
            return this;
        }

        @Override
        public MserviceBuilder createdAt(String createdAt) {
            model.createdAt = createdAt;
            return this;
        }

        @Override
        public MserviceBuilder state(String state) {
            model.state = state;
            return this;
        }

        @Override
        public MserviceBuilder reportCount(int reportCount) {
            model.reportCount = reportCount;
            return this;
        }

        @Override
        public MserviceBuilder updatedAt(String updatedAt) {
            model.updatedAt = updatedAt;
            return this;
        }

        @Override
        public MserviceBuilder host(String host) {
            model.host = host;
            return this;
        }

        @Override
        public MserviceBuilder disabledReason(String disabledReason) {
            model.disabledReason = disabledReason;
            return this;
        }

    }

    /**
     * List of Magnum Services
     */
    public static class Mservices extends ListResult<MagnumMservice> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("mservices")
        protected List<MagnumMservice> list;

        @Override
        protected List<MagnumMservice> value() {
            return list;
        }

    }
}
