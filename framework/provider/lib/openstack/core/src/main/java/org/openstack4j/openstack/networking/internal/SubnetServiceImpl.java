package org.openstack4j.openstack.networking.internal;

import org.openstack4j.api.networking.SubnetService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.Subnet;
import org.openstack4j.openstack.networking.domain.NeutronSubnet;
import org.openstack4j.openstack.networking.domain.NeutronSubnet.Subnets;
import org.openstack4j.openstack.networking.domain.NeutronSubnetUpdate;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * OpenStack (Neutron) Subnet based Operations implementation
 *
 * @author Jeremy Unruh
 */
public class SubnetServiceImpl extends BaseNetworkingServices implements SubnetService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Subnet> list() {
        return get(Subnets.class, uri("/subnets")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Subnet get(String subnetId) {
        checkNotNull(subnetId);
        return get(NeutronSubnet.class, uri("/subnets/%s", subnetId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String subnetId) {
        checkNotNull(subnetId);
        return deleteWithResponse(uri("/subnets/%s", subnetId)).execute();
    }

    @Override
    public Subnet create(Subnet subnet) {
        checkNotNull(subnet);
        return post(NeutronSubnet.class, uri("/subnets")).entity(subnet).execute();
    }

    public Subnet update(Subnet subnet) {
        checkNotNull(subnet);
        return update(subnet.getId(), subnet);
    }

    public Subnet update(String subnetId, Subnet subnet) {
        checkNotNull(subnetId);
        checkNotNull(subnet);
        return put(NeutronSubnet.class, uri("/subnets/%s", subnetId))
                .entity(NeutronSubnetUpdate.createFromSubnet(subnet))
                .execute();
    }
}
