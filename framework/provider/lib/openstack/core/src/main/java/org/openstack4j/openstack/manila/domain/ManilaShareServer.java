package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.manila.ShareServer;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * A share server is created by multi-tenant back-end drivers where shares are hosted.
 * For example, with the <code>generic</code> driver, shares are hosted on Compute VMs.
 * With the <code>cluster_mode</code> driver from NetApp, shares are hosted on virtual storage servers,
 * also known as Vservers or SVMs.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("share_server")
public class ManilaShareServer implements ShareServer {
    private static final long serialVersionUID = 1L;

    private String id;
    @JsonProperty("project_id")
    private String projectId;
    private Status status;
    @JsonProperty("backend_details")
    private Map<String, String> backendDetails;
    @JsonProperty("share_network_id")
    private String shareNetworkId;
    @JsonProperty("share_network_name")
    private String shareNetworkName;
    private String host;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Map<String, String> getBackendDetails() {
        return backendDetails;
    }

    @Override
    public String getShareNetworkId() {
        return shareNetworkId;
    }

    @Override
    public String getShareNetworkName() {
        return shareNetworkName;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getUpdatedAt() {
        return updatedAt;
    }

    public static class ShareServers extends ListResult<ManilaShareServer> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("share_servers")
        private List<ManilaShareServer> shareServers;

        @Override
        protected List<ManilaShareServer> value() {
            return shareServers;
        }
    }
}
