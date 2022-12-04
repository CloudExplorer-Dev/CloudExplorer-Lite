package org.openstack4j.model.heat;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.heat.builder.StackUpdateBuilder;

/**
 * Model Entity used for updating a Stack
 *
 * @author Jeremy Unruh
 */
public interface StackUpdate extends BaseStackCreateUpdate, Buildable<StackUpdateBuilder> {

}
