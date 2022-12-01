package org.openstack4j.model.sahara;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.sahara.builder.JobBinaryBuilder;

import java.util.Date;

/**
 * An Openstack Sahara Job Binary
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface JobBinary extends ModelEntity, Buildable<JobBinaryBuilder> {

    /**
     * @return the description of the job binary
     */
    String getDescription();

    /**
     * @return the URL of the job binary
     */
    String getURL();

    /**
     * @return the tenant id of the job binary
     */
    String getTenantId();

    /**
     * @return the created date of the job binary
     */
    Date getCreatedAt();

    /**
     * @return the updated date of the job binary
     */
    Date getUpdatedAt();

    /**
     * @return the identifier of the job binary
     */
    String getId();

    /**
     * @return the name of the job binary
     */
    String getName();

    /**
     * @return the credentials of the job binary
     */
    JobBinaryCredentials getCredentials();
}
