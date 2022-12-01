package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.senlin.Version;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * This is a model of a senlinVersion. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author lion
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("version")
public class SenlinVersion implements Version {
    private static final long serialVersionUID = -3019833373605658588L;

    @JsonProperty("status")
    private String status;
    @JsonProperty("id")
    private String id;
    @JsonProperty("links")
    private List<Map<String, String>> links;

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public List<Map<String, String>> getLinks() {
        return links;
    }

    @Override
    public String toString() {
        return "SenlinVersion{" +
                "status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", links=" + links +
                '}';
    }

    /**
     * An inner class for representing lists of senlinVersion
     *
     * @author lion
     */
    public static class Version extends ListResult<SenlinVersion> {
        private static final long serialVersionUID = 6026392138824408084L;
        @JsonProperty("versions")
        private List<SenlinVersion> list;

        protected List<SenlinVersion> value() {
            return list;
        }
    }
}
