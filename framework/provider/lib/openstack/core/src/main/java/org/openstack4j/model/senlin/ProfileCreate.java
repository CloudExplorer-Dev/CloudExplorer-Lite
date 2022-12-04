package org.openstack4j.model.senlin;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.senlin.builder.ProfileCreateBuilder;

/**
 * This interface describes the model of a {@link Profile}, before it is sent to
 * the server for creation
 *
 * @author lion
 */
public interface ProfileCreate extends ModelEntity, Buildable<ProfileCreateBuilder> {

}
