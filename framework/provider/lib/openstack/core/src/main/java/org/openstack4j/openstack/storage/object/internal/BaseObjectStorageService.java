package org.openstack4j.openstack.storage.object.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.core.transport.HttpResponse;
import org.openstack4j.openstack.internal.BaseOpenStackService;

import static org.openstack4j.core.transport.HttpEntityHandler.closeQuietly;

/**
 * Base OpenStack Storage Service
 *
 * @author Jeremy Unruh
 */
public class BaseObjectStorageService extends BaseOpenStackService {

    public BaseObjectStorageService() {
        super(ServiceType.OBJECT_STORAGE);
    }

    protected boolean isResponseSuccess(HttpResponse res, int status) {
        return isResponseSuccess(res, status, true);
    }

    protected boolean isResponseSuccess(HttpResponse res, int status, boolean closeResponse) {
        boolean result = res.getStatus() == status;
        if (closeResponse) {
            closeQuietly(res);
        }
        return result;
    }
}
