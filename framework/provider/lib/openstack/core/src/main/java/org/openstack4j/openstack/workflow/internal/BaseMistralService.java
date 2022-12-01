package org.openstack4j.openstack.workflow.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.internal.BaseOpenStackService;


/**
 * Base class for all Mistral services.
 *
 * @author Renat Akhmerov
 */

class BaseMistralService extends BaseOpenStackService {
    BaseMistralService() {
        super(ServiceType.WORKFLOW);
    }
}
