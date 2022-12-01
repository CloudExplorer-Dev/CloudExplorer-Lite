package org.openstack4j.api.senlin;

import org.openstack4j.model.senlin.BuildInfo;

/**
 * This interface defines all methods for the manipulation of BuildInfo
 *
 * @author lion
 */
public interface SenlinBuildInfoService {

    /**
     * returns details of a {@link BuildInfo}.
     *
     * @return BuildInfo
     */
    BuildInfo get();
}
