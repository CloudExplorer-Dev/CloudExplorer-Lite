package org.openstack4j.model.barbican.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.barbican.ContainerSecret;

public interface ContainerSecretBuilder extends Buildable.Builder<ContainerSecretBuilder, ContainerSecret> {
    /**
     * @param name Human readable name for identifying your secret within the container.
     */
    ContainerSecretBuilder name(String name);

    /**
     * @param type Full URI reference to an existing secret.
     */
    ContainerSecretBuilder reference(String type);
}
