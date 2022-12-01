package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.senlin.ProfileType;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * This is a model of a senlinProfile_type. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author lion
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("profile_type")
public class SenlinProfileType implements ProfileType {
    private static final long serialVersionUID = -1760618273420931267L;

    @JsonProperty("name")
    private String name;
    @JsonProperty("schema")
    private Map<String, Map> schema;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Map> getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "SenlinProfileType{" +
                "name='" + name + '\'' +
                ", schema='" + schema + '\'' +
                '}';
    }

    /**
     * An inner class for representing lists of senlinProfile_type
     *
     * @author lion
     */
    public static class ProfileType extends ListResult<SenlinProfileType> {
        private static final long serialVersionUID = 5526915111653514850L;

        @JsonProperty("profile_types")
        private List<SenlinProfileType> list;

        protected List<SenlinProfileType> value() {
            return list;
        }
    }
}
