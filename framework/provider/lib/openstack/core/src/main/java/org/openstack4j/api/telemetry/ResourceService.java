package org.openstack4j.api.telemetry;

import org.openstack4j.model.telemetry.Resource;

import java.util.List;

public interface ResourceService {

    List<? extends Resource> list();

    Resource get(String resourceId);

}
