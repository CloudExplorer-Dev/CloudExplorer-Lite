package org.openstack4j.openstack.heat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.heat.AdoptStackData;

import java.util.Map;

/**
 * This class contains all elements required for the creation of <code>adopt_stack_data</code> element. It is used for stack adoption and as a return value for stack abandoning.
 * It uses Jackson annotation for (de)serialization into JSON.
 *
 * @author Ales Kemr
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeatAdoptStackData implements AdoptStackData {
    private static final long serialVersionUID = 1L;

    @JsonProperty("action")
    private String action;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("status")
    private String status;

    @JsonProperty("template")
    private Map<String, Object> template;

    @JsonProperty("resources")
    private Map<String, Map<String, Object>> resources;

    public static HeatAdoptStackDataBuilder builder() {
        return new HeatAdoptStackDataBuilder();
    }

    @Override
    public String getAction() {
        return action;
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
    public String getStatus() {
        return status;
    }

    @Override
    public Map<String, Object> getTemplate() {
        return template;
    }

    @Override
    public Map<String, Map<String, Object>> getResources() {
        return resources;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id)
                .add("name", name)
                .add("status", status)
                .add("resources", resources)
                .toString();
    }

    public static class HeatAdoptStackDataBuilder {

        private HeatAdoptStackData model;

        public HeatAdoptStackDataBuilder() {
            this.model = new HeatAdoptStackData();
        }

        public HeatAdoptStackDataBuilder(HeatAdoptStackData model) {
            this.model = model;
        }

        public HeatAdoptStackDataBuilder action(String action) {
            this.model.action = action;
            return this;
        }

        public HeatAdoptStackDataBuilder id(String id) {
            this.model.id = id;
            return this;
        }

        public HeatAdoptStackDataBuilder name(String name) {
            this.model.name = name;
            return this;
        }

        public HeatAdoptStackDataBuilder status(String status) {
            this.model.status = status;
            return this;
        }

        public HeatAdoptStackDataBuilder template(Map<String, Object> template) {
            this.model.template = template;
            return this;
        }

        public HeatAdoptStackDataBuilder resources(Map<String, Map<String, Object>> resources) {
            this.model.resources = resources;
            return this;
        }

        public HeatAdoptStackData build() {
            return model;
        }
    }


}
