package org.openstack4j.openstack.image.domain.functions;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.model.image.Image;
import org.openstack4j.openstack.image.domain.ImageHeader;

import javax.annotation.Nullable;
import java.util.Map;

import static org.openstack4j.openstack.image.domain.ImageHeader.*;

/**
 * A function which takes an Image and applies the values to headers. The function only applies values that are supported for Update based
 * calls
 *
 * @author Jeremy Unruh
 */
public class ImageForUpdateToHeaders implements Function<Image, Map<String, Object>> {
    private static final ImageForUpdateToHeaders instance = new ImageForUpdateToHeaders();

    private ImageForUpdateToHeaders() {
    }

    public static ImageForUpdateToHeaders instance() {
        return instance;
    }

    @Override
    @Nullable
    public Map<String, Object> apply(@Nullable Image from) {
        if (from == null) return null;

        Map<String, Object> headers = Maps.newHashMap();

        addIfNotNull(headers, ID, from.getId());
        addIfNotNull(headers, NAME, from.getName());
        addIfNotNull(headers, MIN_DISK, from.getMinDisk());
        addIfNotNull(headers, MIN_RAM, from.getMinRam());
        addIfNotNull(headers, OWNER, from.getOwner());
        addIfNotNull(headers, IS_PUBLIC, from.isPublic());
        addIfNotNull(headers, PROTECTED, from.isProtected());
        addIfNotNull(headers, CHECKSUM, from.getChecksum());
        addIfNotNull(headers, SIZE, from.getSize());
        addIfNotNull(headers, STORE, from.getStoreType());
        addIfNotNull(headers, COPY_FROM, from.getCopyFrom(), false);

        if (from.getContainerFormat() != ContainerFormat.UNRECOGNIZED)
            headers.put(CONTAINER_FORMAT.asHeader(), from.getContainerFormat().value());

        if (from.getDiskFormat() != DiskFormat.UNRECOGNIZED)
            headers.put(DISK_FORMAT.asHeader(), from.getDiskFormat().value());


        if (from.getProperties() != null) {
            for (String k : from.getProperties().keySet()) {
                headers.put(PROPERTY.asHeader() + "-" + k, from.getProperties().get(k));
            }
        }
        return headers;
    }

    private void addIfNotNull(Map<String, Object> headers, ImageHeader header, Object value) {
        addIfNotNull(headers, header, value, true);
    }

    private void addIfNotNull(Map<String, Object> headers, ImageHeader header, Object value, boolean asHeader) {
        if (value == null) return;

        if (asHeader) {
            headers.put(header.asHeader(), value);
        } else {
            headers.put(header.asGlanceHeader(), value);
        }
    }

}
