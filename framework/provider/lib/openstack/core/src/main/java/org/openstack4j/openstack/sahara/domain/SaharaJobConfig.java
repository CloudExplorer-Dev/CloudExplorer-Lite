package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.openstack4j.model.sahara.JobConfig;
import org.openstack4j.model.sahara.builder.JobConfigBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * For mapping JSON response to/from java objects
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */

public class SaharaJobConfig implements JobConfig {

    private static final long serialVersionUID = 1L;

    @JsonProperty("configs")
    private Map<String, Object> configs;
    @JsonProperty("args")
    private List<Object> args;
    @JsonProperty("params")
    private Map<String, Object> params;

    /**
     * @return the job config builder
     */
    public static JobConfigBuilder builder() {
        return new concreteJobConfigBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getConfigs() {
        return configs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("configs", configs)
                .add("args", args)
                .add("params", params)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobConfigBuilder toBuilder() {
        return new concreteJobConfigBuilder(this);
    }

    public static class concreteJobConfigBuilder implements JobConfigBuilder {

        SaharaJobConfig m;

        concreteJobConfigBuilder() {
            this(new SaharaJobConfig());
        }

        concreteJobConfigBuilder(SaharaJobConfig m) {
            this.m = m;
        }

        @Override
        public JobConfig build() {
            return m;
        }

        @Override
        public JobConfigBuilder from(JobConfig in) {
            m = (SaharaJobConfig) in;
            return this;
        }

        @Override
        public JobConfigBuilder addConfig(String name, Object value) {
            if (m.configs == null)
                m.configs = new HashMap<String, Object>();
            m.configs.put(name, value);
            return this;
        }

        @Override
        public JobConfigBuilder addArg(Object arg) {
            if (m.args == null)
                m.args = Lists.newArrayList();
            m.args.add(arg);
            return this;
        }

        @Override
        public JobConfigBuilder addParam(String param, Object value) {
            if (m.params == null)
                m.params = new HashMap<String, Object>();
            m.params.put(param, value);
            return this;
        }

    }
}
