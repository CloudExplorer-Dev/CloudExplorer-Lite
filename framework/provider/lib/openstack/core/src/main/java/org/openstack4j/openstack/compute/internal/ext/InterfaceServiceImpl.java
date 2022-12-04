package org.openstack4j.openstack.compute.internal.ext;

import org.openstack4j.api.compute.ext.InterfaceService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.InterfaceAttachment;
import org.openstack4j.openstack.compute.domain.NovaInterfaceAttachment;
import org.openstack4j.openstack.compute.domain.NovaInterfaceAttachment.NovaInterfaceAttachments;
import org.openstack4j.openstack.compute.internal.BaseComputeServices;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * API to Create, list, get details for, and delete port interfaces on a Server Instance
 *
 * @author Jeremy Unruh
 */
public class InterfaceServiceImpl extends BaseComputeServices implements InterfaceService {

    @Override
    public InterfaceAttachment create(String serverId, String portId) {
        checkNotNull(serverId, "serverId");
        checkNotNull(portId, "portId");
        return post(NovaInterfaceAttachment.class, uri("/servers/%s/os-interface", serverId))
                .entity(new NovaInterfaceAttachment(portId))
                .execute();
    }

    @Override
    public List<? extends InterfaceAttachment> list(String serverId) {
        checkNotNull(serverId, "serverId");
        return get(NovaInterfaceAttachments.class, uri("/servers/%s/os-interface", serverId))
                .execute().getList();
    }

    @Override
    public InterfaceAttachment get(String serverId, String attachmentId) {
        checkNotNull(serverId, "serverId");
        checkNotNull(attachmentId, "attachmentId");
        return get(NovaInterfaceAttachment.class, uri("/servers/%s/os-interface/%s", serverId, attachmentId)).execute();
    }

    @Override
    public ActionResponse detach(String serverId, String attachmentId) {
        checkNotNull(serverId, "serverId");
        checkNotNull(attachmentId, "attachmentId");
        return delete(ActionResponse.class, uri("/servers/%s/os-interface/%s", serverId, attachmentId)).execute();
    }

}
