package org.openstack4j.model.tacker.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.tacker.VnfUpdate;
import org.openstack4j.openstack.tacker.domain.VnfUpdateAttributes;

/**
 * A Builder to Update Vnf
 *
 * @author Vishvesh Deshmukh
 */
public interface VnfUpdateBuilder extends Builder<VnfUpdateBuilder, VnfUpdate> {

    /**
     * @param attributes : VnfUpdateAttributes
     * @return VnfUpdateBuilder
     */
    public VnfUpdateBuilder attributes(VnfUpdateAttributes attributes);
}
