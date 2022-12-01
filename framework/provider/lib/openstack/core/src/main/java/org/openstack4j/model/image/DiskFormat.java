package org.openstack4j.model.image;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * The disk format of a virtual machine image is the format of the underlying disk image
 *
 * @author Jeremy Unruh
 */
public enum DiskFormat {
    /**
     * This is an unstructured disk image format
     */
    RAW,
    /**
     * This is the VHD disk format, a common disk format used by virtual machine monitors from
     * VMWare, Xen, Microsoft, VirtualBox, and others
     */
    VHD,
    /**
     * Another common disk format supported by many common virtual machine monitors
     */
    VMDK,
    /**
     * A disk format supported by VirtualBox virtual machine monitor and the QEMU emulator
     */
    VDI,
    /**
     * An archive format for the data contents of an optical disc (e.g. CDROM).
     */
    ISO,
    /**
     * A disk format supported by the QEMU emulator that can expand dynamically and supports Copy on
     * Write
     */
    QCOW2,

    /**
     * This indicates what is stored in Glance is an Amazon kernel image
     */
    AKI,

    /**
     * This indicates what is stored in Glance is an Amazon ramdisk image
     */
    ARI,

    /**
     * This indicates what is stored in Glance is an Amazon machine image
     */
    AMI,

    /**
     * Type unknown
     */
    UNRECOGNIZED;

    @JsonCreator
    public static DiskFormat value(String df) {
        if (df == null || df.isEmpty()) return UNRECOGNIZED;
        try {
            return valueOf(df.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNRECOGNIZED;
        }
    }

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }
}
