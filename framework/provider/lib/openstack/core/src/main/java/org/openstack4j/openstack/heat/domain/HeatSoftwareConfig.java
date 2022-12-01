package org.openstack4j.openstack.heat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.openstack4j.model.heat.SoftwareConfig;
import org.openstack4j.model.heat.builder.SoftwareConfigBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Model implementation for Software Config
 *
 * @author Jeremy Unruh
 */
@JsonRootName("software_config")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeatSoftwareConfig implements SoftwareConfig {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    String id;
    @JsonProperty
    String name;
    @JsonProperty
    String group;
    @JsonProperty
    String config;
    @JsonProperty
    List<SCInput> inputs;
    @JsonProperty
    List<SCOutput> outputs;
    @JsonProperty
    Map<String, Object> options;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public String getConfig() {
        return config;
    }

    @Override
    public List<? extends Input> getInputs() {
        return inputs != null ? inputs : Collections.<SCInput>emptyList();
    }

    @Override
    public List<? extends Output> getOutputs() {
        return outputs != null ? outputs : Collections.<SCOutput>emptyList();
    }

    @Override
    public Map<String, Object> getOptions() {
        return options;
    }

    @Override
    public SoftwareConfigBuilder toBuilder() {
        return new Builder(this);
    }

    public String toString() {
        return MoreObjects.toStringHelper(SoftwareConfig.class).omitNullValues()
                .add("id", id).add("name", name).add("group", group).addValue("\n")
                .add("inputs", inputs).addValue("\n").add("outputs", outputs)
                .addValue("\n").add("config", config).add("options", options)
                .toString();
    }

    public static class SCInput implements Input {

        @JsonProperty("name")
        String name;
        @JsonProperty("description")
        String description;
        @JsonProperty("default")
        String defaultValue;
        @JsonProperty("type")
        String type;

        public SCInput() {
        }

        public SCInput(String name, String type, String description, String defaultValue) {
            this.name = name;
            this.type = type;
            this.description = description;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getDefaultValue() {
            return defaultValue;
        }

        @Override
        public String getType() {
            return type;
        }

        public String toString() {
            return MoreObjects.toStringHelper(Input.class).omitNullValues()
                    .add("name", name).add("type", type)
                    .add("description", description).add("default", defaultValue)
                    .toString();
        }
    }

    public static class SCOutput implements Output {

        @JsonProperty("name")
        String name;
        @JsonProperty("description")
        String description;
        @JsonProperty("error_output")
        boolean errorOutput;
        @JsonProperty("type")
        String type;

        public SCOutput() {
        }

        public SCOutput(String name, String type, String description, boolean errorOutput) {
            this.name = name;
            this.type = type;
            this.description = description;
            this.errorOutput = errorOutput;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public boolean isErrorOutput() {
            return errorOutput;
        }

        public String toString() {
            return MoreObjects.toStringHelper(Output.class).omitNullValues()
                    .add("name", name).add("type", type)
                    .add("description", description).add("errorOutput", errorOutput)
                    .toString();
        }
    }

    public static class Builder implements SoftwareConfigBuilder {

        private HeatSoftwareConfig sc;

        public Builder() {
            sc = new HeatSoftwareConfig();
        }

        Builder(HeatSoftwareConfig sc) {
            this.sc = sc;
        }

        @Override
        public SoftwareConfig build() {
            return sc;
        }

        @Override
        public SoftwareConfigBuilder from(SoftwareConfig in) {
            return new Builder((HeatSoftwareConfig) in);
        }

        @Override
        public SoftwareConfigBuilder name(String name) {
            sc.name = name;
            return this;
        }

        @Override
        public SoftwareConfigBuilder group(String group) {
            sc.group = group;
            return this;
        }

        @Override
        public SoftwareConfigBuilder input(String name) {
            input(name, null, null, null);
            return this;
        }

        @Override
        public SoftwareConfigBuilder input(String name, String type) {
            input(name, type, null, null);
            return this;
        }

        @Override
        public SoftwareConfigBuilder input(String name, String type, String description, String defaultValue) {
            if (sc.inputs == null)
                sc.inputs = Lists.newArrayList();

            sc.inputs.add(new SCInput(name, type, description, defaultValue));
            return this;
        }

        @Override
        public SoftwareConfigBuilder output(String name) {
            output(name, null, null, false);
            return this;
        }

        @Override
        public SoftwareConfigBuilder output(String name, String type) {
            output(name, type, null, false);
            return this;
        }

        @Override
        public SoftwareConfigBuilder output(String name, String type, String description, boolean isErrorOutput) {
            if (sc.outputs == null)
                sc.outputs = Lists.newArrayList();

            sc.outputs.add(new SCOutput(name, type, description, isErrorOutput));
            return this;
        }

        @Override
        public SoftwareConfigBuilder options(Map<String, Object> options) {
            sc.options = options;
            return this;
        }

        @Override
        public SoftwareConfigBuilder config(String config) {
            sc.config = config;
            return this;
        }
    }
}
