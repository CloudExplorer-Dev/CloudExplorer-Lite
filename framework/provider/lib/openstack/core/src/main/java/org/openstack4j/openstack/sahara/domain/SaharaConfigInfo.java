package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.sahara.ConfigInfo;

import java.util.List;

/**
 * For mapping JSON response to/from java objects
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaharaConfigInfo implements ConfigInfo {

    private static final long serialVersionUID = 1L;

    @JsonProperty("default_value")
    private String defaultValue;
    private String name;
    private Integer priority;
    @JsonProperty("config_type")
    private String type;
    @JsonProperty("applicable_target")
    private String applicableTarget;
    @JsonProperty("is_optional")
    private Boolean isOptional;
    private String scope;
    private String description;
    @JsonProperty("config_values")
    private List<String> configValues;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultValue() {
        return description;
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
    public Integer getPriority() {
        return priority;
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
    public String getApplicableTarget() {
        return applicableTarget;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isOptional() {
        return isOptional;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getScope() {
        return scope;
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
    public List<String> getConfigValues() {
        return configValues;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name)
                .add("description", description)
                .add("priority", priority)
                .add("config_type", type)
                .add("default_value", defaultValue)
                .add("config_values", configValues)
                .add("is_optional", isOptional)
                .add("scope", scope)
                .add("applicable_target", applicableTarget)
                .toString();
    }

}
