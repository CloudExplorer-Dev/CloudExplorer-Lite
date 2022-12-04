package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.magnum.MagnumEnvironment;

import java.util.List;


public interface ContainerBuilder extends Builder<ContainerBuilder, Container> {
    /**
     * @see Container#getStatus
     */
    ContainerBuilder status(String status);

    /**
     * @see Container#getUuid
     */
    ContainerBuilder uuid(String uuid);

    /**
     * @see Container#getLinks
     */
    ContainerBuilder links(List<GenericLink> links);

    /**
     * @see Container#getImage
     */
    ContainerBuilder image(String image);

    /**
     * @see Container#getEnvironment
     */
    ContainerBuilder environment(MagnumEnvironment environment);

    /**
     * @see Container#getCommand
     */
    ContainerBuilder command(String command);

    /**
     * @see Container#getMemory
     */
    ContainerBuilder memory(String memory);

    /**
     * @see Container#getBayUuid
     */
    ContainerBuilder bayUuid(String bayUuid);

    /**
     * @see Container#getName
     */
    ContainerBuilder name(String name);

}
