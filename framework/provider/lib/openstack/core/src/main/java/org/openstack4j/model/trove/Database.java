package org.openstack4j.model.trove;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.trove.builder.DatabaseBuilder;

/**
 * Database Model Entity
 *
 * @author sumit gandhi
 */
public interface Database extends ModelEntity, Buildable<DatabaseBuilder> {

    String getName();

    String getDbCharacterSet();

    String getDbCollation();

}
