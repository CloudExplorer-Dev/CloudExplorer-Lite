package org.openstack4j.openstack.compute.builder;

import org.openstack4j.model.compute.builder.*;
import org.openstack4j.openstack.compute.domain.*;

/**
 * The Compute V3 Builders
 */
public class NovaBuilders implements ComputeBuilders {

    private ComputeBuilders NovaBuilders() {
        return this;
    }

    @Override
    public ServerCreateBuilder server() {
        return NovaServerCreate.builder();
    }

    @Override
    public BlockDeviceMappingBuilder blockDeviceMapping() {
        return NovaBlockDeviceMappingCreate.builder();
    }

    @Override
    public FlavorBuilder flavor() {
        return NovaFlavor.builder();
    }

    @Override
    public FloatingIPBuilder floatingIP() {
        return NovaFloatingIP.builder();
    }

    @Override
    public QuotaSetUpdateBuilder quotaSet() {
        return NovaQuotaSetUpdate.builder();
    }
}
