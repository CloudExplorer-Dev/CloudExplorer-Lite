package org.openstack4j.model.sahara;

import org.openstack4j.model.ModelEntity;

import java.util.List;

/**
 * Hints for job configuration that can be applied to job exeuctions.
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface JobConfigHint extends ModelEntity {

    /**
     * @return the list of configuration maps
     */
    List<? extends JobConfigHintConfig> getConfigs();

    /**
     * @return the list of arguments
     */
    List<Object> getArgs();
}
