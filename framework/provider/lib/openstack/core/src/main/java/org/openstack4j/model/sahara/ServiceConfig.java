package org.openstack4j.model.sahara;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.sahara.builder.ServiceConfigBuilder;

import java.util.Map;

/**
 * Service Configuration Model
 *
 * @author Ekasit Kijsipongse
 */
public interface ServiceConfig extends ModelEntity, Buildable<ServiceConfigBuilder> {

    /**
     * @param name the name of the parameter
     * @return the value of the parameter
     */
    Object get(String name);

    /**
     * @return map of all configurations or null
     */
    Map<String, Object> getConfigs();

}
