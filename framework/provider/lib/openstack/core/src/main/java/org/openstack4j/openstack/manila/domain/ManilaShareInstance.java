package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.manila.ShareInstance;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Representation of a share instance.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("share_instance")
public class ManilaShareInstance implements ShareInstance {
    private static final long serialVersionUID = 1L;

    private Status status;
    @JsonProperty("share_id")
    private String shareId;
    @JsonProperty("availability_zone")
    private String availabilityZone;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("export_location")
    private String exportLocation;
    @JsonProperty("export_locations")
    private List<String> exportLocations;
    @JsonProperty("share_network_id")
    private String shareNetworkId;
    @JsonProperty("share_server_id")
    private String shareServerId;
    private String host;
    private String id;

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getShareId() {
        return shareId;
    }

    @Override
    public String getAvailabilityZone() {
        return availabilityZone;
    }

    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getExportLocation() {
        return exportLocation;
    }

    @Override
    public List<String> getExportLocations() {
        return exportLocations;
    }

    @Override
    public String getShareNetworkId() {
        return shareNetworkId;
    }

    @Override
    public String getShareServerId() {
        return shareServerId;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getId() {
        return id;
    }

    public static class ShareInstances extends ListResult<ManilaShareInstance> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("share_instances")
        private List<ManilaShareInstance> shareInstances;

        @Override
        protected List<ManilaShareInstance> value() {
            return shareInstances;
        }
    }
}
