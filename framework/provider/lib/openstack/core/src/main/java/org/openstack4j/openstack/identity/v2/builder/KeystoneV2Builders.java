package org.openstack4j.openstack.identity.v2.builder;


import org.openstack4j.model.identity.v2.builder.*;
import org.openstack4j.openstack.identity.v2.domain.*;


public class KeystoneV2Builders implements IdentityV2Builders {

    private IdentityV2Builders KeystoneV2Builders() {
        return this;
    }

    @Override
    public UserBuilder user() {
        return KeystoneUser.builder();
    }

    @Override
    public EndpointBuilder endpoint() {
        return KeystoneEndpoint.builder();
    }

    @Override
    public RoleBuilder role() {
        return KeystoneRole.builder();
    }

    @Override
    public ServiceBuilder service() {
        return KeystoneService.builder();
    }

    @Override
    public ServiceEndpointBuilder serviceEndpoint() {
        return KeystoneServiceEndpoint.builder();
    }

    @Override
    public TenantBuilder tenant() {
        return KeystoneTenant.builder();
    }
}
