package org.openstack4j.openstack.dns.v2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.openstack4j.model.dns.v2.Action;
import org.openstack4j.model.dns.v2.Recordset;
import org.openstack4j.model.dns.v2.Status;
import org.openstack4j.model.dns.v2.builder.RecordsetBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * zone model class for designate/v2 zone
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DesignateRecordset implements Recordset {

    private static final long serialVersionUID = 1L;
    private String id;
    @JsonProperty("project_id")
    private String projectId;
    private String name;
    private String ttl;
    private Status status;
    private Action action;
    @JsonProperty("zone_id")
    private String zoneId;
    @JsonProperty("zone_name")
    private String zoneName;
    private String description;
    private String type;
    private Integer version;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    private Map<String, String> links;
    private List<String> records;

    /**
     * @return the zone builder
     */
    public static RecordsetBuilder builder() {
        return new RecordsetConcreteBuilder();
    }

    @Override
    public RecordsetBuilder toBuilder() {
        return new RecordsetConcreteBuilder(this);
    }

    @Override
    public String getId() {
        return id;
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
    public String getTTL() {
        return ttl;
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
    public String getZoneId() {
        return zoneId;
    }

    @Override
    public String getZoneName() {
        return zoneName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getType() {
        return type;
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

    @Override
    public List<String> getRecords() {
        return records;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("projectId", projectId)
                .add("name", name)
                .add("ttl", ttl)
                .add("status", status)
                .add("action", action)
                .add("zoneId", zoneId)
                .add("zoneName", zoneName)
                .add("description", description)
                .add("type", type)
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
        return Objects.hashCode(id, projectId, name, ttl, status, action, zoneId, zoneName, description, type, version, createdAt, updatedAt, links);
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
        DesignateRecordset that = DesignateRecordset.class.cast(obj);
        return Objects.equal(this.id, that.id)
                && Objects.equal(this.projectId, that.projectId)
                && Objects.equal(this.name, that.name)
                && Objects.equal(this.ttl, that.ttl)
                && Objects.equal(this.status, that.status)
                && Objects.equal(this.action, that.action)
                && Objects.equal(this.zoneId, that.zoneId)
                && Objects.equal(this.zoneName, that.zoneName)
                && Objects.equal(this.description, that.description)
                && Objects.equal(this.type, that.type)
                && Objects.equal(this.version, that.version)
                && Objects.equal(this.createdAt, that.createdAt)
                && Objects.equal(this.updatedAt, that.updatedAt)
                && Objects.equal(this.links, that.links);
    }

    public static class RecordsetConcreteBuilder implements RecordsetBuilder {

        DesignateRecordset model;

        RecordsetConcreteBuilder() {
            this(new DesignateRecordset());
        }

        RecordsetConcreteBuilder(DesignateRecordset model) {
            this.model = model;
        }

        @Override
        public Recordset build() {
            return model;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public RecordsetBuilder from(Recordset in) {
            if (in != null)
                this.model = (DesignateRecordset) in;
            return this;
        }

        @Override
        public RecordsetBuilder id(String id) {
            model.id = id;
            return this;
        }

        @Override
        public RecordsetBuilder projectId(String projectId) {
            model.projectId = projectId;
            return this;
        }

        @Override
        public RecordsetBuilder name(String name) {
            model.name = name;
            return this;
        }

        @Override
        public RecordsetBuilder ttl(String ttl) {
            model.ttl = ttl;
            return this;
        }

        @Override
        public RecordsetBuilder status(Status status) {
            model.status = status;
            return this;
        }

        @Override
        public RecordsetBuilder action(Action action) {
            model.action = action;
            return this;
        }

        @Override
        public RecordsetBuilder zoneId(String zoneId) {
            model.zoneId = zoneId;
            return this;
        }

        @Override
        public RecordsetBuilder zoneName(String zoneName) {
            model.zoneName = zoneName;
            return this;
        }

        @Override
        public RecordsetBuilder description(String description) {
            model.description = description;
            return this;
        }

        @Override
        public RecordsetBuilder type(String type) {
            model.type = type;
            return this;
        }

        @Override
        public RecordsetBuilder version(Integer version) {
            model.version = version;
            return this;
        }

        @Override
        public RecordsetBuilder createdAt(String createdAt) {
            model.createdAt = createdAt;
            return this;
        }

        @Override
        public RecordsetBuilder updatedAt(String updatedAt) {
            model.updatedAt = updatedAt;
            return this;
        }

        /**
         * @see DesignateRecordset#getLinks()
         */
        @Override
        public RecordsetBuilder links(Map<String, String> links) {
            model.links = links;
            return this;
        }

        /**
         * @see DesignateRecordset#getRecords()
         */
        @Override
        public RecordsetBuilder records(List<String> records) {
            model.records = records;
            return this;
        }

    }

    public static class Recordsets extends ListResult<DesignateRecordset> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("recordsets")
        protected List<DesignateRecordset> list;

        @Override
        public List<DesignateRecordset> value() {
            return list;
        }
    }
}
