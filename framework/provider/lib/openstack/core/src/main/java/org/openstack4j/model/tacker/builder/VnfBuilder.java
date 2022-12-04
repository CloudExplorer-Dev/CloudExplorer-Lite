package org.openstack4j.model.tacker.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.tacker.Vnf;
import org.openstack4j.openstack.tacker.domain.VnfAttributes;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 11, 2016
 */
public interface VnfBuilder extends Builder<VnfBuilder, Vnf> {

    /**
     * @param tenantId : Owner of the Vnf. Only an administrative user can specify a tenant ID other than its own.
     * @return VnfBuilder
     */
    VnfBuilder tenantId(String tenantId);

    /**
     * @param name : Human readable name for the Vnf (255 characters limit). Does not have to be unique.
     * @return VnfBuilder
     */
    VnfBuilder name(String name);

    /**
     * @param description : Human readable description for the Vnf (1024 characters limit).
     * @return VnfBuilder
     */
    VnfBuilder description(String description);

    /**
     * @return VnfBuilder
     */
    VnfBuilder attributes(VnfAttributes attributes);

    /**
     * @return VnfBuilder
     */
    VnfBuilder vnfdId(String vnfdId);

    /**
     * @return VnfBuilder
     */
    VnfBuilder vimId(String vimId);

}
