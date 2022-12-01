package org.openstack4j.openstack.senlin.internal;

import org.openstack4j.api.senlin.SenlinWebHookService;
import org.openstack4j.core.transport.HttpRequest;
import org.openstack4j.core.transport.internal.HttpExecutor;
import org.openstack4j.model.senlin.ActionID;
import org.openstack4j.openstack.internal.BaseOpenStackService;
import org.openstack4j.openstack.senlin.domain.SenlinAction;
import org.openstack4j.openstack.senlin.domain.SenlinActionID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class contains getters for all implementation of the available webHook services
 *
 * @author lion
 */
public class SenlinWebHookServiceImpl extends BaseOpenStackService implements SenlinWebHookService {

    @Override
    public ActionID action(String webHookUrl) {
        checkNotNull(webHookUrl);
        HttpRequest newReq = new HttpRequest();
        newReq.toBuilder().methodPost().endpoint(webHookUrl).path("");
        newReq.builder(SenlinAction.class);
        return HttpExecutor.create().execute(newReq).getEntity(SenlinActionID.class);
    }
}
