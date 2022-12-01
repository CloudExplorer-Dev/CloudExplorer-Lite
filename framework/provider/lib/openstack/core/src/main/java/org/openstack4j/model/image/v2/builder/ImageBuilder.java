package org.openstack4j.model.image.v2.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.image.v2.ContainerFormat;
import org.openstack4j.model.image.v2.DiskFormat;
import org.openstack4j.model.image.v2.Image;

import java.util.List;

/**
 * Builder which creates a v2 Image
 *
 * @author emjburns
 */
public interface ImageBuilder extends Buildable.Builder<ImageBuilder, Image> {

    /**
     * @see Image#getName()
     */
    ImageBuilder name(String name);

    /**
     * @see Image#getId()
     */
    ImageBuilder id(String id);

    /**
     * @see Image#getVisibility()
     */
    ImageBuilder visibility(Image.ImageVisibility visibility);

    /**
     * @see Image#getTags()
     */
    ImageBuilder tags(List<String> tags);

    /**
     * @see Image#getContainerFormat()
     */
    ImageBuilder containerFormat(ContainerFormat containerFormat);

    /**
     * @see Image#getDiskFormat()
     */
    ImageBuilder diskFormat(DiskFormat diskFormat);

    /**
     * @see Image#getMinDisk()
     */
    ImageBuilder minDisk(Long minDisk);

    /**
     * @see Image#getMinRam()
     */
    ImageBuilder minRam(Long minRam);

    /**
     * @see Image#getIsProtected()
     */
    ImageBuilder isProtected(Boolean isProtected);

    /**
     * @see Image#getArchitecture()
     */
    ImageBuilder architecture(String architecture);

    /**
     * @see Image#getInstanceUuid()
     */
    ImageBuilder instanceUuid(String instanceUuid);

    /**
     * @see Image#getKernelId()
     */
    ImageBuilder kernelId(String kernelId);

    /**
     * @see Image#getOsVersion()
     */
    ImageBuilder osVersion(String osVersion);

    /**
     * @see Image#getOsDistro()
     */
    ImageBuilder osDistro(String osDistro);

    /**
     * @see Image#getRamdiskId()
     */
    ImageBuilder ramdiskId(String ramdiskId);

    /**
     * @see Image#getAdditionalPropertyValue()
     */
    ImageBuilder additionalProperty(String key, String value);
}
