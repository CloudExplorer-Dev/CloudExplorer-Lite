package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.HostAggregate;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Nova HostAggregate
 *
 * @author liujunpeng
 */
@JsonRootName("aggregate")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NovaHostAggregate implements HostAggregate {

    private static final long serialVersionUID = 1L;
    @JsonProperty("availability_zone")
    public String availabilityZone;
    @JsonProperty("created_at")
    public Date createdAt;
    public boolean deleted;
    @JsonProperty("deleted_at")
    public Date deletedAt;
    public List<String> hosts;
    public String id;
    public Map<String, String> metadata;
    public String name;
    @JsonProperty("updated_at")
    public Date updatedAt;

    /**
     * Used internally by the domain side of the API to create a new HostAggregate on an OpenStack server
     *
     * @return NovaHostAggregate
     */
    public static NovaHostAggregate create(String name, String availabilityZone) {
        NovaHostAggregate hostAggregate = new NovaHostAggregate();
        hostAggregate.name = name;
        hostAggregate.availabilityZone = availabilityZone;
        return hostAggregate;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAvailabilityZone() {
        return availabilityZone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCreate() {
        return createdAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDeletedAt() {
        return deletedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getHosts() {
        return hosts;
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
    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {

        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("availabilityZone", availabilityZone)
                .add("createdAt", createdAt)
                .add("deleted", deleted)
                .add("deletedAt", deletedAt)
                .add("hosts", hosts)
                .add("id", id)
                .add("metadata", metadata)
                .add("name", name)
                .add("updatedAt", updatedAt).toString();
    }

    /**
     * host aggregates
     *
     * @author liujunpeng
     */
    public static class NovaHostAggregates extends ListResult<NovaHostAggregate> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("aggregates")
        private List<NovaHostAggregate> aggregates;

        /**
         * NovaHostAggregates
         */
        public List<NovaHostAggregate> value() {
            return aggregates;
        }

        @Override
        public String toString() {
            return "HostAggregates [aggregates=" + aggregates + "]";
        }

    }

}
