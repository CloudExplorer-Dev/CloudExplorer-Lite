package org.openstack4j.model.storage.block.builder;


/**
 * The Storage builders
 */
public interface StorageBuilders {

    /**
     * The builder which creates a BlockQuotaSet
     *
     * @return the block quota-set builder
     */
    public BlockQuotaSetBuilder blockQuotaSet();

    /**
     * The builder to create a Block Volume
     *
     * @return the volume builder
     */
    public VolumeBuilder volume();

    /**
     * The builder to create a Block Volume Snapshot
     *
     * @return the snapshot builder
     */
    public VolumeSnapshotBuilder volumeSnapshot();

    /**
     * The builder to create a volume backup
     *
     * @return the backup creation builder
     */
    public VolumeBackupCreateBuilder volumeBackupCreate();

    /**
     * The builder to create a Volume Type Encryption
     *
     * @return the volume type encryption builder
     */
    public VolumeTypeEncryptionBuilder volumeTypeEncryption();

}
