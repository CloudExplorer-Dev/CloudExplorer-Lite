package org.openstack4j.openstack.manila.builder;

import org.openstack4j.model.manila.builder.*;
import org.openstack4j.openstack.manila.domain.*;

/**
 * The Share File System Builders
 */
public class ManilaBuilders implements SharedFileSystemBuilders {

    @Override
    public SecurityServiceCreateBuilder securityService() {
        return ManilaSecurityServiceCreate.builder();
    }

    @Override
    public ShareNetworkCreateBuilder shareNetwork() {
        return ManilaShareNetworkCreate.builder();
    }

    @Override
    public ShareCreateBuilder share() {
        return ManilaShareCreate.builder();
    }

    @Override
    public ShareTypeCreateBuilder shareType() {
        return ManilaShareTypeCreate.builder();
    }

    @Override
    public ShareSnapshotCreateBuilder shareSnapshot() {
        return ManilaShareSnapshotCreate.builder();
    }

    @Override
    public ShareManageBuilder shareManage() {
        return ManilaShareManage.builder();
    }
}
