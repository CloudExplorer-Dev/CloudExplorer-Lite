package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.sahara.JobExecution;

/**
 * An OpenStack Sahara
 * Unwrap the root name of Job Execution when serialized into json request
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */

public class SaharaJobExecutionUnwrapped implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonUnwrapped
    //@JsonProperty("job_execution")
    private JobExecution jobExecution;

    public SaharaJobExecutionUnwrapped(JobExecution jobExecution) {
        this.jobExecution = jobExecution;
    }

    public JobExecution getJobExecution() { // need for serialization
        return jobExecution;
    }
}
