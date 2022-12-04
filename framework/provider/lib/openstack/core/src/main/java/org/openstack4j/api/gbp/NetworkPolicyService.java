package org.openstack4j.api.gbp;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.gbp.NetworkServicePolicy;

import java.util.List;
import java.util.Map;

/**
 * This interface defines all methods for the manipulation of network service
 *
 * @author vinod borole
 */
public interface NetworkPolicyService {

    /**
     * List all gbp network service policies
     */
    List<? extends NetworkServicePolicy> list();

    /**
     * Returns list of service policy filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends NetworkServicePolicy> list(Map<String, String> filteringParams);

    /**
     * Get a gbp network service policy specified by id
     */
    NetworkServicePolicy get(String id);

    /**
     * Create a gbp network service policy
     */
    NetworkServicePolicy create(NetworkServicePolicy networkServicePolicy);

    /**
     * Delete a gbp network service policy specified by id
     */
    ActionResponse delete(String id);

    /**
     * Update the gbp network service policy specified by id
     */
    NetworkServicePolicy update(String gbpServicePolicyId, NetworkServicePolicy gbpServicePolicy);

}
