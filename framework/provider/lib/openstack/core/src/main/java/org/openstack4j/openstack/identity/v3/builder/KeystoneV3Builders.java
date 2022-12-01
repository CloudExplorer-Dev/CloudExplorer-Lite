package org.openstack4j.openstack.identity.v3.builder;

import org.openstack4j.model.identity.v3.builder.*;
import org.openstack4j.openstack.identity.v3.domain.*;

/**
 * The Identity V3 Builders
 */
public class KeystoneV3Builders implements IdentityV3Builders {

    private IdentityV3Builders KeystoneV3Builders() {
        return this;
    }

    @Override
    public CredentialBuilder credential() {
        return KeystoneCredential.builder();
    }

    @Override
    public DomainBuilder domain() {
        return KeystoneDomain.builder();
    }

    @Override
    public EndpointBuilder endpoint() {
        return KeystoneEndpoint.builder();
    }

    @Override
    public GroupBuilder group() {
        return KeystoneGroup.builder();
    }

    @Override
    public PolicyBuilder policy() {
        return KeystonePolicy.builder();
    }

    @Override
    public ProjectBuilder project() {
        return KeystoneProject.builder();
    }

    @Override
    public RegionBuilder region() {
        return KeystoneRegion.builder();
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
    public UserBuilder user() {
        return KeystoneUser.builder();
    }
}
