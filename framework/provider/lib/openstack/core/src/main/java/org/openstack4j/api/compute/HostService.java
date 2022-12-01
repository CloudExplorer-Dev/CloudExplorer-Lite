package org.openstack4j.api.compute;

import org.openstack4j.common.RestService;
import org.openstack4j.model.compute.HostResource;

import java.util.List;
import java.util.Map;

/**
 * Nova OS Host Service
 *
 * @author Qin An
 * @see ServerService
 * @deprecated The os-hosts API is deprecated as of the 2.43 microversion. Requests made with microversion >= 2.43 will result in a 404 error. To list and show host details, use the Hypervisors (os-hypervisors) API. To enable or disable a service, use the Compute services (os-services) API. There is no replacement for the shutdown, startup, reboot, or maintenance_mode actions as those are system-level operations which should be outside of the control of the compute service.
 */
@Deprecated
public interface HostService extends RestService {

    /**
     * Shows details for a specified host
     *
     * @return the Resource of the Host specified
     */
    public List<? extends HostResource> hostDescribe(String hostName);

    /**
     * List all host that the current tenant has access to
     *
     * @return list of all hosts
     * @author Wang Ting/王婷
     */
    List<? extends HostResource> list();

    /**
     * Returns list of hosts filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     * @author Wang Ting/王婷
     */
    List<? extends HostResource> list(Map<String, String> filteringParams);

}
