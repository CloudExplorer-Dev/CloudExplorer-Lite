package org.openstack4j.openstack.networking.internal.ext;

import org.openstack4j.api.networking.ext.VipService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.Vip;
import org.openstack4j.model.network.ext.VipUpdate;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.networking.domain.ext.NeutronVip;
import org.openstack4j.openstack.networking.domain.ext.NeutronVip.Vips;
import org.openstack4j.openstack.networking.internal.BaseNetworkingServices;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * OpenStack (Neutron) Lbaas vip based Operations
 *
 * @author liujunpeng
 */
public class VipServiceImpl extends BaseNetworkingServices implements
        VipService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Vip> list() {
        return get(Vips.class, uri("/lb/vips")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Vip> list(Map<String, String> filteringParams) {
        Invocation<Vips> req = get(Vips.class, uri("/lb/vips"));
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                req = req.param(entry.getKey(), entry.getValue());
            }
        }
        return req.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vip get(String vipId) {
        checkNotNull(vipId);
        return get(NeutronVip.class, uri("/lb/vips/%s", vipId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String vipId) {
        checkNotNull(vipId);
        return ToActionResponseFunction.INSTANCE.apply(delete(Void.class, uri("/lb/vips/%s", vipId)).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vip create(Vip vip) {
        checkNotNull(vip);
        return post(NeutronVip.class, uri("/lb/vips")).entity(vip).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vip update(String vipId, VipUpdate vip) {
        checkNotNull(vipId);
        checkNotNull(vip);
        return put(NeutronVip.class, uri("/lb/vips/%s", vipId)).entity(vip).execute();
    }

}
