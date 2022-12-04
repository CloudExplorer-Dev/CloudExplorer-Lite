package org.openstack4j.model.sahara;

import org.openstack4j.model.ModelEntity;

import java.util.Date;
import java.util.List;

/**
 * An Openstack Sahara Instance
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface Instance extends ModelEntity {

    /**
     * @return the name of the instance
     */
    String getName();

    /**
     * @return the created date of the instance
     */
    Date getCreatedAt();

    /**
     * @return the updated date of the instance
     */
    Date getUpdatedAt();

    /**
     * @return the NOVA instance identifier
     */
    String getInstanceId();

    /**
     * @return the management IP of the instance
     */
    String getManagementIp();

    /**
     * TODO: how volumes are presented?
     *
     * @return the volumes of the instance
     */
    List<String> getVolumes();

    /**
     * @return the internal IP of the instance
     */
    String getInternalIp();

    /**
     * @return the Sahara instance identifier
     */
    String getId();

}
