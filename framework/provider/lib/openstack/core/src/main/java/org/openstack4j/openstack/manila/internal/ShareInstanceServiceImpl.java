package org.openstack4j.openstack.manila.internal;

import org.openstack4j.api.manila.ShareInstanceService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.manila.ShareInstance;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.manila.domain.ManilaShareInstance;
import org.openstack4j.openstack.manila.domain.actions.ShareInstanceActions;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShareInstanceServiceImpl extends BaseShareServices implements ShareInstanceService {
    @Override
    public List<? extends ShareInstance> list() {
        return get(ManilaShareInstance.ShareInstances.class, uri("/share_instances"))
                .execute()
                .getList();
    }

    @Override
    public ShareInstance get(String shareInstanceId) {
        checkNotNull(shareInstanceId);
        return get(ManilaShareInstance.class, uri("/share_instances/%s", shareInstanceId)).execute();
    }

    @Override
    public ActionResponse resetState(String shareInstanceId, ShareInstance.Status status) {
        checkNotNull(shareInstanceId);
        checkNotNull(status);

        return ToActionResponseFunction.INSTANCE.apply(
                post(Void.class, uri("/share_instances/%s/action", shareInstanceId))
                        .entity(ShareInstanceActions.resetState(status))
                        .executeWithResponse());
    }

    @Override
    public ActionResponse forceDelete(String shareInstanceId) {
        checkNotNull(shareInstanceId);

        return ToActionResponseFunction.INSTANCE.apply(
                post(Void.class, uri("/share_instances/%s/action", shareInstanceId))
                        .entity(ShareInstanceActions.forceDelete())
                        .executeWithResponse());
    }
}
