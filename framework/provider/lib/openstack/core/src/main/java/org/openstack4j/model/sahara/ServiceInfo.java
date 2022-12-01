package org.openstack4j.model.sahara;

import org.openstack4j.model.ModelEntity;

import java.util.Map;

/**
 * Service Information Model
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface ServiceInfo extends ModelEntity {

    /**
     * @param name the name of the information
     * @return the information
     */
    String get(String name);

    /**
     * @return map of all information or null
     */
    Map<String, String> getInfos();

}
