package org.openstack4j.openstack.trove.internal;

import org.openstack4j.api.trove.DatabaseService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.trove.Database;
import org.openstack4j.openstack.trove.domain.TroveDatabase.Databases;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Database API Implementation
 *
 * @author sumit gandhi
 */
public class DBDatabaseServiceImpl extends BaseTroveServices implements DatabaseService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Database> list(String instanceId) {
        return get(Databases.class, uri("/instances/%s/databases", instanceId)).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse create(String instanceId, Databases databases) {
        checkNotNull(instanceId);
        checkNotNull(databases);
        return post(ActionResponse.class, uri("/instances/%s/databases", instanceId)).entity(databases).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String instanceId, String dbName) {
        checkNotNull(instanceId);
        checkNotNull(dbName);
        return deleteWithResponse(uri("/instances/%s/databases/%s", instanceId, dbName)).execute();
    }

}
