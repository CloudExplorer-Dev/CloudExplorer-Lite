package org.openstack4j.model.sahara.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.sahara.ServiceConfig;

/**
 * Builder interface used for {@link ServiceConfig} object.
 *
 * @author Ekasit Kijsipongse
 */
public interface ServiceConfigBuilder extends Builder<ServiceConfigBuilder, ServiceConfig> {

    //
    // Define setter apis for user to create a new service configuration
    //

    /**
     * See {@link ServiceConfig#get()}
     *
     * @param name  name of the parameter
     * @param value value of the parameter
     * @return ServiceConfigBuilder
     */
    ServiceConfigBuilder set(String name, Object value);

}
