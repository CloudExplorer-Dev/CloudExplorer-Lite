package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.NetworkUpdate;
import org.openstack4j.model.network.builder.NetworkUpdateBuilder;

/**
 * An entity used to update a network
 *
 * @author Jeremy Unruh
 */
@JsonRootName("network")
public class NeutronNetworkUpdate implements NetworkUpdate {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String name;
    @JsonProperty("shared")
    private Boolean shared;
    @JsonProperty("admin_state_up")
    private Boolean adminStateUp;

    public static NetworkUpdateBuilder builder() {
        return new NetworkUpdateConcreteBuilder();
    }

    @Override
    public NetworkUpdateBuilder toBuilder() {
        return new NetworkUpdateConcreteBuilder(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @JsonIgnore
    @Override
    public boolean isAdminStateUp() {
        return adminStateUp == null ? false : adminStateUp;
    }

    @JsonIgnore
    @Override
    public boolean isShared() {
        return shared == null ? false : shared;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name).add("adminStateUp", adminStateUp).add("shared", shared)
                .toString();
    }

    public static class NetworkUpdateConcreteBuilder implements NetworkUpdateBuilder {

        private NeutronNetworkUpdate model;

        public NetworkUpdateConcreteBuilder() {
            this.model = new NeutronNetworkUpdate();
        }

        public NetworkUpdateConcreteBuilder(NeutronNetworkUpdate model) {
            this.model = model;
        }

        @Override
        public NetworkUpdate build() {
            return model;
        }

        @Override
        public NetworkUpdateBuilder from(NetworkUpdate in) {
            model = (NeutronNetworkUpdate) in;
            return this;
        }

        @Override
        public NetworkUpdateBuilder name(String name) {
            model.name = name;
            return this;
        }

        @Override
        public NetworkUpdateBuilder adminStateUp(boolean enabled) {
            model.adminStateUp = enabled;
            return this;
        }

        @Override
        public NetworkUpdateBuilder shared(boolean shared) {
            model.shared = shared;
            return this;
        }

    }
}
