package org.openstack4j.model.image.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.image.StoreType;

import java.util.Map;

/**
 * Builder which creates an Image
 *
 * @author Jeremy Unruh
 */
public interface ImageBuilder extends Builder<ImageBuilder, Image> {

    /**
     * @see Image#getId()
     */
    ImageBuilder id(String id);

    /**
     * @see Image#getName()
     */
    ImageBuilder name(String name);

    /**
     * @see Image#getDiskFormat()
     */
    ImageBuilder diskFormat(DiskFormat diskFormat);

    /**
     * @see Image#getContainerFormat()
     */
    ImageBuilder containerFormat(ContainerFormat containerFormat);

    /**
     * @see Image#getSize()
     */
    ImageBuilder size(Long size);

    /**
     * @see Image#getChecksum()
     */
    ImageBuilder checksum(String checksum);

    /**
     * @see Image#getMinDisk()
     */
    ImageBuilder minDisk(Long minDisk);

    /**
     * @see Image#getMinRam()
     */
    ImageBuilder minRam(Long minRam);

    /**
     * @see Image#getOwner()
     */
    ImageBuilder owner(String owner);

    /**
     * @see Image#isPublic()
     */
    ImageBuilder isPublic(Boolean isPublic);

    /**
     * @see Image#getProperties()
     */
    ImageBuilder properties(Map<String, String> properties);

    /**
     * @see Image#getProperties()
     */
    ImageBuilder property(String key, String value);

    /**
     * Store type to be used during create or reserving of new images
     *
     * @param storeType the store type
     * @return the image builder
     */
    ImageBuilder storeType(StoreType storeType);


    /**
     * @see Image#getCopyFrom()
     */
    ImageBuilder copyFrom(String copyFrom);

}
