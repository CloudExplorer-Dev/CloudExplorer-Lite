package org.openstack4j.openstack.murano.v1.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.murano.v1.builder.EnvironmentBuilder;
import org.openstack4j.model.murano.v1.domain.Environment;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;


public class MuranoEnvironment implements Environment {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String status;

    @JsonProperty
    private String updated;

    @JsonProperty
    private String created;

    @JsonProperty
    private String version;

    @JsonProperty("tenant_id")
    private String tenantId;

    private List<MuranoApplication> services;

    /**
     * @return the environment Builder
     */
    public static EnvironmentBuilder builder() {
        return new MuranoEnvironmentConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MuranoApplication> getServices() {
        return this.services;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatus() {
        return this.status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUpdated() {
        return this.updated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCreated() {
        return this.created;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantId() {
        return this.tenantId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", this.id)
                .add("name", this.name)
                .add("status", this.status)
                .add("created", this.created)
                .add("updated", this.updated)
                .add("tenant_id", this.tenantId)
                .add("version", this.version)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EnvironmentBuilder toBuilder() {
        return new MuranoEnvironmentConcreteBuilder(this);
    }

    public static class MuranoEnvironmentConcreteBuilder implements EnvironmentBuilder {

        private MuranoEnvironment model;

        MuranoEnvironmentConcreteBuilder() {
            this(new MuranoEnvironment());
        }

        MuranoEnvironmentConcreteBuilder(MuranoEnvironment model) {
            this.model = model;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public EnvironmentBuilder name(String name) {
            this.model.name = name;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Environment build() {
            return this.model;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public EnvironmentBuilder from(Environment in) {
            if (in != null) {
                this.model = (MuranoEnvironment) in;
            }
            return this;
        }
    }

    public static class MuranoEnvironments extends ListResult<MuranoEnvironment> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("environments")
        protected List<MuranoEnvironment> list;

        @Override
        protected List<MuranoEnvironment> value() {
            return list;
        }
    }
}
