package org.openstack4j.model.tacker.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.tacker.Vnfd;
import org.openstack4j.openstack.tacker.domain.VnfdAttributes;
import org.openstack4j.openstack.tacker.domain.VnfdServiceTypes;

import java.util.List;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 11, 2016
 */
public interface VnfdBuilder extends Builder<VnfdBuilder, Vnfd> {

    /**
     * @param tenantId : Owner of the Vnfd. Only an administrative user can specify a tenant ID other than its own.
     * @return VnfdBuilder
     */
    VnfdBuilder tenantId(String tenantId);

    /**
     * @param name : Human readable name for the Vnfd (255 characters limit). Does not have to be unique.
     * @return VnfdBuilder
     */
    VnfdBuilder name(String name);

    /**
     * @param description : Human readable description for the Vnfd (1024 characters limit).
     * @return VnfdBuilder
     */
    VnfdBuilder description(String description);

    /**
     * @return VnfdBuilder
     */
    VnfdBuilder managementDriver(String managementDriver);

    /**
     * @return VnfdBuilder
     */
    VnfdBuilder infrastructureDriver(String infrastructureDriver);

    /**
     * @return VnfdBuilder
     */
    VnfdBuilder attributes(VnfdAttributes attributes);

    /**
     * @return VnfdBuilder
     */
    VnfdBuilder serviceTypes(List<VnfdServiceTypes> serviceTypes);

}
