package org.openstack4j.model.network.ext.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ext.HealthMonitorAssociate;

/**
 * A builder to assiocate a healthmonitor with a lb pool
 *
 * @author liujunpeng
 */
public interface HealthMonitorAssociateBuilder extends Builder<HealthMonitorAssociateBuilder, HealthMonitorAssociate> {

    /**
     * @param id the healthMonitor identifier
     * @return HealthMonitorAssociateBuilder
     */
    public HealthMonitorAssociateBuilder id(String id);
}
