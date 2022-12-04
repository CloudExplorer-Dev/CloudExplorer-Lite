package org.openstack4j.model.senlin;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.senlin.builder.PolicyCreateBuilder;

/**
 * This interface describes the model of a {@link Policy}, before it is sent to
 * the server for creation
 *
 * @author lion
 */
public interface PolicyCreate extends ModelEntity, Buildable<PolicyCreateBuilder> {

}
