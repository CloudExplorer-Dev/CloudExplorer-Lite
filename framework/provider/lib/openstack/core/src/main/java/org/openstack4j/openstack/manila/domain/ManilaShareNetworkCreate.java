package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.manila.ShareNetworkCreate;
import org.openstack4j.model.manila.builder.ShareNetworkCreateBuilder;

/**
 * Object used to create new share networks.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("share_network")
public class ManilaShareNetworkCreate implements ShareNetworkCreate {
    @JsonProperty("neutron_net_id")
    private String neutronNetId;
    @JsonProperty("neutron_subnet_id")
    private String neutronSubnetId;
    @JsonProperty("nova_net_id")
    private String novaNetId;
    private String name;
    private String description;

    private ManilaShareNetworkCreate() {
    }

    public static ShareNetworkCreateBuilder builder() {
        return new ShareNetworkCreateConcreteBuilder();
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

    @Override
    public ShareNetworkCreateBuilder toBuilder() {
        return new ShareNetworkCreateConcreteBuilder(this);
    }

    public static class ShareNetworkCreateConcreteBuilder implements ShareNetworkCreateBuilder {
        ManilaShareNetworkCreate shareNetworkCreate;

        ShareNetworkCreateConcreteBuilder() {
            this(new ManilaShareNetworkCreate());
        }

        ShareNetworkCreateConcreteBuilder(ManilaShareNetworkCreate shareNetworkCreate) {
            this.shareNetworkCreate = shareNetworkCreate;
        }

        @Override
        public ShareNetworkCreateBuilder neutronNet(String neutronNetId, String neutronSubnetId) {
            shareNetworkCreate.neutronNetId = neutronNetId;
            shareNetworkCreate.neutronSubnetId = neutronSubnetId;
            return this;
        }

        @Override
        public ShareNetworkCreateBuilder novaNet(String novaNetId) {
            shareNetworkCreate.novaNetId = novaNetId;
            return this;
        }

        @Override
        public ShareNetworkCreateBuilder name(String name) {
            shareNetworkCreate.name = name;
            return this;
        }

        @Override
        public ShareNetworkCreateBuilder description(String description) {
            shareNetworkCreate.description = description;
            return this;
        }

        @Override
        public ShareNetworkCreate build() {
            return shareNetworkCreate;
        }

        @Override
        public ShareNetworkCreateBuilder from(ShareNetworkCreate in) {
            shareNetworkCreate = (ManilaShareNetworkCreate) in;
            return this;
        }
    }
}
