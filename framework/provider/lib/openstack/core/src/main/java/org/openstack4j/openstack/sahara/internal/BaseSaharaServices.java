package org.openstack4j.openstack.sahara.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.internal.BaseOpenStackService;

/**
 * This class is extended by every Sahara Service. It is necessary to determine
 * the correct endpoint (url) for sahara calls.
 *
 * @author Ekasit Kijipongse
 */
public class BaseSaharaServices extends BaseOpenStackService {

    protected BaseSaharaServices() {
        super(ServiceType.SAHARA);
    }

}
