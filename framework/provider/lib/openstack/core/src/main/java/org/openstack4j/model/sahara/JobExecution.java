package org.openstack4j.model.sahara;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.sahara.builder.JobExecutionBuilder;

import java.util.Date;

/**
 * An Openstack Sahara Job Execution
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface JobExecution extends ModelEntity, Buildable<JobExecutionBuilder> {

    /**
     * @return the cluster identifier
     */
    String getClusterId();

    /**
     * @return the input identifier
     */
    String getInputId();

    /**
     * @return the output identifier
     */
    String getOutputId();

    /**
     * @return the job configurations
     */
    JobConfig getJobConfigs();

    /**
     * @return the tenant id of the job execution
     */
    String getTenantId();

    /**
     * @return start time of the job execution
     */
    Date getStartTime();

    /**
     * @return end time of the job execution
     */
    Date getEndtime();

    /**
     * @return create time of the job execution
     */
    Date getCreatedAt();

    /**
     * @return update time of the job execution
     */
    Date getUpdatedAt();

    /**
     * @return return code of the job execution
     */
    String getReturnCode();

    /**
     * @return the OOzie job identifier
     */
    String getOozieJobId();

    /**
     * @return the job identifier
     */
    String getJobId();

    /**
     * @return the job execution identifier
     */
    String getId();

    /**
     * @return the progress of the job execution
     */
    String getProgress();

    /**
     * @return informations of the job executions
     */
    JobExecutionInfo getInfo();

    /**
     * @return the job identifier
     */
    String getJobIdForExecution();
}
