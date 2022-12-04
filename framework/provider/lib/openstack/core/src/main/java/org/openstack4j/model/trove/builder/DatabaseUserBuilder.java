package org.openstack4j.model.trove.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.trove.DatabaseUser;
import org.openstack4j.openstack.trove.domain.TroveDatabase;

import java.util.List;

/**
 * Created by sumit gandhi on 9/3/2016.
 */
public interface DatabaseUserBuilder extends Buildable.Builder<DatabaseUserBuilder, DatabaseUser> {

    DatabaseUserBuilder username(String username);

    DatabaseUserBuilder password(String password);

    DatabaseUserBuilder troveDatabaseList(List<TroveDatabase> troveDatabaseList);

}
