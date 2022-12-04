package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.openstack4j.model.sahara.ServiceConfig;
import org.openstack4j.model.sahara.builder.ServiceConfigBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * For mapping JSON response to java objects
 *
 * @author Ekasit Kijsipongse
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaharaServiceConfig extends HashMap<String, Object> implements ServiceConfig {

    public static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    public static ServiceConfigBuilder builder() {
        return new ConcreteServiceConfigBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name) {
        return super.get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getConfigs() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceConfigBuilder toBuilder() {
        return new ConcreteServiceConfigBuilder(this);
    }

    public static class ConcreteServiceConfigBuilder implements ServiceConfigBuilder {

        private SaharaServiceConfig m;

        ConcreteServiceConfigBuilder() {
            this(new SaharaServiceConfig());
        }

        ConcreteServiceConfigBuilder(SaharaServiceConfig m) {
            this.m = m;
        }

        @Override
        public ServiceConfigBuilder set(String name, Object value) {
            m.put(name, value);
            return this;
        }

        @Override
        public ServiceConfig build() {
            return m;
        }

        @Override
        public ConcreteServiceConfigBuilder from(ServiceConfig in) {
            m = (SaharaServiceConfig) in;
            return this;
        }

    }

}
