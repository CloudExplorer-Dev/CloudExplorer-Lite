package org.openstack4j.openstack.compute.internal;

import org.openstack4j.api.compute.FlavorService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.FlavorAccess;
import org.openstack4j.openstack.compute.domain.ExtraSpecsWrapper;
import org.openstack4j.openstack.compute.domain.NovaFlavor;
import org.openstack4j.openstack.compute.domain.NovaFlavor.Flavors;
import org.openstack4j.openstack.compute.domain.NovaFlavorAccess.AddTenantAccess;
import org.openstack4j.openstack.compute.domain.NovaFlavorAccess.FlavorAccesses;
import org.openstack4j.openstack.compute.domain.NovaFlavorAccess.RemoveTenantAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Flavor service provides CRUD capabilities for Flavor(s).  A flavor is an available hardware configuration/template for a server
 *
 * @author Jeremy Unruh, whaon
 */
public class FlavorServiceImpl extends BaseComputeServices implements FlavorService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Flavor> list() {
        return this.list(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Flavor> list(boolean detail) {
        return this.list(detail, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Flavor> list(Map<String, String> filteringParams) {
        return this.list(true, filteringParams);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Flavor> list(boolean detail, Map<String, String> filteringParams) {
        Invocation<Flavors> flavorInvocation = get(Flavors.class, uri("/flavors" + ((detail) ? "/detail" : "")));
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                flavorInvocation = flavorInvocation.param(entry.getKey(), entry.getValue());
            }
        }

        return flavorInvocation.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flavor get(String flavorId) {
        checkNotNull(flavorId);
        return get(NovaFlavor.class, uri("/flavors/%s", flavorId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String flavorId) {
        checkNotNull(flavorId);
        return deleteWithResponse(uri("/flavors/%s", flavorId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flavor create(Flavor flavor) {
        checkNotNull(flavor);
        return post(NovaFlavor.class, uri("/flavors"))
                .entity(flavor)
                .execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flavor create(String name, int ram, int vcpus, int disk, int ephemeral, int swap, float rxtxFactor, boolean isPublic) {
        checkNotNull(name);
        return create(NovaFlavor.builder().name(name).ram(ram).vcpus(vcpus).disk(disk).swap(swap).ephemeral(ephemeral).rxtxFactor(rxtxFactor).isPublic(isPublic).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> listExtraSpecs(String flavorId) {
        checkNotNull(flavorId);
        ExtraSpecsWrapper wrapper = get(ExtraSpecsWrapper.class, uri("/flavors/%s/os-extra_specs", flavorId)).execute();
        return wrapper != null ? wrapper.getExtraSpecs() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> createAndUpdateExtraSpecs(String flavorId, Map<String, String> spec) {
        checkNotNull(flavorId);
        checkNotNull(spec);
        return post(ExtraSpecsWrapper.class, uri("/flavors/%s/os-extra_specs", flavorId)).entity(ExtraSpecsWrapper.wrap(spec)).execute().getExtraSpecs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteExtraSpecs(String flavorId, String key) {
        checkNotNull(flavorId);
        checkNotNull(key);
        delete(Void.class, uri("/flavors/%s/os-extra_specs/%s", flavorId, key)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public String getSpec(String flavorId, String key) {
        checkNotNull(flavorId);
        checkNotNull(key);
        Map extraSpec = get(HashMap.class, uri("/flavors/%s/os-extra_specs/%s", flavorId, key)).execute();
        return extraSpec == null ? null : (String) extraSpec.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends FlavorAccess> listFlavorAccess(String flavorId) {
        checkNotNull(flavorId);
        return get(FlavorAccesses.class, uri("/flavors/%s/os-flavor-access", flavorId))
                .execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends FlavorAccess> addTenantAccess(String flavorId, String tenantId) {
        checkNotNull(flavorId);
        checkNotNull(tenantId);

        AddTenantAccess addTenantAccess = new AddTenantAccess();
        addTenantAccess.setTenantId(tenantId);

        return post(FlavorAccesses.class, uri("/flavors/%s/action", flavorId))
                .entity(addTenantAccess)
                .execute()
                .getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends FlavorAccess> removeTenantAccess(String flavorId, String tenantId) {
        checkNotNull(flavorId);
        checkNotNull(tenantId);

        RemoveTenantAccess removeTenantAccess = new RemoveTenantAccess();
        removeTenantAccess.setTenantId(tenantId);

        return post(FlavorAccesses.class, uri("/flavors/%s/action", flavorId))
                .entity(removeTenantAccess)
                .execute()
                .getList();
    }
}
