package org.openstack4j.api.image.v2;

import org.openstack4j.common.RestService;
import org.openstack4j.model.image.v2.Task;

import java.util.List;
import java.util.Map;

/**
 * Image (Glance) V2 Tasks Api
 *
 * @author emjburns
 */
public interface TaskService extends RestService {
    /**
     * List tasks
     *
     * @return a list of tasks.
     */
    List<? extends Task> list();

    /**
     * List tasks filtering by parameters.
     * Use these parameters to do pagination as described in
     * http://developer.openstack.org/api-ref/image/v2/index.html#list-tasks
     *
     * @return a list of tasks
     */
    List<? extends Task> list(Map<String, String> filteringParams);

    /**
     * Show details for a task.
     *
     * @return a specific task
     */
    Task get(String taskId);

    /**
     * Create a task
     *
     * @return the task
     */
    Task create(Task task);
}
