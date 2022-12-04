package org.openstack4j.openstack.compute.internal.ext;

import org.openstack4j.api.compute.ext.ZoneService;
import org.openstack4j.model.compute.ext.AvailabilityZone;
import org.openstack4j.openstack.compute.domain.ext.ExtAvailabilityZone.AvailabilityZones;
import org.openstack4j.openstack.compute.internal.BaseComputeServices;

import java.util.List;

/**
 * Service implementation for ZoneService API
 */
public class ZoneServiceImpl extends BaseComputeServices implements ZoneService {

    @Override
    public List<? extends AvailabilityZone> list() {
        return list(Boolean.FALSE);
    }

    @Override
    public List<? extends AvailabilityZone> list(boolean detailed) {
        String uri = (detailed) ? "/os-availability-zone/detail" : "/os-availability-zone";
        return get(AvailabilityZones.class, uri).execute().getList();
    }
}
