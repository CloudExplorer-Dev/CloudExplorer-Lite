package org.openstack4j.openstack.manila.internal;

import org.openstack4j.api.manila.ShareSnapshotService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.manila.ShareSnapshot;
import org.openstack4j.model.manila.ShareSnapshotCreate;
import org.openstack4j.model.manila.ShareSnapshotUpdateOptions;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.manila.domain.ManilaShareSnapshot;
import org.openstack4j.openstack.manila.domain.ManilaShareSnapshotUpdate;
import org.openstack4j.openstack.manila.domain.actions.ShareSnapshotAction;
import org.openstack4j.openstack.manila.domain.actions.ShareSnapshotActions;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShareSnapshotServiceImpl extends BaseShareServices implements ShareSnapshotService {
    @Override
    public ShareSnapshot create(ShareSnapshotCreate snapshotCreate) {
        checkNotNull(snapshotCreate);
        return post(ManilaShareSnapshot.class, uri("/snapshots"))
                .entity(snapshotCreate)
                .execute();
    }

    @Override
    public List<? extends ShareSnapshot> list() {
        return list(false);
    }

    @Override
    public List<? extends ShareSnapshot> listDetails() {
        return list(true);
    }

    private List<? extends ShareSnapshot> list(boolean detail) {
        return get(ManilaShareSnapshot.ShareSnapshots.class, uri("/snapshots" + (detail ? "/detail" : "")))
                .execute()
                .getList();
    }

    @Override
    public ShareSnapshot get(String snapshotId) {
        checkNotNull(snapshotId);
        return get(ManilaShareSnapshot.class, uri("/snapshots/%s", snapshotId)).execute();
    }

    @Override
    public ShareSnapshot update(String snapshotId, ShareSnapshotUpdateOptions snapshotUpdateOptions) {
        checkNotNull(snapshotId);
        checkNotNull(snapshotUpdateOptions);

        return put(ManilaShareSnapshot.class, uri("/snapshots/%s", snapshotId))
                .entity(ManilaShareSnapshotUpdate.fromOptions(snapshotUpdateOptions))
                .execute();
    }

    @Override
    public ActionResponse delete(String snapshotId) {
        checkNotNull(snapshotId);
        return ToActionResponseFunction.INSTANCE.apply(
                delete(Void.class, uri("/snapshots/%s", snapshotId)).executeWithResponse());
    }

    @Override
    public ActionResponse resetState(String snapshotId, ShareSnapshot.Status status) {
        checkNotNull(snapshotId);
        checkNotNull(status);

        return invokeAction(snapshotId, ShareSnapshotActions.resetState(status));
    }

    @Override
    public ActionResponse forceDelete(String snapshotId) {
        checkNotNull(snapshotId);
        return invokeAction(snapshotId, ShareSnapshotActions.forceDelete());
    }

    /**
     * Invoke the action on the given snapshot.
     *
     * @param snapshotId the snapshot ID
     * @param action     the action to invoke
     * @return the action response of the server
     */
    private ActionResponse invokeAction(String snapshotId, ShareSnapshotAction action) {
        return ToActionResponseFunction.INSTANCE.apply(
                post(Void.class, uri("/snapshots/%s/action", snapshotId))
                        .entity(action)
                        .executeWithResponse());
    }
}
