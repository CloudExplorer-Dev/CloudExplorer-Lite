package org.openstack4j.api.sahara;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.sahara.JobBinaryInternal;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Sahara Data Processing Operations
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface JobBinaryInternalService extends RestService {

    /**
     * List all job binary internals
     *
     * @return list of job binary internals or empty
     */
    List<? extends JobBinaryInternal> list();

    /**
     * Get a job binary internal by ID
     *
     * @param jobBinaryInternalId the job binary internal identifier
     * @return the job binary internal or null if not found
     */
    JobBinaryInternal get(String jobBinaryInternalId);

    /**
     * Create a new job binary internal
     *
     * @param payload the payload to create a new job binary internal
     * @return the created job binary internal
     */
    JobBinaryInternal create(Payload<File> payload);

    /**
     * Delete the specified job binary internal
     *
     * @param jobBinaryInternalId the job binary internal identifier
     * @return the action response
     */
    ActionResponse delete(String jobBinaryInternalId);

    /**
     * Retrieves data of specified job binary internal object
     *
     * @param jobBinaryInternalId the job binary internal identifier
     * @return Job Binary Internal data
     */
    Payload<InputStream> getData(String jobBinaryInternalId);
}
