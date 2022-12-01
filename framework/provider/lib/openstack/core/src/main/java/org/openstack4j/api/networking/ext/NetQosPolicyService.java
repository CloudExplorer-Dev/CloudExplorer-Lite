package org.openstack4j.api.networking.ext;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.network.ext.NetQosPolicy;
import org.openstack4j.model.network.ext.NetQosPolicyUpdate;

import java.util.List;

/**
 * Networking (Neutron) Qos Policy Extension API
 *
 * @author bboyHan
 */
public interface NetQosPolicyService extends RestService {

    /**
     * Lists qos policies for tenants
     *
     * @return the list of qos policy
     */
    List<? extends NetQosPolicy> list();

    /**
     * Fetches the network qos policy for the specified tenant
     *
     * @param tenantId the tenant identifier
     * @return the NetQosPolicy
     */
    NetQosPolicy get(String tenantId);

    /**
     * Updates the network qos policy for the current tenant
     *
     * @param netQosPolicy the net qos policy to update
     * @return the updated network qos policy
     */
    NetQosPolicy update(NetQosPolicyUpdate netQosPolicy);

    /**
     * Create the current network qos policy for the current tenant back to defaults
     *
     * @return NetQosPolicy the response object
     */
    NetQosPolicy create(NetQosPolicy netQosPolicy);

    /**
     * Delete the current network qos policy for the current tenant back to defaults
     *
     * @param policyId the net qos uuid
     * @return the action response
     */
    ActionResponse delete(String policyId);
}
