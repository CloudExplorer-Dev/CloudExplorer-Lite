package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable.Builder;

public interface CarequestBuilder extends Builder<CarequestBuilder, Carequest> {
    /**
     * @see Carequest#getBayUuid
     */
    CarequestBuilder bayUuid(String bayUuid);

    /**
     * @see Carequest#getCsr
     */
    CarequestBuilder csr(String csr);

}
