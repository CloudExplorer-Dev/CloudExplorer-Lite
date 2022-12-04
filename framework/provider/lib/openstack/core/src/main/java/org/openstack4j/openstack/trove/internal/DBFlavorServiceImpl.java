package org.openstack4j.openstack.trove.internal;

import org.openstack4j.api.trove.InstanceFlavorService;
import org.openstack4j.model.trove.Flavor;
import org.openstack4j.openstack.trove.domain.TroveInstanceFlavor;
import org.openstack4j.openstack.trove.domain.TroveInstanceFlavor.Flavors;

import java.util.List;

/**
 * Flavor API Implementation
 *
 * @author sumit gandhi
 */
public class DBFlavorServiceImpl extends BaseTroveServices implements InstanceFlavorService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Flavor> list() {
        return get(Flavors.class, uri("/flavors")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flavor get(String id) {
        return get(TroveInstanceFlavor.class, uri("/flavors/%s", id)).execute();
    }

}
