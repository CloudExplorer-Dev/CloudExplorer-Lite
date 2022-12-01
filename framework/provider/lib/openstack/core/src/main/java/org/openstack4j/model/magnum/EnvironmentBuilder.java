package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable.Builder;

public interface EnvironmentBuilder extends Builder<EnvironmentBuilder, Environment> {
    /**
     * @see Environment#getPath
     */
    EnvironmentBuilder path(String path);

    /**
     * @see Environment#getLdLibraryPath
     */
    EnvironmentBuilder ldLibraryPath(String ldLibraryPath);

}
