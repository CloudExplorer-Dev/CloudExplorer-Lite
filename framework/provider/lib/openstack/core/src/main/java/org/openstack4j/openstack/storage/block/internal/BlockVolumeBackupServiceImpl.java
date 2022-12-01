package org.openstack4j.openstack.storage.block.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.api.storage.BlockVolumeBackupService;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.storage.block.VolumeBackup;
import org.openstack4j.model.storage.block.VolumeBackupCreate;
import org.openstack4j.model.storage.block.VolumeBackupRestore;
import org.openstack4j.openstack.storage.block.domain.CinderVolumeBackup;
import org.openstack4j.openstack.storage.block.domain.CinderVolumeBackup.VolumeBackups;
import org.openstack4j.openstack.storage.block.domain.CinderVolumeBackupRestore;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * OpenStack (Cinder) Volume Backup Operations API Implementation.
 *
 * @author Huang Yi
 */
public class BlockVolumeBackupServiceImpl extends BaseBlockStorageServices implements BlockVolumeBackupService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends VolumeBackup> list() {
        return get(VolumeBackups.class, uri("/backups/detail")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends VolumeBackup> list(Map<String, String> filteringParams) {
        Invocation<VolumeBackups> invocation = buildInvocation(filteringParams);
        return invocation.execute().getList();
    }

    private Invocation<VolumeBackups> buildInvocation(Map<String, String> filteringParams) {
        Invocation<VolumeBackups> invocation = get(VolumeBackups.class, "/backups/detail");
        if (filteringParams == null) {
            return invocation;
        } else {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                invocation = invocation.param(entry.getKey(), entry.getValue());
            }
        }
        return invocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VolumeBackup get(String backupId) {
        checkNotNull(backupId);
        return get(CinderVolumeBackup.class, uri("/backups/%s", backupId)).execute();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String backupId) {
        checkNotNull(backupId);
        return deleteWithResponse(uri("/backups/%s", backupId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VolumeBackup create(VolumeBackupCreate vbc) {
        checkNotNull(vbc);
        checkNotNull(vbc.getVolumeId());
        return post(CinderVolumeBackup.class, uri("/backups")).entity(vbc).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VolumeBackupRestore restore(String backupId, String name, String volumeId) {
        _VolumeBackupRestore entity = new _VolumeBackupRestore(name, volumeId);
        return post(CinderVolumeBackupRestore.class, uri("/backups/%s/restore", backupId)).entity(entity).execute();
    }

    @JsonRootName("restore")
    private static class _VolumeBackupRestore implements ModelEntity {

        private static final long serialVersionUID = 1L;
        @JsonProperty("name")
        private String name;
        @JsonProperty("volume_id")
        private String volumeId;

        public _VolumeBackupRestore(String name, String volumeId) {
            this.name = name;
            this.volumeId = volumeId;
        }
    }

}
