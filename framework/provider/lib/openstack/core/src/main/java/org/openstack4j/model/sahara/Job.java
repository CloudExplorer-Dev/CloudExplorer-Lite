package org.openstack4j.model.sahara;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.sahara.builder.JobBuilder;

import java.util.Date;
import java.util.List;

/**
 * An Openstack Sahara Job
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface Job extends ModelEntity, Buildable<JobBuilder> {

    /**
     * @return the description of the job
     */
    String getDescription();

    /**
     * @return the tenant id of the job
     */
    String getTenantId();

    /**
     * @return the created date of the job
     */
    Date getCreatedAt();

    /**
     * @return the updated date of the job
     */
    Date getUpdatedAt();

    /**
     * @return the identifier of the job
     */
    String getId();

    /**
     * @return the name of the job
     */
    String getName();

    /**
     * @return the type of the job
     */
    String getType();

    /**
     * @return mains
     */
    List<? extends JobBinary> getFullMains();

    /**
     * @return libs
     */
    List<? extends JobBinary> getFullLibs();

    /**
     * @return main ids
     */
    List<String> getMains();

    /**
     * @return lib ids
     */
    List<String> getLibs();
}
