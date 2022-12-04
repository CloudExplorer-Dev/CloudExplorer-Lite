package org.openstack4j.model.tacker;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.tacker.builder.VnfUpdateBuilder;
import org.openstack4j.openstack.tacker.domain.VnfUpdateAttributes;

/**
 * A Builder to Update Firewall of FwaaS
 *
 * @author Vishvesh Deshmukh
 */
public interface VnfUpdate extends ModelEntity, Buildable<VnfUpdateBuilder> {

    /**
     * @return attributes : VnfUpdateAttributes
     */
    VnfUpdateAttributes getAttributes();
}
