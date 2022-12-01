package org.openstack4j.model.barbican;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.barbican.builder.ContainerSecretBuilder;

/**
 * A secret associated to a container.
 */
public interface ContainerSecret extends ModelEntity, Buildable<ContainerSecretBuilder> {
    /**
     * @return the name of the secret.
     */
    String getName();

    /**
     * @return Full URI reference to the secret.
     */
    String getReference();
}
