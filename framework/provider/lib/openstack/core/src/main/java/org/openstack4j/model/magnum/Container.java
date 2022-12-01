package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.magnum.MagnumEnvironment;

import java.util.List;

public interface Container extends ModelEntity, Buildable<ContainerBuilder> {
    /**
     * Gets status
     *
     * @return status
     */
    String getStatus();

    /**
     * Gets uuid
     *
     * @return uuid
     */
    String getUuid();

    /**
     * Gets links
     *
     * @return links
     */
    List<GenericLink> getLinks();

    /**
     * Gets image
     *
     * @return image
     */
    String getImage();

    /**
     * Gets environment
     *
     * @return environment
     */
    MagnumEnvironment getEnvironment();

    /**
     * Gets command
     *
     * @return command
     */
    String getCommand();

    /**
     * Gets memory
     *
     * @return memory
     */
    String getMemory();

    /**
     * Gets bayUuid
     *
     * @return bayUuid
     */
    String getBayUuid();

    /**
     * Gets name
     *
     * @return name
     */
    String getName();

}
