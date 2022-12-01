package org.openstack4j.model.trove;

import org.openstack4j.model.ModelEntity;

/**
 * Datastore Version Model Entity
 *
 * @author sumit gandhi
 */
public interface DatastoreVersion extends ModelEntity {

    String getName();

    String getId();

    String getDatastoreId();

    int getIsActive();

    String getImage();

    String getPackageName();
}
