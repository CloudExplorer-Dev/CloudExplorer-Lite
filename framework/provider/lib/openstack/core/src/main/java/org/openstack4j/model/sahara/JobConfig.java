package org.openstack4j.model.sahara;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.sahara.builder.JobConfigBuilder;

import java.util.List;
import java.util.Map;

/**
 * An Configuration for Sahara Job Execution
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface JobConfig extends ModelEntity, Buildable<JobConfigBuilder> {

    /**
     * @return map of configurations
     */
    Map<String, Object> getConfigs();

    /**
     * @return list of arguments
     */
    List<Object> getArgs();

    /**
     * @return map of parameters
     */
    Map<String, Object> getParams();
}
