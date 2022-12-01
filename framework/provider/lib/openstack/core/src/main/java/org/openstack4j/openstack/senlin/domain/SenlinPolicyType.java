package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.senlin.PolicyType;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * This is a model of a senlinPolicy_type. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author lion
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("policy_type")
public class SenlinPolicyType implements PolicyType {
    private static final long serialVersionUID = -8893826725585696810L;

    @JsonProperty("name")
    private String name;
    @JsonProperty("schema")
    private Map<String, Object> schema;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "SenlinPolicyType{" +
                "name='" + name + '\'' +
                ", schema=" + schema +
                '}';
    }

    /**
     * An inner class for representing lists of senlinPolicy_type
     *
     * @author lion
     */
    public static class PolicyType extends ListResult<SenlinPolicyType> {
        private static final long serialVersionUID = -4755855096962007407L;

        @JsonProperty("policy_types")
        private List<SenlinPolicyType> list;

        protected List<SenlinPolicyType> value() {
            return list;
        }
    }
}
