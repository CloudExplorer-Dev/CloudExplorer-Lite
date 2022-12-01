package org.openstack4j.openstack.compute.internal;

import org.openstack4j.api.compute.ServerTagService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.openstack.compute.domain.NovaServerTag;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class ServerTagServiceImpl extends BaseComputeServices implements ServerTagService {

    private static final Logger LOG = LoggerFactory.getLogger(ServerTagServiceImpl.class);

    @Override
    public NovaServerTag list(String serverId) {
        checkNotNull(serverId);
        return this.get(NovaServerTag.class, this.uri("/servers/%s/tags", serverId)).execute();
    }

    @Override
    public NovaServerTag replace(String serverId, NovaServerTag tags) {
        checkNotNull(serverId);
        checkNotNull(tags);
        return this.put(NovaServerTag.class, this.uri("/servers/%s/tags", serverId)).entity(tags).execute();
    }

    @Override
    public ActionResponse deleteAll(String serverId) {
        checkNotNull(serverId);
        return ToActionResponseFunction.INSTANCE.apply(
                this.delete(Void.class, this.uri("/servers/%s/tags", serverId)).executeWithResponse());
    }

    @Override
    public ActionResponse delete(String serverId, String tag) {
        checkNotNull(serverId);
        checkNotNull(tag);
        return ToActionResponseFunction.INSTANCE.apply(
                this.delete(Void.class, this.uri("/servers/%s/tags/%s", serverId, tag)).executeWithResponse());
    }

    @Override
    public ActionResponse check(String serverId, String tag) {
        checkNotNull(serverId);
        checkNotNull(tag);
        return ToActionResponseFunction.INSTANCE.apply(
                this.get(Void.class, this.uri("/servers/%s/tags/%s", serverId, tag)).executeWithResponse());
    }

    @Override
    public ActionResponse addSingle(String serverId, String tag) {
        checkNotNull(serverId);
        checkNotNull(tag);
        return ToActionResponseFunction.INSTANCE.apply(
                this.put(ActionResponse.class, this.uri("/servers/%s/tags/%s", serverId, tag)).executeWithResponse());
    }

}
