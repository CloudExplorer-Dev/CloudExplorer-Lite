package org.openstack4j.model.senlin.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.senlin.PolicyCreate;

import java.util.Map;

/**
 * This interface describes a builder for {@link PolicyCreate} objects
 *
 * @author lion
 */
public interface PolicyCreateBuilder extends Buildable.Builder<PolicyCreateBuilder, PolicyCreate> {

    /**
     * Add the name for the policy.
     *
     * @param name The name for the policy.
     * @return PolicyCreateBuilder
     */
    PolicyCreateBuilder name(String name);

    /**
     * Add a detailed specification based on the policy type.
     *
     * @param spec A detailed specification based on the policy type.
     * @return PolicyCreateBuilder
     */
    PolicyCreateBuilder spec(Map<String, Object> spec);

    /**
     * Add the properties of the spec.
     *
     * @param properties The properties of the spec.
     * @return PolicyCreateBuilder
     */
    PolicyCreateBuilder properties(Map<String, Object> properties);

    /**
     * Add the adjustment of the policy.
     *
     * @param adjustment The adjustment of the policy.
     * @return PolicyCreateBuilder
     */
    PolicyCreateBuilder adjustment(Map<String, String> adjustment);

    /**
     * Add the value of the min_step for policy.
     *
     * @param minStep The value of the min_step for policy.
     * @return PolicyCreateBuilder
     */
    PolicyCreateBuilder minStep(int minStep);

    /**
     * Add the number of the policy.
     *
     * @param number The number of the policy.
     * @return PolicyCreateBuilder
     */
    PolicyCreateBuilder number(int number);

    /**
     * Add the type of the adjustment for the policy.
     *
     * @param adjustmentType The type of the adjustment for the policy.
     * @return PolicyCreateBuilder
     */
    PolicyCreateBuilder adjustmentType(String adjustmentType);

    /**
     * Add the event of the properties for the policy.
     *
     * @param event The event of the properties for the policy.
     * @return PolicyCreateBuilder
     */
    PolicyCreateBuilder event(String event);

    /**
     * Add the type of the spec for the policy.
     *
     * @param specType The type of the spec for the policy.
     * @return PolicyCreateBuilder
     */
    PolicyCreateBuilder specType(String specType);

    /**
     * Add the version of the spec for the policy.
     *
     * @param version The version of the spec for the policy.
     * @return PolicyCreateBuilder
     */
    PolicyCreateBuilder version(String version);

}
