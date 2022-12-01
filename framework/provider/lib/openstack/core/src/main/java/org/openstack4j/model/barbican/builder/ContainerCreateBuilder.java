package org.openstack4j.model.barbican.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.barbican.Container;
import org.openstack4j.model.barbican.ContainerSecret;

import java.util.List;

public interface ContainerCreateBuilder extends Buildable.Builder<ContainerCreateBuilder, Container> {
    /**
     * Optional.
     *
     * @param name Human readable name for identifying your container.
     */
    ContainerCreateBuilder name(String name);

    /**
     * Required.
     *
     * @param type Type of container. Options: generic, rsa, certificate
     */
    ContainerCreateBuilder type(String type);

    /**
     * Required.
     *
     * @param references A list of dictionaries containing references to secrets
     */
    ContainerCreateBuilder secretReferences(List<? extends ContainerSecret> references);
}
