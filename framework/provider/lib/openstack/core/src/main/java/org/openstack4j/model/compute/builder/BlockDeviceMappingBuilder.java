package org.openstack4j.model.compute.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.compute.BDMDestType;
import org.openstack4j.model.compute.BDMSourceType;
import org.openstack4j.model.compute.BlockDeviceMappingCreate;

/**
 * Block Device Mapping.
 *
 * @author jaroslav.sovicka@oracle.com
 */
public interface BlockDeviceMappingBuilder extends Buildable.Builder<BlockDeviceMappingBuilder, BlockDeviceMappingCreate> {

    /**
     * Set device boot index.
     *
     * @param i The boot index.
     * @return BlockDeviceMappingBuilder
     */
    BlockDeviceMappingBuilder bootIndex(int i);

    /**
     * A device name where the volume is attached in the system at <tt>/dev/dev_name</tt>. This value is typically <tt>vda</tt>.
     *
     * @param deviceName the device name
     * @return BlockDeviceMappingBuilder
     */
    BlockDeviceMappingBuilder deviceName(String deviceName);

    /**
     * The device ID.
     *
     * @param id the device id
     * @return BlockDeviceMappingBuilder
     */
    BlockDeviceMappingBuilder uuid(String id);

    /**
     * Defines where the volume comes from.
     *
     * @param type the destination type
     * @return BlockDeviceMappingBuilder
     */
    BlockDeviceMappingBuilder destinationType(BDMDestType type);

    /**
     * The source type of the volume.
     *
     * @param type the source type
     * @return BlockDeviceMappingBuilder
     */
    BlockDeviceMappingBuilder sourceType(BDMSourceType type);

    /**
     * Delete the volume at termination.
     *
     * @param deleteOnTermination Set to True to delete the volume when the instance is deleted. Set to False to retain the volume when the instance is deleted.
     * @return BlockDeviceMappingBuilder
     */
    BlockDeviceMappingBuilder deleteOnTermination(boolean deleteOnTermination);

    /**
     * Set to create a volume from a snapshot id.
     *
     * @return BlockDeviceMappingBuilder
     */
    BlockDeviceMappingBuilder snapshotId(String snapshotId);

    /**
     * Set to create a volume from a volume id.
     *
     * @return BlockDeviceMappingBuilder
     */
    BlockDeviceMappingBuilder volumeId(String volumeId);

    /**
     * Used to set the volume size of the destination volume (typically needed when source type is image).
     *
     * @param size the size of the volume in Gigabytes.
     * @return BlockDeviceMappingBuilder
     */
    BlockDeviceMappingBuilder volumeSize(Integer size);

    /**
     * Used to set the disk_bus, low level detail that some hypervisors
     * (currently only libvirt) may support.
     *
     * @param diskBus type, e.g ide, usb, virtio, scsi
     * @return BlockDeviceMappingBuilder
     */
    BlockDeviceMappingBuilder diskBus(String diskBus);

    /**
     * Used to set the device_type, low level detail that some hypervisors
     * (currently only libvirt) may support.
     *
     * @param deviceType, disk, cdrom, floppy, lun
     * @return BlockDeviceMappingBuilder
     */
    BlockDeviceMappingBuilder deviceType(String deviceType);
}
