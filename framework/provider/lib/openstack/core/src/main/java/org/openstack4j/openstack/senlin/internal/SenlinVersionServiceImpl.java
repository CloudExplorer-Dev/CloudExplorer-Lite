package org.openstack4j.openstack.senlin.internal;

import org.openstack4j.api.senlin.SenlinVersionService;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.senlin.Version;
import org.openstack4j.openstack.internal.BaseOpenStackService;
import org.openstack4j.openstack.senlin.domain.SenlinVersion;

import java.util.List;

/**
 * This class contains getters for all implementation of the available version services
 *
 * @author lion
 */
public class SenlinVersionServiceImpl extends BaseOpenStackService implements SenlinVersionService {

    public SenlinVersionServiceImpl() {
        super(ServiceType.CLUSTERING);
    }

    @Override
    public List<? extends Version> list() {
        return get(SenlinVersion.Version.class, uri("/")).execute().getList();
    }
}
