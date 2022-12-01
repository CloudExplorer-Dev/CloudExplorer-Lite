package org.openstack4j.openstack.image.domain;

/**
 * Used to decode and encode header values into corresponding image mutators
 *
 * @author Jeremy Unruh
 */
public enum ImageHeader {
    ID, NAME, CHECKSUM, MIN_DISK, MIN_RAM, IS_PUBLIC, PROTECTED, CREATED_AT, UPDATED_AT, DELETED_AT, COPY_FROM,
    OWNER, LOCATION, STATUS, DISK_FORMAT, CONTAINER_FORMAT, SIZE, SIZE_MIN, SIZE_MAX, STORE, PROPERTY, DELETED;

    public static final String HEADER_PREFIX = "X-Image-Meta-";
    public static final String GLANCE_HEADER_PREFIX = "x-glance-api-";

    public String asParam() {
        return name().toLowerCase();
    }

    public String asHeader() {
        return HEADER_PREFIX + name().charAt(0) + name().substring(1).toLowerCase();
    }

    public String asGlanceHeader() {
        return GLANCE_HEADER_PREFIX + name().charAt(0) + name().substring(1).toLowerCase();
    }

}
