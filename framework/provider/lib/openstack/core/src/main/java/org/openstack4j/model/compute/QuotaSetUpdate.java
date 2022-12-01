package org.openstack4j.model.compute;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.compute.builder.QuotaSetUpdateBuilder;

/**
 * Represents an updateable quota set
 *
 * @author Jeremy Unruh
 */
public interface QuotaSetUpdate extends ModelEntity, Buildable<QuotaSetUpdateBuilder> {

}
