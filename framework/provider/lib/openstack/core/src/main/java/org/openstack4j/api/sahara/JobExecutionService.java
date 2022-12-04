package org.openstack4j.api.sahara;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.sahara.JobExecution;

import java.util.List;

/**
 * Sahara Data Processing Operations
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface JobExecutionService extends RestService {

    /**
     * Create a new job execution
     *
     * @param jobExecution the job execution to create
     * @return the created job execution
     */
    JobExecution create(JobExecution jobExecution);

    /**
     * List all job executions
     *
     * @return list of job executions or empty
     */
    List<? extends JobExecution> list();

    /**
     * Get a job execution by ID
     *
     * @param jobExecutionId the job execution identifier
     * @return the job execution or null if not found
     */
    JobExecution get(String jobExecutionId);

    /**
     * Refresh the status of a job execution by ID
     *
     * @param jobExecutionId the job execution identifier
     * @return the refreshed job execution or null if not found
     */
    JobExecution refreshStatus(String jobExecutionId);

    /**
     * Cancel a job execution by ID
     *
     * @param jobExecutionId the job execution identifier
     * @return the canceled job execution or null if not found
     */
    JobExecution cancel(String jobExecutionId);

    /**
     * Delete a job execution by ID
     *
     * @param jobExecutionId the job execution identifier
     * @return the action response
     */
    ActionResponse delete(String jobExecutionId);
}
