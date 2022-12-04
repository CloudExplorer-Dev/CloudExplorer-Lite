package org.openstack4j.api.trove;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.trove.Database;
import org.openstack4j.model.trove.DatabaseUser;
import org.openstack4j.openstack.trove.domain.TroveDatabase.Databases;
import org.openstack4j.openstack.trove.domain.TroveDatabaseUser.DatabaseUsers;

import java.util.List;

/**
 * This interface defines all methods for the manipulation of trove database instance users
 *
 * @author sumit gandhi
 */

public interface UserService {

    /**
     * Returns list of all users for the database instance
     *
     * @return the list of users for the database instance
     */
    List<? extends DatabaseUser> list(String instanceId);

    /**
     * Returns list of all databases which the user has access to on the database instance
     *
     * @return the list of databases for a user
     */
    List<? extends Database> listUserDatabases(String instanceId, String userName);

    /**
     * Create a user for the database instance
     *
     * @return the action response
     */
    ActionResponse create(String instanceId, DatabaseUsers databaseUsers);

    /**
     * Deletes a user for the database instance
     *
     * @return the action response
     */
    ActionResponse delete(String instanceId, String userName);

    /**
     * Grant user access to a database on the database instance
     *
     * @return the action response
     */
    ActionResponse grantUserDBAccess(String instanceId, String userName, Databases databases);

    /**
     * Revoke user access to a database on the database instance
     *
     * @return the action response
     */
    ActionResponse revokeUserDBAccess(String instanceId, String userName, String dbName);


}
