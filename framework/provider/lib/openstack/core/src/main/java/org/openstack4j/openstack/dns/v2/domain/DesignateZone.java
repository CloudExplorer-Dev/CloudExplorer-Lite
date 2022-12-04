package org.openstack4j.openstack.dns.v2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.openstack4j.model.dns.v2.Action;
import org.openstack4j.model.dns.v2.Status;
import org.openstack4j.model.dns.v2.Zone;
import org.openstack4j.model.dns.v2.ZoneType;
import org.openstack4j.model.dns.v2.builder.ZoneBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * zone model class for designate/v2 zone
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DesignateZone implements Zone {

    private static final long serialVersionUID = 1L;
    private String id;
    @JsonProperty("pool_id")
    private String poolId;
    @JsonProperty("project_id")
    private String projectId;
    private String name;
    private String email;
    private Integer ttl;
    private String serial;
    private Status status;
    private Action action;
    private String description;
    private List<String> masters;
    private ZoneType type;
    @JsonProperty("tranferred_at")
    private String transferredAt;
    private Integer version;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    private Map<String, String> links;

    /**
     * @return the zone builder
     */
    public static ZoneBuilder builder() {
        return new ZoneConcreteBuilder();
    }

    @Override
    public ZoneBuilder toBuilder() {
        return new ZoneConcreteBuilder(this);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPoolId() {
        return poolId;
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Integer getTTL() {
        return ttl;
    }

    @Override
    public String getSerial() {
        return serial;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<String> getMasters() {
        return masters;
    }

    @Override
    public ZoneType getType() {
        return type;
    }

    @Override
    public String getTransferedAt() {
        return transferredAt;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public Map<String, String> getLinks() {
        return links;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("poolId", poolId)
                .add("projectId", projectId)
                .add("name", name)
                .add("email", email)
                .add("ttl", ttl)
                .add("serial", serial)
                .add("status", status)
                .add("action", action)
                .add("description", description)
                .add("masters", masters)
                .add("type", type)
                .add("transferredAt", transferredAt)
                .add("version", version)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .add("links", links)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id, poolId, projectId, name, email, ttl, serial, status, action, description, masters, type, transferredAt, version, createdAt, updatedAt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        DesignateZone that = DesignateZone.class.cast(obj);
        return Objects.equal(this.id, that.id)
                && Objects.equal(this.poolId, that.poolId)
                && Objects.equal(this.projectId, that.projectId)
                && Objects.equal(this.name, that.name)
                && Objects.equal(this.email, that.email)
                && Objects.equal(this.ttl, that.ttl)
                && Objects.equal(this.serial, that.serial)
                && Objects.equal(this.status, that.status)
                && Objects.equal(this.action, that.action)
                && Objects.equal(this.description, that.description)
                && Objects.equal(this.masters, that.masters)
                && Objects.equal(this.type, that.type)
                && Objects.equal(this.transferredAt, that.transferredAt)
                && Objects.equal(this.version, that.version)
                && Objects.equal(this.createdAt, that.createdAt)
                && Objects.equal(this.updatedAt, that.updatedAt)
                && Objects.equal(this.links, that.links);
    }

    public static class ZoneConcreteBuilder implements ZoneBuilder {

        DesignateZone model;

        ZoneConcreteBuilder() {
            this(new DesignateZone());
        }

        ZoneConcreteBuilder(DesignateZone model) {
            this.model = model;
        }

        @Override
        public Zone build() {
            return model;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ZoneBuilder from(Zone in) {
            if (in != null)
                this.model = (DesignateZone) in;
            return this;
        }

        /**
         * @see DesignateZone#getId()
         */
        @Override
        public ZoneBuilder id(String id) {
            model.id = id;
            return this;
        }

        /**
         * @see DesignateZone#getPoolId()
         */
        @Override
        public ZoneBuilder poolId(String poolId) {
            model.poolId = poolId;
            return this;
        }

        /**
         * @see DesignateZone#getProjectId()
         */
        @Override
        public ZoneBuilder projectId(String projectId) {
            model.projectId = projectId;
            return this;
        }

        /**
         * @see DesignateZone#getName()
         */
        @Override
        public ZoneBuilder name(String name) {
            model.name = name;
            return this;
        }

        /**
         * @see DesignateZone#getEmail()
         */
        @Override
        public ZoneBuilder email(String email) {
            model.email = email;
            return this;
        }

        /**
         * @see DesignateZone#getTTL()
         */
        @Override
        public ZoneBuilder ttl(Integer ttl) {
            model.ttl = ttl;
            return this;
        }

        /**
         * @see DesignateZone#getSerial()
         */
        @Override
        public ZoneBuilder serial(String serial) {
            model.serial = serial;
            return this;
        }

        /**
         * @see DesignateZone#getStatus()
         */
        @Override
        public ZoneBuilder status(Status status) {
            model.status = status;
            return this;
        }

        /**
         * @see DesignateZone#getAction()
         */
        @Override
        public ZoneBuilder action(Action action) {
            model.action = action;
            return this;
        }

        /**
         * @see DesignateZone#getDescription()()
         */
        @Override
        public ZoneBuilder description(String description) {
            model.description = description;
            return this;
        }

        /**
         * @see DesignateZone#getMasters()
         */
        @Override
        public ZoneBuilder masters(List<String> masters) {
            model.masters = masters;
            return this;
        }

        /**
         * @see DesignateZone#getType()
         */
        @Override
        public ZoneBuilder type(ZoneType type) {
            model.type = type;
            return this;
        }

        /**
         * @see DesignateZone#getTransferedAt()
         */
        @Override
        public ZoneBuilder transferredAt(String transferredAt) {
            model.transferredAt = transferredAt;
            return this;
        }

        /**
         * @see DesignateZone#getVersion()
         */
        @Override
        public ZoneBuilder version(Integer version) {
            model.version = version;
            return this;
        }

        /**
         * @see DesignateZone#getCreatedAt()
         */
        @Override
        public ZoneBuilder createdAt(String createdAt) {
            model.createdAt = createdAt;
            return this;
        }

        /**
         * @see DesignateZone#getUpdatedAt()
         */
        @Override
        public ZoneBuilder updatedAt(String updatedAt) {
            model.updatedAt = updatedAt;
            return this;
        }

        /**
         * @see DesignateZone#getLinks()
         */
        @Override
        public ZoneBuilder links(Map<String, String> links) {
            model.links = links;
            return this;
        }
    }

    public static class Zones extends ListResult<DesignateZone> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("zones")
        protected List<DesignateZone> list;

        @Override
        public List<DesignateZone> value() {
            return list;
        }
    }

}
