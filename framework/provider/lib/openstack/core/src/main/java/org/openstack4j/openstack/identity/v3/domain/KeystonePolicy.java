package org.openstack4j.openstack.identity.v3.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.openstack4j.model.identity.v3.Policy;
import org.openstack4j.model.identity.v3.builder.PolicyBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

@JsonRootName("policy")
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeystonePolicy implements Policy {

    private static final long serialVersionUID = 1L;
    private String id;
    @JsonProperty("project_id")
    private String projectId;
    @JsonProperty("user_id")
    private String userId;
    private String type;
    private Map<String, String> links;
    private String blob;

    /**
     * @return the policy builder
     */
    public static PolicyBuilder builder() {
        return new PolicyConcreteBuilder();
    }

    @Override
    public PolicyBuilder toBuilder() {
        return new PolicyConcreteBuilder(this);
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
    public String getProjectId() {
        return projectId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserId() {
        return userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBlob() {
        return blob;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getLinks() {
        return links;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("projectId", projectId)
                .add("userId", userId)
                .add("type", type)
                .add("blob", blob)
                .add("links", links)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id, projectId, userId, type, blob, links);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        KeystonePolicy that = KeystonePolicy.class.cast(obj);
        return Objects.equal(this.id, that.id)
                && Objects.equal(this.type, that.type)
                && Objects.equal(this.projectId, that.projectId)
                && Objects.equal(this.userId, that.userId)
                && Objects.equal(this.blob, that.blob)
                && Objects.equal(this.links, that.links);
    }


    public static class Policies extends ListResult<KeystonePolicy> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("policies")
        private List<KeystonePolicy> list;

        @Override
        public List<KeystonePolicy> value() {
            return list;
        }
    }

    public static class PolicyConcreteBuilder implements PolicyBuilder {

        KeystonePolicy model;

        PolicyConcreteBuilder() {
            this(new KeystonePolicy());
        }

        PolicyConcreteBuilder(KeystonePolicy model) {
            this.model = model;
        }

        @Override
        public Policy build() {
            return model;
        }

        @Override
        public PolicyBuilder from(Policy in) {
            if (in != null)
                this.model = (KeystonePolicy) in;
            return this;
        }

        @Override
        public PolicyBuilder id(String id) {
            model.id = id;
            return this;
        }

        @Override
        public PolicyBuilder type(String type) {
            model.type = type;
            return this;
        }

        @Override
        public PolicyBuilder blob(String blob) {
            model.blob = blob;
            return this;
        }

        @Override
        public PolicyBuilder links(Map<String, String> links) {
            model.links = links;
            return this;
        }

        @Override
        public PolicyBuilder projectId(String projectId) {
            model.projectId = projectId;
            return this;
        }

        @Override
        public PolicyBuilder userId(String userId) {
            model.userId = userId;
            return this;
        }

    }

}
