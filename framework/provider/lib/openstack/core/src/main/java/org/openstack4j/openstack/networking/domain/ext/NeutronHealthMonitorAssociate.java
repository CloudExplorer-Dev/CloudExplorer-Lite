package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.ext.HealthMonitorAssociate;
import org.openstack4j.model.network.ext.builder.HealthMonitorAssociateBuilder;

/**
 * A  entity used to associate a healthMonitor with a pool
 *
 * @author liujunpeng
 */
@JsonRootName("health_monitor")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronHealthMonitorAssociate implements HealthMonitorAssociate {

    private static final long serialVersionUID = 1L;

    private String id;

    public static HealthMonitorAssociateBuilder builder() {
        return new HealthMonitorAssociateConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HealthMonitorAssociateBuilder toBuilder() {
        return new HealthMonitorAssociateConcreteBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).toString();
    }

    public static class HealthMonitorAssociateConcreteBuilder implements HealthMonitorAssociateBuilder {

        NeutronHealthMonitorAssociate m;

        public HealthMonitorAssociateConcreteBuilder() {
            this(new NeutronHealthMonitorAssociate());
        }

        public HealthMonitorAssociateConcreteBuilder(NeutronHealthMonitorAssociate m) {
            this.m = m;
        }

        @Override
        public HealthMonitorAssociate build() {
            return m;
        }

        @Override
        public HealthMonitorAssociateBuilder from(HealthMonitorAssociate in) {
            m = (NeutronHealthMonitorAssociate) in;
            return this;
        }

        @Override
        public HealthMonitorAssociateBuilder id(String id) {
            m.id = id;
            return this;
        }
    }

}
