package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.senlin.Policy;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This is a model of a senlinPolicy. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author lion
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("policy")
public class SenlinPolicy implements Policy {
    private static final long serialVersionUID = -8596243151035363550L;

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("project")
    private String project;
    @JsonProperty("user")
    private String user;
    @JsonProperty("data")
    private Map<String, Object> data;
    @JsonProperty("spec")
    private Map<String, Object> spec;
    @JsonProperty("type")
    private String type;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
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
    public String getUser() {
        return user;
    }

    @Override
    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public Map<String, Object> getSpec() {
        return spec;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "SenlinPolicy{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                ", project='" + project + '\'' +
                ", user='" + user + '\'' +
                ", data=" + data +
                ", spec=" + spec +
                ", type='" + type + '\'' +
                ", created_at='" + createdAt + '\'' +
                ", updated_at='" + updatedAt + '\'' +
                '}';
    }

    /**
     * An inner class for representing lists of senlinPolicy
     *
     * @author lion
     */
    public static class Policy extends ListResult<SenlinPolicy> {
        private static final long serialVersionUID = -7262524403002617445L;

        @JsonProperty("policies")
        private List<SenlinPolicy> list;

        protected List<SenlinPolicy> value() {
            return list;
        }
    }
}
