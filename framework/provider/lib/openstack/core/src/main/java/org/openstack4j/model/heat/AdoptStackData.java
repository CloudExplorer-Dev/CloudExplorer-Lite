package org.openstack4j.model.heat;

import org.openstack4j.model.ModelEntity;

import java.util.Map;

/**
 * This interface describes <code>adopt_stack_data</code> element. It is used
 * for stack adoption and as a return value for stack abandoning. All getters
 * map to the possible return values of
 * <code> Delete /v1/{tenant_id}/stacks/{stack_name}/{stack_id}/abandon</code>
 *
 * @author Ales Kemr
 * @see https://developer.openstack.org/api-ref/orchestration/v1
 */
public interface AdoptStackData extends ModelEntity {

    /**
     * Returns stack action, e.g. CREATE
     *
     * @return stack action
     */
    String getAction();

    /**
     * Returns the id of the stack
     *
     * @return the id of the stack
     */
    String getId();

    /**
     * Returns the name of the stack
     *
     * @return the name of the stack
     */
    String getName();

    /**
     * Returns the status of the stack
     *
     * @return the status of the stack
     */
    String getStatus();

    /**
     * Returns stack template as a map
     *
     * @return stack template as a map
     */
    Map<String, Object> getTemplate();

    /**
     * Returns map of existing resources, to be adopted into the stack
     *
     * @return Map of existing resources to be adopted into the stack
     */
    Map<String, Map<String, Object>> getResources();
}
