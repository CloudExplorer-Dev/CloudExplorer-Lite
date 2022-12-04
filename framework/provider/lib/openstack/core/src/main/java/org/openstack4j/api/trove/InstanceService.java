package org.openstack4j.api.trove;

import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.trove.Instance;
import org.openstack4j.model.trove.InstanceCreate;

import java.util.List;

public interface InstanceService {

    List<? extends Instance> list();

    Instance get(String instanceId);

    Instance create(InstanceCreate instanceCreate);

    ActionResponse delete(String id);

}
