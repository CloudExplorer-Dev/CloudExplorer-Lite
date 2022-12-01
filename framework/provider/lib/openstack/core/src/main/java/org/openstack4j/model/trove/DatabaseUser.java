package org.openstack4j.model.trove;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.trove.builder.DatabaseUserBuilder;
import org.openstack4j.openstack.trove.domain.TroveDatabase;

import java.util.List;

/**
 * Database User Model Entity
 *
 * @author sumit gandhi
 */
public interface DatabaseUser extends ModelEntity, Buildable<DatabaseUserBuilder> {

    String getUsername();

    List<TroveDatabase> getTroveDatabaseList();
}
