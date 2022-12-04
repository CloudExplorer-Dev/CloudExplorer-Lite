package org.openstack4j.openstack.senlin.internal;

import org.openstack4j.api.senlin.SenlinBuildInfoService;
import org.openstack4j.model.senlin.BuildInfo;
import org.openstack4j.openstack.senlin.domain.SenlinBuildInfo;

/**
 * This class contains getters for all implementation of the available build-info services
 *
 * @author lion
 */
public class SenlinBuildInfoServiceImpl extends BaseSenlinServices implements SenlinBuildInfoService {

    @Override
    public BuildInfo get() {
        return get(SenlinBuildInfo.class, uri("/build-info")).execute();
    }
}
