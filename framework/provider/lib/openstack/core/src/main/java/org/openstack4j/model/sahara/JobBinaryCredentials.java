package org.openstack4j.model.sahara;

import org.openstack4j.model.ModelEntity;

/**
 * Credentials for JobBinary
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface JobBinaryCredentials extends ModelEntity {

    /**
     * @return the username
     */
    String getUser();

    /**
     * @return the password
     */
    String getPassword();

}
