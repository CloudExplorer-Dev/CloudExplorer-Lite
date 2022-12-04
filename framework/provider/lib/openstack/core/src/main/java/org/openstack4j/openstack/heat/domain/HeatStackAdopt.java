package org.openstack4j.openstack.heat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.heat.AdoptStackData;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains all elements required for the adoption of a HeatStack. It
 * uses Jackson annotation for (de)serialization into JSON
 *
 * @author Ales Kemr
 */
public class HeatStackAdopt implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("stack_name")
    private String name;
    @JsonProperty("timeout_mins")
    private Long timeoutMins;
    @JsonProperty("parameters")
    private Map<String, String> parameters;
    @JsonProperty("disable_rollback")
    private boolean disableRollback;
    @JsonProperty("adopt_stack_data")
    private String adoptStackData;
    @JsonProperty("template")
    private String template;

    public static HeatStackAdoptBuilder builder() {
        return new HeatStackAdoptBuilder();
    }

    public String getName() {
        return name;
    }

    public Long getTimeoutMins() {
        return timeoutMins;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public boolean isDisableRollback() {
        return disableRollback;
    }

    public String getAdoptStackData() {
        return adoptStackData;
    }

    public String getTemplate() {
        return template;
    }

    public static class HeatStackAdoptBuilder {

        private HeatStackAdopt model;

        public HeatStackAdoptBuilder(HeatStackAdopt model) {
            this.model = model;
        }

        public HeatStackAdoptBuilder() {
            this.model = new HeatStackAdopt();
        }

        public HeatStackAdoptBuilder name(String name) {
            this.model.name = name;
            return this;
        }

        public HeatStackAdoptBuilder timeoutMins(Long timeoutMins) {
            this.model.timeoutMins = timeoutMins;
            return this;
        }

        public HeatStackAdoptBuilder parameters(Map<String, String> parameters) {
            this.model.parameters = parameters;
            return this;
        }

        public HeatStackAdoptBuilder disableRollback(boolean disableRollback) {
            this.model.disableRollback = disableRollback;
            return this;
        }

        public HeatStackAdoptBuilder adoptStackData(AdoptStackData adoptStackData) {
            try {
                this.model.adoptStackData = new ObjectMapper().writeValueAsString(adoptStackData);
                return this;
            } catch (JsonProcessingException ex) {
                Logger.getLogger(HeatStackAdopt.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }

        public HeatStackAdoptBuilder template(String template) {
            this.model.template = template;
            return this;
        }

        public HeatStackAdopt build() {
            return model;
        }
    }


}
