package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.model.storage.block.options.UploadImageData;

/**
 * REST Entity for uploading a Volume to the Image Service
 *
 * @author Jeremy Unruh
 */
@JsonRootName("os-volume_upload_image")
public class CinderUploadImageData implements ModelEntity {

    private static final long serialVersionUID = 1L;
    @JsonProperty("image_name")
    private String imageName;
    @JsonProperty("container_format")
    private ContainerFormat containerFormat;
    @JsonProperty("disk_format")
    private DiskFormat diskFormat;
    @JsonProperty("force")
    private boolean force;

    public CinderUploadImageData() {
    }

    public static CinderUploadImageData create(UploadImageData data) {
        CinderUploadImageData ret = new CinderUploadImageData();
        ret.imageName = data.getImageName();
        ret.containerFormat = data.getContainerFormat();
        ret.diskFormat = data.getDiskFormat();
        ret.force = data.isForce();
        return ret;
    }

}
