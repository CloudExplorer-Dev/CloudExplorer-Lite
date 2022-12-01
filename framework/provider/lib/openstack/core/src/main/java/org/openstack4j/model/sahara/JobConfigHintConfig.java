package org.openstack4j.model.sahara;

/**
 * Information of each config in job configuration hint
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface JobConfigHintConfig {

    /**
     * @return name of the configuration
     */
    String getName();

    /**
     * @return value of the configuration
     */
    String getValue();

    /**
     * @return description of the configuration
     */
    String getDescription();
}
