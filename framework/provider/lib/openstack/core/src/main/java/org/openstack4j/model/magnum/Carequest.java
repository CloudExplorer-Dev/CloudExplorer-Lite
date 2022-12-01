package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;

public interface Carequest extends ModelEntity, Buildable<CarequestBuilder> {
    /**
     * Gets bayUuid
     *
     * @return bayUuid
     */
    String getBayUuid();

    /**
     * Gets csr
     *
     * @return csr
     */
    String getCsr();

}
