package org.openstack4j.api.trove;

import org.openstack4j.common.RestService;

/**
 * This interface contains all available Trove Services
 *
 * @author sumit gandhi
 */
public interface TroveService extends RestService {

    /**
     * Service implementation which provides methods for manipulation of databases
     *
     * @return DatabaseService
     */
    DatabaseService databaseService();

    /**
     * Service implementation which provides methods for manipulation of database users
     *
     * @return UserService
     */
    UserService databaseUsersService();

    /**
     * Service implementation which provides methods for manipulation of datastores
     *
     * @return DatastoreService
     */
    DatastoreService datastoreService();

    /**
     * Service implementation which provides methods for manipulation of database instance flavors
     *
     * @return DBInstanceFlavorService
     */
    InstanceFlavorService flavorService();

    /**
     * Service implementation which provides methods for manipulation of database instances
     *
     * @return InstanceService
     */
    InstanceService instanceService();

}
