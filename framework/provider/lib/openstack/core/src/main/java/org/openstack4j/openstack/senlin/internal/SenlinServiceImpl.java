package org.openstack4j.openstack.senlin.internal;


import org.openstack4j.api.Apis;
import org.openstack4j.api.senlin.*;

/**
 * This class contains getters for all implementation of the available senlin services
 *
 * @author lion
 */
public class SenlinServiceImpl extends BaseSenlinServices implements SenlinService {

    @Override
    public SenlinPolicyService policy() {
        return Apis.get(SenlinPolicyService.class);
    }

    @Override
    public SenlinActionService action() {
        return Apis.get(SenlinActionService.class);
    }

    @Override
    public SenlinBuildInfoService buildInfo() {
        return Apis.get(SenlinBuildInfoService.class);
    }

    @Override
    public SenlinClusterPolicyService clusterPolicy() {
        return Apis.get(SenlinClusterPolicyService.class);
    }

    @Override
    public SenlinClusterService cluster() {
        return Apis.get(SenlinClusterService.class);
    }

    @Override
    public SenlinEventService event() {
        return Apis.get(SenlinEventService.class);
    }

    @Override
    public SenlinNodeService node() {
        return Apis.get(SenlinNodeService.class);
    }

    @Override
    public SenlinProfileService profile() {
        return Apis.get(SenlinProfileService.class);
    }

    @Override
    public SenlinProfileTypeService profileType() {
        return Apis.get(SenlinProfileTypeService.class);
    }

    @Override
    public SenlinPolicyTypeService policyType() {
        return Apis.get(SenlinPolicyTypeService.class);
    }

    @Override
    public SenlinReceiverService receiver() {
        return Apis.get(SenlinReceiverService.class);
    }

    @Override
    public SenlinWebHookService webHook() {
        return Apis.get(SenlinWebHookService.class);
    }

    @Override
    public SenlinVersionService version() {
        return Apis.get(SenlinVersionService.class);
    }
}
