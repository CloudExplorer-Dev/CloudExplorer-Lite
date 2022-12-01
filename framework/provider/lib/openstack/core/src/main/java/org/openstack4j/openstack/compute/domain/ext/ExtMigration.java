package org.openstack4j.openstack.compute.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.ext.Migration;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

/**
 * A model class implementation which enables an administrative user to fetch in-progress migrations for a region or specified cell in a region
 *
 * @author Jeremy Unruh
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtMigration implements Migration {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    String id;
    @JsonProperty("instance_uuid")
    String instanceUUID;
    @JsonProperty("status")
    Status status;
    @JsonProperty("created_at")
    Date createdAt;
    @JsonProperty("updated_at")
    Date updatedAt;
    @JsonProperty("dest_compute")
    String destCompute;
    @JsonProperty("dest_host")
    String destHost;
    @JsonProperty("dest_node")
    String destNode;
    @JsonProperty("source_compute")
    String sourceCompute;
    @JsonProperty("source_node")
    String sourceNode;
    @JsonProperty("new_instance_type_id")
    String newInstanceTypeId;
    @JsonProperty("old_instance_type_id")
    String oldInstanceTypeId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getInstanceUuid() {
        return instanceUUID;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String getDestCompute() {
        return destCompute;
    }

    @Override
    public String getDestHost() {
        return destHost;
    }

    @Override
    public String getDestNode() {
        return destNode;
    }

    @Override
    public String getSourceCompute() {
        return sourceCompute;
    }

    @Override
    public String getSourceNode() {
        return sourceNode;
    }

    @Override
    public String getNewInstanceTypeId() {
        return newInstanceTypeId;
    }

    @Override
    public String getOldInstanceTypeId() {
        return oldInstanceTypeId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("instanceUUID", instanceUUID).add("status", status).add("createdAt", createdAt)
                .add("updatedAt", updatedAt).add("destCompute", destCompute).add("destHost", destHost)
                .add("destNode", destNode).add("sourceCompute", sourceCompute).add("sourceNode", sourceNode)
                .add("newInstanceTypeId at", newInstanceTypeId).add("oldInstanceTypeId", oldInstanceTypeId)
                .toString();
    }

    public static class Migrations extends ListResult<ExtMigration> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("migrations")
        private List<ExtMigration> migrations;

        public List<ExtMigration> value() {
            return migrations;
        }
    }

}
