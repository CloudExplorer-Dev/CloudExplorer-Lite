package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.ext.builder.HealthMonitorAssociateBuilder;

/**
 * An entity used to associate  a healthMonitor with a lb pool
 *
 * @author liujunpeng
 */
public interface HealthMonitorAssociate extends ModelEntity,
        Buildable<HealthMonitorAssociateBuilder> {

    /**
     * @return id the healthMonitor identifier
     */
    public String getId();
}
