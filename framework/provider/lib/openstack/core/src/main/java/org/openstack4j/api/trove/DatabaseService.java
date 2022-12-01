package org.openstack4j.api.trove;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.trove.Database;
import org.openstack4j.openstack.trove.domain.TroveDatabase.Databases;

import java.util.List;

/**
 * This interface defines all methods for the manipulation of databases
 *
 * @author sumit gandhi
 */

public interface DatabaseService {

    /**
     * Gets the database specified by ID
     *
     * @return the database or null if not found
     */
    List<? extends Database> list(String instanceId);

    /**
     * Create a new database
     *
     * @return the action response
     */
    ActionResponse create(String id, Databases databases);

    /**
     * Deletes the database
     *
     * @return the action response
     */
    ActionResponse delete(String instanceId, String name);

}
