package org.openstack4j.openstack.storage.object.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.api.Apis;
import org.openstack4j.api.storage.ObjectStorageObjectService;
import org.openstack4j.model.common.DLPayload;
import org.openstack4j.model.storage.block.options.DownloadOptions;
import org.openstack4j.model.storage.object.SwiftObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static org.openstack4j.core.transport.ClientConstants.CONTENT_TYPE_DIRECTORY;

/**
 * Represents an Object which is a File or Directory within a Container
 *
 * @author Jeremy Unruh
 */
public class SwiftObjectImpl implements SwiftObject {

    private static final long serialVersionUID = 1L;

    @JsonProperty("hash")
    private String eTag;
    @JsonProperty("last_modified")
    private Date lastModified;
    @JsonProperty("bytes")
    private long sizeBytes;
    @JsonProperty
    private String name;
    @JsonProperty("subdir")
    private String directoryName;
    @JsonProperty("content_type")
    private String mimeType;

    @JsonIgnore
    private Map<String, String> metadata;

    @JsonIgnore
    private String containerName;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String getETag() {
        return eTag;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public long getSizeInBytes() {
        return sizeBytes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDirectoryName() {
        return directoryName;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public boolean isDirectory() {
        if (directoryName != null && mimeType == null)
            return true;
        else
            return CONTENT_TYPE_DIRECTORY.equals(mimeType);
    }

    @Override
    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    @Override
    public Map<String, String> getMetadata() {
        if (metadata == null)
            metadata = Apis.get(ObjectStorageObjectService.class).getMetadata(containerName, name);
        return metadata;
    }

    @Override
    public DLPayload download() {
        return download(DownloadOptions.create());
    }

    @Override
    public DLPayload download(DownloadOptions options) {
        return Apis.get(ObjectStorageObjectService.class).download(containerName, name, options);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name).add("last_modified", lastModified).add("mimeType", mimeType)
                .add("size_bytes", sizeBytes).add("ETag", eTag).add("directory", isDirectory())
                .add("containerName", containerName).add("metadata", metadata)
                .toString();
    }

    public static class SwiftObjects extends ArrayList<SwiftObjectImpl> {
        private static final long serialVersionUID = 1L;
    }

    public static class Builder {

        private SwiftObjectImpl obj = new SwiftObjectImpl();

        public Builder name(String name) {
            obj.name = name;
            return this;
        }

        public Builder directoryName(String directoryName) {
            obj.directoryName = directoryName;
            return this;
        }

        public Builder eTag(String eTag) {
            obj.eTag = eTag;
            return this;
        }

        public Builder lastModified(Date lastModified) {
            obj.lastModified = lastModified;
            return this;
        }

        public Builder sizeBytes(long sizeBytes) {
            obj.sizeBytes = sizeBytes;
            return this;
        }

        public Builder mimeType(String mimeType) {
            obj.mimeType = mimeType;
            return this;
        }

        public Builder containerName(String containerName) {
            obj.containerName = containerName;
            return this;
        }

        public Builder metadata(Map<String, String> metadata) {
            obj.metadata = metadata;
            return this;
        }

        public SwiftObjectImpl build() {
            return obj;
        }
    }
}
