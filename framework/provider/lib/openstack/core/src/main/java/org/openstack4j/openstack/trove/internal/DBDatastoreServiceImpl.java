package org.openstack4j.openstack.trove.internal;

import org.openstack4j.api.trove.DatastoreService;
import org.openstack4j.model.trove.Datastore;
import org.openstack4j.model.trove.DatastoreVersion;
import org.openstack4j.openstack.trove.domain.TroveDatastore;
import org.openstack4j.openstack.trove.domain.TroveDatastore.Datastores;
import org.openstack4j.openstack.trove.domain.TroveDatastoreVersion;
import org.openstack4j.openstack.trove.domain.TroveDatastoreVersion.Versions;

import java.util.List;

/**
 * Datastore API Implementation
 *
 * @author sumit gandhi
 */
public class DBDatastoreServiceImpl extends BaseTroveServices implements DatastoreService {

    @Override
    public List<? extends Datastore> list() {
        return get(Datastores.class, uri("/datastores")).execute().getList();
    }

    @Override
    public Datastore get(String id) {
        return get(TroveDatastore.class, uri("/datastores/%s", id)).execute();
    }

    @Override
    public List<? extends DatastoreVersion> listDatastoreVersions(String datasoreId) {
        return get(Versions.class, uri("/datastores/%s/versions", datasoreId)).execute().getList();
    }

    @Override
    public DatastoreVersion getDatastoreVersion(String datastoreId, String versionId) {
        return get(TroveDatastoreVersion.class, uri("/datastores/%s/versions/%s", datastoreId, versionId)).execute();
    }

}
