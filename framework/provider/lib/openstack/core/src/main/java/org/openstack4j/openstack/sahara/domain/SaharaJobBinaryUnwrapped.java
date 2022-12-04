package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.sahara.JobBinary;

/**
 * An OpenStack Sahara
 * Unwrap the root name of Job Binary when serialized into json request
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public class SaharaJobBinaryUnwrapped implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonUnwrapped
    //@JsonProperty("job_binary")
    private JobBinary jobBinary;

    public SaharaJobBinaryUnwrapped(JobBinary jobBinary) {
        this.jobBinary = jobBinary;
    }

    public JobBinary getJobBinary() { // need for serialization
        return jobBinary;
    }
}
