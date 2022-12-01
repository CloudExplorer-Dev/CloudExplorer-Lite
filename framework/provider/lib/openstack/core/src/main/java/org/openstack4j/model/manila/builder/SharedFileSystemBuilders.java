package org.openstack4j.model.manila.builder;

/**
 * The Shared File System builders
 */
public interface SharedFileSystemBuilders {

    /**
     * The builder which creates manila security services
     *
     * @return the security service builder
     */
    public SecurityServiceCreateBuilder securityService();

    /**
     * The builder which creates manila share networks.
     *
     * @return the share network builder
     */
    public ShareNetworkCreateBuilder shareNetwork();

    /**
     * The builder which creates manila shares.
     *
     * @return the share builder
     */
    public ShareCreateBuilder share();

    /**
     * The builder which creates share types.
     *
     * @return the shae type builder
     */
    public ShareTypeCreateBuilder shareType();

    /**
     * The builder which creates manila share snapshots.
     *
     * @return the share builder
     */
    public ShareSnapshotCreateBuilder shareSnapshot();

    /**
     * The builder which creates manila share manages
     *
     * @return the share manage builder
     */
    public ShareManageBuilder shareManage();

}
