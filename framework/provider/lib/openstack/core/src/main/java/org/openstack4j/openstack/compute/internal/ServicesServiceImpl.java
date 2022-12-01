package org.openstack4j.openstack.compute.internal;

import org.openstack4j.api.compute.ext.ServicesService;
import org.openstack4j.model.compute.ext.Service;
import org.openstack4j.openstack.compute.domain.ext.ExtService;
import org.openstack4j.openstack.compute.domain.ext.ExtService.Services;
import org.openstack4j.openstack.manila.domain.actions.ServiceAction;
import org.openstack4j.openstack.manila.domain.actions.ServiceActions;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Compute Services service provides CRUD capabilities for nova service(s).
 *
 * @author Stephan Latour
 */
public class ServicesServiceImpl extends BaseComputeServices implements ServicesService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Service> list() {
        return get(Services.class, uri("/os-services")).execute().getList();
    }

    /**
     * Returns list of compute services filtered by parameters.
     * <p>Author:Wang Ting/王婷</p>
     *
     * @Title: list
     * @see org.openstack4j.api.compute.ServicesService#list(java.util.Map)
     */
    @Override
    public List<? extends Service> list(Map<String, String> filteringParams) {
        Invocation<Services> req = get(Services.class, uri("/os-services"));
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                req = req.param(entry.getKey(), entry.getValue());
            }
        }
        return req.execute().getList();
    }

    /**
     * Enables a compute services.
     * <p>Author:Wang Ting/王婷</p>
     *
     * @Title: enableService
     * @see org.openstack4j.api.compute.ServicesService#enableService(java.lang.String, java.lang.String)
     */
    @Override
    public ExtService enableService(String binary, String host) {
        checkNotNull(binary);
        checkNotNull(host);

        return put(ExtService.class, uri("/os-services/enable")).entity(ServiceAction.enable(binary, host)).execute();
    }

    /**
     * Disables a compute service.
     * <p>Author:Wang Ting/王婷</p>
     *
     * @Title: disableService
     * @see org.openstack4j.api.compute.ServicesService#disableService(java.lang.String, java.lang.String)
     */
    @Override
    public ExtService disableService(String binary, String host) {
        checkNotNull(binary);
        checkNotNull(host);

        return put(ExtService.class, uri("/os-services/disable")).entity(ServiceAction.disable(binary, host)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtService forceDownService(String binary, String host) {
        checkNotNull(binary);
        checkNotNull(host);
        return put(ExtService.class, uri("/os-services/force-down")).header("x-openstack-nova-api-version", "2.11")
                .entity(ServiceActions.forceDown(binary, host)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtService forceUpService(String binary, String host) {
        checkNotNull(binary);
        checkNotNull(host);
        return put(ExtService.class, uri("/os-services/force-down")).header("x-openstack-nova-api-version", "2.11")
                .entity(ServiceActions.forceUp(binary, host)).execute();
    }
}
