package org.openstack4j.model.senlin;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.senlin.builder.ClusterActionCreateBuilder;

/**
 * This interface describes the model of a {@link Cluster}, before it is sent to
 * the server for creation
 *
 * @author lion
 */
public interface ClusterActionCreate extends ModelEntity, Buildable<ClusterActionCreateBuilder> {

}
