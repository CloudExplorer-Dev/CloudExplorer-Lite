package org.openstack4j.model.barbican;

import org.openstack4j.model.ModelEntity;

/**
 * Consumer associated with a container (i.e. lbaas).
 */
public interface ContainerConsumer extends ModelEntity {
    /**
     * @return name of the consumer.
     */
    String getName();

    /**
     * @return Full url to the consumer.
     */
    String getUrl();
}
