package org.openstack4j.openstack.artifact.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.artifact.Template;

/**
 * Template implementation model
 *
 * @author Pavan Vadavi
 */
public class TemplateImpl implements Template {

    @JsonProperty("status")
    private String status;
    @JsonProperty("sha1")
    private String sha1;
    @JsonProperty("url")
    private String url;
    @JsonProperty("external")
    private Boolean external;
    @JsonProperty("content_type")
    private String contentType;
    @JsonProperty("sha256")
    private String sha256;
    @JsonProperty("md5")
    private String md5;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Boolean getExternal() {
        return external;
    }

    public void setExternal(Boolean external) {
        this.external = external;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
