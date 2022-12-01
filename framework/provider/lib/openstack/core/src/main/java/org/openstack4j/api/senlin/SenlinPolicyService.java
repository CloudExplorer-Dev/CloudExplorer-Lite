package org.openstack4j.api.senlin;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.senlin.Policy;
import org.openstack4j.model.senlin.PolicyCreate;

import java.util.List;

/**
 * This interface defines all methods for the manipulation of Policy
 *
 * @author lion
 */
public interface SenlinPolicyService {

    /**
     * Gets a list of currently existing {@link Policy}s.
     *
     * @return the list of {@link Policy}s
     */
    List<? extends Policy> list();

    /**
     * <code>POST /v1/policies</code><br \>
     * <p>
     * Creates a new {@link Policy} out of a {@link PolicyCreate} object
     *
     * @param newPolicy {@link PolicyCreate} object out of which policy is to be created
     * @return new {@link Policy} as returned from the server
     */
    Policy create(PolicyCreate newPolicy);

    /**
     * returns details of a {@link Policy}.
     *
     * @param policyID Id of {@link Policy}
     * @return Policy
     */
    Policy get(String policyID);

    /**
     * <code>PATCH /v1/policies/​{policy_id}​</code><br \>
     * <p>
     * Update a {@link Policy} out of a {@link PolicyCreate} object
     *
     * @param policyID  Id of {@link Policy}
     * @param newPolicy {@link PolicyCreate} object out of which stack is to be update
     * @return new {@link Policy} as returned from the server
     */
    Policy update(String policyID, PolicyCreate newPolicy);

    /**
     * Deletes the specified {@link Policy} from the server.
     *
     * @param policyID Id of {@link Policy}
     * @return the action response
     */
    ActionResponse delete(String policyID);
}
