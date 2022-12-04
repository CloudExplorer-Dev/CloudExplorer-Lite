package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;

/**
 * Server password
 *
 * @author vinod borole
 */
public interface ServerPassword extends ModelEntity {
    /**
     * @return the password of the server
     */
    String getPassword();
}
