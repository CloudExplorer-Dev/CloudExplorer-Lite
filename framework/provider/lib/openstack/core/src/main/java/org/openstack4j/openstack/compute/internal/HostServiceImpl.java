package org.openstack4j.openstack.compute.internal;

import org.openstack4j.api.compute.HostService;
import org.openstack4j.model.compute.HostResource;
import org.openstack4j.openstack.compute.domain.NovaHost;
import org.openstack4j.openstack.compute.domain.NovaHostResource.NovaHostResources;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * OS Host Service
 *
 * @author Qin An
 */
@Deprecated
public class HostServiceImpl extends BaseComputeServices implements HostService {

    @Override
    public List<? extends HostResource> hostDescribe(String hostName) {
        checkNotNull(hostName);
        return get(NovaHost.class, uri("/os-hosts/%s", hostName)).execute().getList();
    }

    /**
     * List all host that the current tenant has access to
     * {@inheritDoc}
     *
     * @author Wang Ting/王婷
     */
    @Override
    public List<? extends HostResource> list() {
        Invocation<NovaHostResources> req = get(NovaHostResources.class, uri("/os-hosts"));
        return req.execute().getList();

    }

    /**
     * Returns list of hosts filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     *                        Now supports query by zone name
     *                        {@inheritDoc}
     * @author Wang Ting/王婷
     */
    @Override
    public List<? extends HostResource> list(Map<String, String> filteringParams) {
        Invocation<NovaHostResources> req = get(NovaHostResources.class, uri("/os-hosts"));
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                req = req.param(entry.getKey(), entry.getValue());
            }
        }
        return req.execute().getList();
    }
}
