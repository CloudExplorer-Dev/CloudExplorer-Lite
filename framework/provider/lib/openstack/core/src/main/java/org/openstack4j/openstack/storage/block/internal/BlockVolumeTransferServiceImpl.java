package org.openstack4j.openstack.storage.block.internal;

import org.openstack4j.api.storage.BlockVolumeTransferService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.storage.block.VolumeTransfer;
import org.openstack4j.openstack.storage.block.domain.CinderVolumeTransfer;
import org.openstack4j.openstack.storage.block.domain.CinderVolumeTransfer.VolumeTransferList;
import org.openstack4j.openstack.storage.block.domain.CinderVolumeTransferAccept;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Related services for managing volume transfers (os-volume-transfer)
 *
 * @author Jeremy Unruh
 */
public class BlockVolumeTransferServiceImpl extends BaseBlockStorageServices implements BlockVolumeTransferService {

    @Override
    public List<? extends VolumeTransfer> list() {
        return list(Boolean.TRUE);
    }

    @Override
    public List<? extends VolumeTransfer> list(boolean detailed) {
        String url = (detailed) ? "/os-volume-transfer/detail" : "/os-volume-transfer";
        return get(VolumeTransferList.class, url).execute().getList();
    }

    @Override
    public VolumeTransfer get(String transferId) {
        checkNotNull(transferId, "TransferId must contain a value");
        return get(CinderVolumeTransfer.class, uri("/os-volume-transfer/%s", transferId)).execute();
    }

    @Override
    public VolumeTransfer create(String volumeId) {
        return create(volumeId, null);
    }

    @Override
    public VolumeTransfer create(String volumeId, String name) {
        checkNotNull(volumeId, "VolumeId must contain a value");
        return post(CinderVolumeTransfer.class, "/os-volume-transfer").entity(CinderVolumeTransfer.create(volumeId, name)).execute();
    }

    @Override
    public VolumeTransfer accept(String transferId, String authKey) {
        checkNotNull(transferId, "TransferId must contain a value");
        checkNotNull(authKey, "AuthKey must contain a value");
        return post(CinderVolumeTransfer.class, uri("/os-volume-transfer/%s/accept", transferId))
                .entity(CinderVolumeTransferAccept.create(authKey)).execute();
    }

    @Override
    public ActionResponse delete(String transferId) {
        checkNotNull(transferId, "TransferId must contain a value");
        return delete(ActionResponse.class, uri("/os-volume-transfer/%s", transferId)).execute();
    }
}
