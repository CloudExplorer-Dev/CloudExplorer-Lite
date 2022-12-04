package org.openstack4j.openstack.networking.internal;

import org.openstack4j.api.networking.TrunkService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.Trunk;
import org.openstack4j.model.network.TrunkSubport;
import org.openstack4j.openstack.networking.domain.AbstractNeutronTrunk.Trunks;
import org.openstack4j.openstack.networking.domain.*;
import org.openstack4j.openstack.networking.domain.NeutronTrunkSubport.TrunkSubports;
import org.openstack4j.openstack.networking.domain.NeutronTrunkSubportCreate.NeutronTrunkSubportDelete.NeutronTrunkSubportsDelete;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * OpenStack Network Trunk operations implementation
 *
 * @author Kashyap Jha
 */
public class TrunkServiceImpl extends BaseNetworkingServices implements TrunkService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Trunk> list() {
        return get(Trunks.class, uri("/trunks")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Trunk create(Trunk trunk) {
        checkNotNull(trunk);
        return post(NeutronTrunk.class, uri("/trunks")).entity(NeutronTrunkCreate.fromTrunk(trunk)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Trunk get(String trunkId) {
        checkNotNull(trunkId);
        return get(NeutronTrunk.class, uri("/trunks/%s", trunkId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String trunkId) {
        checkNotNull(trunkId);
        return deleteWithResponse(uri("/trunks/%s", trunkId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Trunk update(Trunk trunk) {
        checkNotNull(trunk);
        checkNotNull(trunk.getId());
        return put(NeutronTrunk.class, uri("/trunks/%s", trunk.getId())).entity(NeutronTrunkUpdate.update(trunk)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Trunk addTrunkSubport(String trunkId, TrunkSubport subPort) {
        checkNotNull(subPort);
        checkNotNull(trunkId);
        List<TrunkSubport> al = new ArrayList<>();
        al.add(subPort);
        return put(NeutronTrunkSubportAddRemove.class, uri("/trunks/%s/add_subports", trunkId))
                .entity(NeutronTrunkSubportCreate.NeutronTrunkSubportsCreate.fromTrunkSubports(al)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Trunk removeTrunkSubport(String trunkId, String portId) {
        checkNotNull(trunkId);
        checkNotNull(portId);
        List<String> al = new ArrayList<>();
        al.add(portId);
        return put(NeutronTrunkSubportAddRemove.class, uri("/trunks/%s/remove_subports", trunkId))
                .entity(NeutronTrunkSubportsDelete.delete(al)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NeutronTrunkSubport> listTrunkSubports(String trunkId) {
        checkNotNull(trunkId);
        return get(TrunkSubports.class, uri("/trunks/%s/get_subports", trunkId)).execute().getList();
    }
}
