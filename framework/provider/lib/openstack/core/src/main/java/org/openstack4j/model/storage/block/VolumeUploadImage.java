package org.openstack4j.model.storage.block;

import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.model.storage.block.Volume.Status;

import java.util.Date;

/**
 * Represents an action state when uploading a volume to the image service
 *
 * @author Jeremy Unruh
 */
public interface VolumeUploadImage extends ModelEntity {

    /**
     * @return the identifier for this transactions
     */
    String getId();

    /**
     * @return the display description
     */
    String getDisplayDescription();

    /**
     * @return the last update date
     */
    Date getUpdatedAt();

    /**
     * @return the current status
     */
    Status getStatus();

    /**
     * @return the assigned image identifier
     */
    String getImageId();

    /**
     * @return the image name
     */
    String getImageName();

    /**
     * @return the container format
     */
    ContainerFormat getContainerFormat();

    /**
     * @return the overall size
     */
    int getSize();

    /**
     * @return the disk format
     */
    DiskFormat getDiskFormat();


}
