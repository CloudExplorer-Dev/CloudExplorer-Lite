package org.openstack4j.api.octavia;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.octavia.LoadBalancerV2;
import org.openstack4j.model.octavia.LoadBalancerV2Stats;
import org.openstack4j.model.octavia.LoadBalancerV2StatusTree;
import org.openstack4j.model.octavia.LoadBalancerV2Update;

import java.util.List;
import java.util.Map;

/**
 * Octavia V2 loadbalancer Extention API
 *
 * @author wei
 */
public interface LoadBalancerV2Service extends RestService {
    /**
     * List all loadbalancers  that the current tenant has access to
     *
     * @return list of all loadbalancers
     */
    List<? extends LoadBalancerV2> list();

    /**
     * Returns list of loadbalancers filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     * @return list of loadbalancer fitered by filteringParams
     */
    List<? extends LoadBalancerV2> list(Map<String, String> filteringParams);

    /**
     * Get the specified loadbalancer by ID
     *
     * @param loadbalancerId the loadbalancer identifier
     * @return the loadbalancer or null if not found
     */
    LoadBalancerV2 get(String loadbalancerId);

    /**
     * Delete the specified loadbalancer by ID
     *
     * @param loadbalancerId the loadbalancer identifier
     * @return the action response
     */
    ActionResponse delete(String loadbalancerId);

    /**
     * Cascade delete the specified loadbalancer by ID
     *
     * @param loadbalancerId the loadbalancer identifier
     * @return the action response
     */
    ActionResponse cascadeDelete(String loadbalancerId);

    /**
     * Create a loadbalancer
     *
     * @return loadbalancer
     */
    LoadBalancerV2 create(LoadBalancerV2 loadbalancer);

    /**
     * Update a loadbalancer
     *
     * @param loadbalancerId the loadbalancer identifier
     * @param loadbalancer   LoadBalancerV2Update
     * @return loadbalancer
     */
    LoadBalancerV2 update(String loadbalancerId, LoadBalancerV2Update loadbalancer);

    /**
     * Retrieve statistics on a loadbalancer
     *
     * @return LoadBalancerV2Stats
     */
    LoadBalancerV2Stats stats(String loadbalancerId);

    /**
     * Retrieve the status tree of a loadbalancer
     *
     * @return status
     */
    LoadBalancerV2StatusTree statusTree(String loadbalancerId);
}
