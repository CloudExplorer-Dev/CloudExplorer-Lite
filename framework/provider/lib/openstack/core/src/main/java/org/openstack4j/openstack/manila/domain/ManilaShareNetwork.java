package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.manila.ShareNetwork;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A share network stores network information that share servers can use where shares are hosted.
 * A share network has these attributes:
 * <ul>
 * <li>The IP block in Classless Inter-Domain Routing (CIDR) notation from which to allocate the network.</li>
 * <li>The IP version of the network.</li>
 * <li>The network type, which is {@code vlan}, {@code vxlan}, {@code gre}, or {@code flat}.</li>
 * <li>If the network uses segmentation, a segmentation identifier. For example, VLAN, VXLAN,
 * and GRE networks use segmentation.</li>
 * </ul>
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("share_network")
public class ManilaShareNetwork implements ShareNetwork {
    private static final long serialVersionUID = 1L;

    private String id;
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("neutron_net_id")
    private String neutronNetId;
    @JsonProperty("neutron_subnet_id")
    private String neutronSubnetId;
    @JsonProperty("nova_net_id")
    private String novaNetId;
    @JsonProperty("network_type")
    private NetworkType networkType;
    @JsonProperty("segmentation_id")
    private Integer segmentationId;
    private String cidr;
    @JsonProperty("ip_version")
    private Integer ipVersion;
    private String name;
    private String description;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;

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
    public String getProjectId() {
        return projectId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNeutronNetId() {
        return neutronNetId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNeutronSubnetId() {
        return neutronSubnetId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNovaNetId() {
        return novaNetId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkType getNetworkType() {
        return networkType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getSegmentationId() {
        return segmentationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCidr() {
        return cidr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getIpVersion() {
        return ipVersion;
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
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUpdatedAt() {
        return updatedAt;
    }

    public static class ShareNetworks extends ListResult<ManilaShareNetwork> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("share_networks")
        private List<ManilaShareNetwork> shareNetworks;

        @Override
        protected List<ManilaShareNetwork> value() {
            return shareNetworks;
        }
    }
}
