package org.openstack4j.openstack.heat.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.heat.StackCreate;
import org.openstack4j.model.heat.builder.StackCreateBuilder;
import org.openstack4j.openstack.heat.utils.Environment;
import org.openstack4j.openstack.heat.utils.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all elements required for the creation of a HeatStack. It
 * uses Jackson annotation for (de)serialization into JSON
 *
 * @author Matthias Reisser
 */
public class HeatStackCreate implements StackCreate {

    private static final long serialVersionUID = -8775995682456485275L;
    private static final Logger LOG = LoggerFactory.getLogger(HeatStackCreate.class);

    @JsonProperty("disableRollback")
    private boolean disableRollback;
    @JsonProperty("stack_name")
    private String name;
    @JsonProperty("template")
    private String template;
    @JsonProperty("template_url")
    private String templateURL;
    @JsonProperty("parameters")
    private Map<String, String> parameters;
    @JsonProperty("timeout_mins")
    private Long timeoutMins;
    @JsonProperty("environment")
    private String environment;
    @JsonProperty("files")
    private Map<String, String> files = new HashMap<String, String>();
    @JsonProperty("tags")
    private String tags;

    /**
     * Returnes a {@link HeatStackCreateConcreteBuilder} for configuration and
     * creation of a {@link HeatStackCreate} object.
     *
     * @return a {@link HeatStackCreateConcreteBuilder}
     */
    public static HeatStackCreateConcreteBuilder build() {
        return new HeatStackCreateConcreteBuilder();
    }

    @Override
    public StackCreateBuilder toBuilder() {
        return new HeatStackCreateConcreteBuilder(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public boolean getDisableRollback() {
        return disableRollback;
    }

    @Override
    public String getTemplate() {
        return template;
    }

    public String getTempateURL() {
        return templateURL;
    }

    public String getEnvironment() {
        return environment;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public String getTags() {
        return tags;
    }

    /**
     * A Builder to create a HeatStack. Use {@link #build()} to receive the
     * {@link StackCreate} object.
     *
     * @author Matthias Reisser
     */
    public static class HeatStackCreateConcreteBuilder implements
            StackCreateBuilder {

        private HeatStackCreate model;

        /**
         * Constructor to create a {@link HeatStackCreateConcreteBuilder} object
         * with a new, empty {@link HeatStackCreate} object.
         */
        public HeatStackCreateConcreteBuilder() {
            this(new HeatStackCreate());
        }

        /**
         * Constructor for manipulation of an existing {@link HeatStackCreate}
         * object.
         *
         * @param model the {@link HeatStackCreate} object which is to be
         *              modified.
         */
        public HeatStackCreateConcreteBuilder(HeatStackCreate model) {
            this.model = model;
        }

        @Override
        public StackCreate build() {
            return model;
        }

        @Override
        public StackCreateBuilder from(StackCreate in) {
            model = (HeatStackCreate) in;
            return this;
        }

        @Override
        public StackCreateBuilder name(String name) {
            model.name = name;
            return this;
        }

        @Override
        public StackCreateBuilder parameters(Map<String, String> parameters) {
            model.parameters = parameters;
            return this;
        }

        @Override
        public StackCreateBuilder timeoutMins(Long timeoutMins) {
            model.timeoutMins = timeoutMins;
            return this;
        }

        @Override
        public StackCreateBuilder disableRollback(boolean disableRollback) {
            model.disableRollback = disableRollback;
            return this;
        }

        @Override
        public StackCreateBuilder template(String template) {
            model.template = template;
            return this;
        }

        @Override
        public StackCreateBuilder templateFromFile(String tplFile) {
            try {
                Template tpl = new Template(tplFile);
                model.template = tpl.getTplContent();
                model.files.putAll(tpl.getFiles());
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            return this;
        }

        @Override
        public StackCreateBuilder templateURL(String templateURL) {
            model.templateURL = templateURL;
            return this;
        }

        @Override
        public StackCreateBuilder environment(String environment) {
            model.environment = environment;
            return this;
        }

        @Override
        public StackCreateBuilder environmentFromFile(String envFile) {
            try {
                Environment env = new Environment(envFile);
                model.environment = env.getEnvContent();
                model.files.putAll(env.getFiles());
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
            return this;
        }

        @Override
        public StackCreateBuilder files(Map<String, String> files) {
            model.files = files;
            return this;
        }

        @Override
        public StackCreateBuilder tags(String tags) {
            model.tags = tags;
            return this;
        }

    }

}
