package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.model.storage.block.Volume.Status;
import org.openstack4j.model.storage.block.VolumeUploadImage;

import java.util.Date;

/**
 * Represents an action state when uploading a volume to the image service
 *
 * @author Jeremy Unruh
 */
@JsonRootName("os-volume_upload_image")
public class CinderVolumeUploadImage implements VolumeUploadImage {

    private static final long serialVersionUID = 1L;

    private String id;
    @JsonProperty("status")
    private Status status;
    @JsonProperty("display_description")
    private String displayDescription;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("image_id")
    private String imageId;
    @JsonProperty("image_name")
    private String imageName;
    private ContainerFormat containerFormat;
    @JsonProperty("disk_format")
    private DiskFormat diskFormat;
    private int size;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDisplayDescription() {
        return displayDescription;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getImageId() {
        return imageId;
    }

    @Override
    public String getImageName() {
        return imageName;
    }

    @Override
    public ContainerFormat getContainerFormat() {
        return containerFormat;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public DiskFormat getDiskFormat() {
        return diskFormat;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("status", status).add("display_description", displayDescription)
                .add("updatedAt", updatedAt).add("image_id", imageId).add("image_name", imageName)
                .add("container_format", containerFormat).add("disk_format", diskFormat)
                .toString();
    }
}
