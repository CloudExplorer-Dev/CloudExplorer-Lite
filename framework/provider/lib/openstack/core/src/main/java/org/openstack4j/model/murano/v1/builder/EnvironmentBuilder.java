package org.openstack4j.model.murano.v1.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.murano.v1.domain.Environment;

/**
 * Builder interface used for {@link Environment} object.
 * For mapping from object builder to JSON request
 */
public interface EnvironmentBuilder extends Builder<EnvironmentBuilder, Environment> {

    //
    // Define setter apis for user to create a new environment
    //

    /**
     * See {@link Environment#getName()}
     *
     * @param name the name of the environment
     * @return EnvironmentBuilder
     */
    EnvironmentBuilder name(String name);
}
