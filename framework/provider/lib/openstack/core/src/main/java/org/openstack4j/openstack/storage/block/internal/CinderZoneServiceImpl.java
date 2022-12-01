package org.openstack4j.openstack.storage.block.internal;

import org.openstack4j.api.storage.CinderZoneService;
import org.openstack4j.openstack.storage.block.domain.AvailabilityZone;
import org.openstack4j.openstack.storage.block.domain.ExtAvailabilityZone.AvailabilityZones;

import java.util.List;

public class CinderZoneServiceImpl extends BaseBlockStorageServices implements CinderZoneService {

    @Override
    public List<? extends AvailabilityZone> list() {
        String uri = "/os-availability-zone";
        return get(AvailabilityZones.class, uri).execute().getList();
    }


}
