package org.openstack4j.api.sahara;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.sahara.JobBinary;

import java.io.InputStream;
import java.util.List;

/**
 * Sahara Data Processing Operations
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface JobBinaryService extends RestService {

    /**
     * List all job binaries
     *
     * @return list of job binaries or empty
     */
    List<? extends JobBinary> list();

    /**
     * Get a job binary by ID
     *
     * @param JobBinaryId the job binary identifier
     * @return the job binary or null if not found
     */
    JobBinary get(String JobBinaryId);

    /**
     * Create a new job binary
     *
     * @param jobBinary the job binary to create
     * @return the created job binary
     */
    JobBinary create(JobBinary jobBinary);

    /**
     * Delete the specified job binary
     *
     * @param JobBinaryId the job binary identifier
     * @return the action response
     */
    ActionResponse delete(String JobBinaryId);

    /**
     * Retrieves data of specified job binary object
     *
     * @param JobBinaryId the job binary identifier
     * @return Job Binary data
     */
    Payload<InputStream> getData(String JobBinaryId);
}
