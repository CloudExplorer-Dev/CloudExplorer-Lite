package org.openstack4j.model.trove;

import org.openstack4j.model.ModelEntity;
import org.openstack4j.openstack.trove.domain.TroveDatastoreVersion;

import java.util.List;

/**
 * Datastore Model Entity
 *
 * @author sumit gandhi
 */
public interface Datastore extends ModelEntity {

    String getName();

    String getId();

    String getDefault_version();

    List<TroveDatastoreVersion> getTroveDatastoreVersionList();
}
