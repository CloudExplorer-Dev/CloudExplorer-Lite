package org.openstack4j.openstack.storage.block.internal;

import org.openstack4j.api.Builders;
import org.openstack4j.api.storage.BlockVolumeSnapshotService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.storage.block.VolumeSnapshot;
import org.openstack4j.openstack.storage.block.domain.CinderVolumeSnapshot;
import org.openstack4j.openstack.storage.block.domain.CinderVolumeSnapshot.VolumeSnapshots;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * OpenStack (Cinder) Volume Snapshot Operations API Implementation.
 *
 * @author Jeremy Unruh
 */
public class BlockVolumeSnapshotServiceImpl extends BaseBlockStorageServices implements BlockVolumeSnapshotService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends VolumeSnapshot> list() {
        return get(VolumeSnapshots.class, uri("/snapshots")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends VolumeSnapshot> list(Map<String, String> filteringParams) {
        Invocation<VolumeSnapshots> volumeInvocation = buildInvocation(filteringParams);
        return volumeInvocation.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VolumeSnapshot get(String snapshotId) {
        checkNotNull(snapshotId);
        return get(CinderVolumeSnapshot.class, uri("/snapshots/%s", snapshotId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String snapshotId) {
        checkNotNull(snapshotId);
        return deleteWithResponse(uri("/snapshots/%s", snapshotId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse update(String snapshotId, String name, String description) {
        checkNotNull(snapshotId);
        if (name == null && description == null)
            return ActionResponse.actionFailed("Both Name and Description are required", 412);

        return put(ActionResponse.class, uri("/snapshots/%s", snapshotId))
                .entity(Builders.volumeSnapshot().name(name).description(description).build())
                .execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VolumeSnapshot create(VolumeSnapshot snapshot) {
        checkNotNull(snapshot);
        checkNotNull(snapshot.getVolumeId());
        return post(CinderVolumeSnapshot.class, uri("/snapshots")).entity(snapshot).execute();
    }

    private Invocation<VolumeSnapshots> buildInvocation(Map<String, String> filteringParams) {
        Invocation<VolumeSnapshots> volumeInvocation = get(VolumeSnapshots.class, "/snapshots");
        if (filteringParams == null) {
            return volumeInvocation;
        } else {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                volumeInvocation = volumeInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return volumeInvocation;
    }

}
