package org.openstack4j.model.sahara;

import org.openstack4j.model.ModelEntity;

import java.util.List;

/**
 * A Sahara Config Information
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface ConfigInfo extends ModelEntity {

    /**
     * @return the default value
     */
    String getDefaultValue();

    /**
     * @return the name
     */
    String getName();

    /**
     * @return the priority
     */
    Integer getPriority();

    /**
     * @return the type (string, int, bool, enum)
     */
    String getType();

    /**
     * @return the applicable target
     */
    String getApplicableTarget();

    /**
     * @return true if this config is optional
     */
    Boolean isOptional();

    /**
     * @return the scope
     */
    String getScope();

    /**
     * @return the description
     */
    String getDescription();

    /**
     * @return the list of valid config values (if type is enum)
     */
    List<String> getConfigValues();
}
