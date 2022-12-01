package org.openstack4j.model.image.v2.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.image.v2.Task;

import java.util.Map;

/**
 * Builder for Glance V2 tasks
 *
 * @author emjburns
 */
public interface TaskBuilder extends Buildable.Builder<TaskBuilder, Task> {
    /**
     * @see Task#getType()
     */
    TaskBuilder type(String type);

    /**
     * @see Task#getInput()
     */
    TaskBuilder input(Map<String, Object> input);
}
