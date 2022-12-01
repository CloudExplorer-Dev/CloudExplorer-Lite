package org.openstack4j.openstack.compute.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.core.transport.HttpResponse;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.openstack.compute.domain.actions.ServerAction;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.internal.BaseOpenStackService;

/**
 * Base class for Computer / Nova services
 *
 * @author Jeremy Unruh
 */
public class BaseComputeServices extends BaseOpenStackService {

    protected BaseComputeServices() {
        super(ServiceType.COMPUTE);
    }

    protected ActionResponse invokeAction(String serverId, ServerAction action) {
        return ToActionResponseFunction.INSTANCE.apply(invokeActionWithResponse(serverId, action), action.getClass().getName());
    }

    protected HttpResponse invokeActionWithResponse(String serverId, ServerAction action) {
        HttpResponse response = post(Void.class, uri("/servers/%s/action", serverId))
                .entity(action)
                .executeWithResponse();
        return response;
    }

}
