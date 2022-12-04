package org.openstack4j.openstack.image.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import org.openstack4j.model.common.builder.BasicResourceBuilder;
import org.openstack4j.model.image.ContainerFormat;
import org.openstack4j.model.image.DiskFormat;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.image.StoreType;
import org.openstack4j.model.image.builder.ImageBuilder;
import org.openstack4j.openstack.common.ListResult;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A Glance v1.1 Image model implementation
 *
 * @author Jeremy Unruh
 * @see http://docs.openstack.org/api/openstack-image-service/1.1/content/index.html
 */
@JsonRootName("image")
public class GlanceImage implements Image {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String location;

    @JsonProperty("disk_format")
    private DiskFormat diskFormat;

    @JsonProperty("container_format")
    private ContainerFormat containerFormat;
    private Long size;
    private String checksum;
    private String owner;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("deleted_at")
    private Date deletedAt;

    private Status status;

    @JsonProperty("is_public")
    private boolean isPublic;

    @JsonProperty("min_ram")
    private Long minRam;

    @JsonProperty("min_disk")
    private Long minDisk;

    @JsonProperty("deleted")
    private boolean isDeleted;

    @JsonProperty("protected")
    private boolean isProtected;

    @JsonProperty("copy_from")
    private String copyFrom;

    private Map<String, String> properties;

    private transient StoreType storeType;

    public static ImageBuilder builder() {
        return new ImageConcreteBuilder();
    }

    @Override
    public ImageBuilder toBuilder() {
        return new ImageConcreteBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainerFormat getContainerFormat() {
        return (containerFormat != null) ? containerFormat : ContainerFormat.UNRECOGNIZED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DiskFormat getDiskFormat() {
        return (diskFormat != null) ? diskFormat : DiskFormat.UNRECOGNIZED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getSize() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getChecksum() {
        return checksum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getMinDisk() {
        return (minDisk == null) ? 0L : minDisk;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getMinRam() {
        return (minRam == null) ? 0L : minRam;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocation() {
        return location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getOwner() {
        return owner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public Date getDeletedAt() {
        return deletedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isProtected() {
        return isProtected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StoreType getStoreType() {
        return storeType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String getCopyFrom() {
        return copyFrom;
    }

    /* Business Domain Method Chains for Conversion */

    public GlanceImage isProtected(Boolean isProtected) {
        if (isProtected != null)
            this.isProtected = isProtected;
        return this;
    }

    public GlanceImage isDeleted(Boolean isDeleted) {
        if (isDeleted != null)
            this.isDeleted = isDeleted;
        return this;
    }

    public GlanceImage status(Status status) {
        this.status = status;
        return this;
    }

    public GlanceImage createdAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public GlanceImage updatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public GlanceImage deletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public GlanceImage size(Long size) {
        this.size = size;
        return this;
    }

    public GlanceImage location(String location) {
        this.location = location;
        return this;
    }

    public GlanceImage properties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isSnapshot() {
        return properties != null && properties.containsKey("image_location") && "snapshot".equals(properties.get("image_location"));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("status", status).add("location", location).add("diskFormat", diskFormat)
                .add("containerFormat", containerFormat).add("size", size).add("owner", owner).add("minRam", minRam).add("minDisk", minDisk)
                .add("created", createdAt).add("updated", updatedAt).add("deleted", deletedAt).add("isPublic", isPublic)
                .add("isProtected", isProtected).add("isDeleted", isDeleted).add("propterties", properties).addValue("\n")
                .toString();
    }

    public static class Images extends ListResult<GlanceImage> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("images")
        private List<GlanceImage> images;

        @Override
        protected List<GlanceImage> value() {
            return images;
        }
    }

    public static class ImageConcreteBuilder extends BasicResourceBuilder<Image, ImageConcreteBuilder> implements ImageBuilder {

        private GlanceImage m;

        ImageConcreteBuilder() {
            this(new GlanceImage());
        }

        ImageConcreteBuilder(GlanceImage m) {
            this.m = m;
        }

        @Override
        public ImageBuilder diskFormat(DiskFormat diskFormat) {
            m.diskFormat = diskFormat;
            return this;
        }

        @Override
        public ImageBuilder containerFormat(ContainerFormat containerFormat) {
            m.containerFormat = containerFormat;
            return this;
        }

        @Override
        public ImageBuilder size(Long size) {
            m.size = size;
            return this;
        }

        @Override
        public ImageBuilder checksum(String checksum) {
            m.checksum = checksum;
            return this;
        }

        @Override
        public ImageBuilder minDisk(Long minDisk) {
            m.minDisk = minDisk;
            return this;
        }

        @Override
        public ImageBuilder minRam(Long minRam) {
            m.minRam = minRam;
            return this;
        }

        @Override
        public ImageBuilder owner(String owner) {
            m.owner = owner;
            return this;
        }

        @Override
        public ImageBuilder isPublic(Boolean isPublic) {
            if (isPublic != null)
                m.isPublic = isPublic;
            return this;
        }

        @Override
        public ImageBuilder properties(Map<String, String> properties) {
            m.properties = properties;
            return this;
        }

        @Override
        protected Image reference() {
            return m;
        }

        @Override
        public Image build() {
            return m;
        }

        @Override
        public ImageBuilder from(Image in) {
            this.m = (GlanceImage) in;
            return this;
        }

        @Override
        public ImageBuilder property(String key, String value) {
            if (key != null && value != null) {
                if (m.properties == null)
                    m.properties = Maps.newHashMap();
                m.properties.put(key, value);
            }
            return this;
        }

        @Override
        public ImageBuilder storeType(StoreType storeType) {
            m.storeType = storeType;
            return this;
        }

        @Override
        public ImageBuilder copyFrom(String copyFrom) {
            m.copyFrom = copyFrom;
            return this;
        }
    }
}
