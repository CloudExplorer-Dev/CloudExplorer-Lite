package org.openstack4j.openstack.workflow.internal;

import org.openstack4j.api.workflow.TaskExecutionService;
import org.openstack4j.model.workflow.TaskExecution;
import org.openstack4j.openstack.workflow.domain.MistralTaskExecution;
import org.openstack4j.openstack.workflow.domain.MistralTaskExecution.MistralTaskExecutions;

import java.util.List;

/**
 * Task execution service implementation.
 *
 * @author Renat Akhmerov
 */
public class TaskExecutionServiceImpl extends BaseMistralService implements TaskExecutionService {

    @Override
    public List<? extends TaskExecution> list() {
        return get(MistralTaskExecutions.class, uri("/tasks")).execute().getList();
    }

    @Override
    public TaskExecution get(String id) {
        return get(MistralTaskExecution.class, uri("/tasks/%s", id)).execute();
    }
}
