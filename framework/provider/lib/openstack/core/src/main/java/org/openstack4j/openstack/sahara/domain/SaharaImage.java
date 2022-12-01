package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.sahara.Image;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A Sahara image
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
@JsonRootName("image")
public class SaharaImage implements Image {

    private static final long serialVersionUID = 1L;

    private Status status;
    private String username;
    private Date updated;
    @JsonProperty("OS-EXT-IMG-SIZE:size")
    private long size;
    private String name;
    private Date created;
    private List<String> tags;
    private int minDisk;
    private int progress;
    private int minRam;
    @JsonProperty("metadata")
    private Map<String, Object> metadata;
    private String id;
    private String description;

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
    public String getUsername() {
        return username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getUpdated() {
        return updated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSize() {
        return size;
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
    public Date getCreated() {
        return created;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getTags() {
        return tags;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinDisk() {
        return minDisk;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getProgress() {
        return progress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinRam() {
        return minRam;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getMetaData() {
        return metadata;
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
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("status", status).add("description", description).add("username", username)
                .add("progress", progress).add("size", size).add("minRam", minRam)
                .add("minDisk", minDisk).add("created", created).add("updated", updated)
                .add("metadata", metadata).add("tags", tags).addValue("\n")
                .toString();
    }

    public static class SaharaImages extends ListResult<SaharaImage> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("images")
        private List<SaharaImage> images;

        public List<SaharaImage> value() {
            return images;
        }

    }
}
