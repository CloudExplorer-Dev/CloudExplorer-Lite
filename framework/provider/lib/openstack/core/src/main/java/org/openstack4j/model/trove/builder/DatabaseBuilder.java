package org.openstack4j.model.trove.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.trove.Database;

/**
 * Created by sumit gandhi on 8/19/2016.
 */
public interface DatabaseBuilder extends Buildable.Builder<DatabaseBuilder, Database> {

    DatabaseBuilder name(String name);

    DatabaseBuilder dbCharacterSet(String dbCharacterSet);

    DatabaseBuilder dbCollation(String dbCollation);

}
