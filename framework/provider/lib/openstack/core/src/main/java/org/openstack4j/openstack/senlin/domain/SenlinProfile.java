package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.senlin.Profile;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This is a model of a senlinProfile. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author lion
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("profile")
public class SenlinProfile implements Profile {
    private static final long serialVersionUID = 5188248340362305141L;

    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("project")
    private String project;
    @JsonProperty("metadata")
    private Map<String, Object> metadata;
    @JsonProperty("spec")
    private Map<String, Object> spec;
    @JsonProperty("type")
    private String type;
    @JsonProperty("updated_at")
    private Date updated_at;
    @JsonProperty("user")
    private String user;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Date getUpdatedAt() {
        return updated_at;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getDomain() {
        return domain;
    }

    @Override
    public String getProject() {
        return project;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public Map<String, Object> getSpec() {
        return spec;
    }

    @Override
    public String toString() {
        return "SenlinProfile{" +
                "createdAt=" + createdAt +
                ", domain='" + domain + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", project='" + project + '\'' +
                ", metadata=" + metadata +
                ", spec=" + spec +
                ", type='" + type + '\'' +
                ", updated_at=" + updated_at +
                ", user='" + user + '\'' +
                '}';
    }

    /**
     * An inner class for representing lists of senlinProfile
     *
     * @author lion
     */
    public static class Profile extends ListResult<SenlinProfile> {
        private static final long serialVersionUID = -3931672456549086071L;

        @JsonProperty("profiles")
        private List<SenlinProfile> list;

        protected List<SenlinProfile> value() {
            return list;
        }
    }
}
