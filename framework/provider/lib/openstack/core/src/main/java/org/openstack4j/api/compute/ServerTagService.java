package org.openstack4j.api.compute;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.openstack.compute.domain.NovaServerTag;

public interface ServerTagService extends RestService {

    NovaServerTag list(String serverId);

    NovaServerTag replace(String serverId, NovaServerTag tag);

    ActionResponse deleteAll(String serverId);

    ActionResponse delete(String serverId, String tag);

    ActionResponse check(String serverId, String tag);

    ActionResponse addSingle(String serverId, String tag);

}
