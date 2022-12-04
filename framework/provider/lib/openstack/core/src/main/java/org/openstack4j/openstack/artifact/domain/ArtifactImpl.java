package org.openstack4j.openstack.artifact.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.artifact.Artifact;

import java.util.List;

/**
 * Created by vadavi on 27-01-2017.
 */
public class ArtifactImpl implements Artifact {

    @JsonProperty("status")
    private String status;
    @JsonProperty("icon")
    private Object icon;
    @JsonProperty("name")
    private String name;
    @JsonProperty("license")
    private Object license;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("activated_at")
    private String activatedAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("visibility")
    private String visibility;
    @JsonProperty("provided_by")
    private Object providedBy;
    @JsonProperty("version")
    private String version;
    @JsonProperty("license_url")
    private Object licenseUrl;
    @JsonProperty("supported_by")
    private Object supportedBy;
    @JsonProperty("owner")
    private String owner;
    @JsonProperty("release")
    private List<Object> release = null;
    @JsonProperty("metadata")
    private MetadataImpl metadata;
    @JsonProperty("id")
    private String id;
    @JsonProperty("tags")
    private List<Object> tags = null;
    @JsonProperty("description")
    private String description;

    @Override
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object getLicense() {
        return license;
    }

    public void setLicense(Object license) {
        this.license = license;
    }

    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(String activatedAt) {
        this.activatedAt = activatedAt;
    }

    @Override
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public Object getProvidedBy() {
        return providedBy;
    }

    public void setProvidedBy(Object providedBy) {
        this.providedBy = providedBy;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public Object getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(Object licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    @Override
    public Object getSupportedBy() {
        return supportedBy;
    }

    public void setSupportedBy(Object supportedBy) {
        this.supportedBy = supportedBy;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public List<Object> getRelease() {
        return release;
    }

    public void setRelease(List<Object> release) {
        this.release = release;
    }

    @Override
    public MetadataImpl getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataImpl metadata) {
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
