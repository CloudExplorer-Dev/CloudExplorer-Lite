package org.openstack4j.model.sahara.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.sahara.Job;

/**
 * Builder interface used for {@link Job} object.
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface JobBuilder extends Builder<JobBuilder, Job> {

    /**
     * See {@link Job#getDescription()}
     *
     * @param description the description of the job
     * @return JobBuilder
     */
    JobBuilder description(String description);

    /**
     * See {@link Job#getType()}
     *
     * @param type the type of the job
     * @return JobBuilder
     */
    JobBuilder type(String type);

    /**
     * See {@link Job#getName()}
     *
     * @param name the name of the job
     * @return JobBuilder
     */
    JobBuilder name(String name);

    /**
     * See {@link Job#getMainIds()}
     *
     * @param id the id of the job binary
     * @return JobBuilder
     */
    JobBuilder setMain(String jobBinaryId);

    /**
     * See {@link Job#getLibId()}
     *
     * @param name the name of the job
     * @return JobBuilder
     */
    JobBuilder addLibs(String jobBinaryId);
}
