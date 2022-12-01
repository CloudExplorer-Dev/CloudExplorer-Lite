package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;

public interface Environment extends ModelEntity, Buildable<EnvironmentBuilder> {
    /**
     * Gets path
     *
     * @return path
     */
    String getPath();


    /**
     * Gets ldLibraryPath
     *
     * @return ldLibraryPath
     */
    String getLdLibraryPath();


}
