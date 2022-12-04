package org.openstack4j.model.sahara;

import org.openstack4j.model.ModelEntity;

/**
 * Credentials for Data Source
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface DataSourceCredentials extends ModelEntity {

    /**
     * @return the username
     */
    String getUser();

    /**
     * @return the password
     */
    String getPassword();

}
