package org.openstack4j.openstack.compute.internal.ext;

import org.openstack4j.api.compute.ext.InstanceActionsService;
import org.openstack4j.model.compute.InstanceAction;
import org.openstack4j.openstack.compute.domain.NovaInstanceAction;
import org.openstack4j.openstack.compute.domain.NovaInstanceAction.NovaInstanceActions;
import org.openstack4j.openstack.compute.internal.BaseComputeServices;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * API to list executed instance actions.
 *
 * @author Christian Banse
 */
public class InstanceActionsServiceImpl extends BaseComputeServices implements InstanceActionsService {

    @Override
    public List<? extends InstanceAction> list(String serverId) {
        checkNotNull(serverId, "serverId");
        return get(NovaInstanceActions.class, uri("/servers/%s/os-instance-actions", serverId)).execute().getList();
    }

    @Override
    public InstanceAction get(String serverId, String requestId) {
        checkNotNull(serverId, "serverId");
        checkNotNull(requestId, "requestId");
        return get(NovaInstanceAction.class, uri("/servers/%s/os-instance-actions/%s", serverId, requestId)).execute();
    }

}
