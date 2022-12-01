package org.openstack4j.model.sahara;

import org.openstack4j.model.ModelEntity;

import java.util.Date;

/**
 * An Openstack Sahara Job Binary Internal
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface JobBinaryInternal extends ModelEntity {

    /**
     * @return the name of the job binary internal
     */
    String getName();

    /**
     * @return the tenant id of the job binary internal
     */
    String getTenantId();

    /**
     * @return the created date of the job binary internal
     */
    Date getCreatedAt();

    /**
     * @return the updated date of the job binary internal
     */
    Date getUpdatedAt();

    /**
     * @return the data size of the job binary internal
     */
    int getDataSize();

    /**
     * @return the identifier of the job binary internal
     */
    String getId();

}
