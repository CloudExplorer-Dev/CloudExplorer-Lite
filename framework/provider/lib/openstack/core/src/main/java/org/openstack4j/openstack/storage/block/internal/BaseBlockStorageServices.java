package org.openstack4j.openstack.storage.block.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.openstack.internal.BaseOpenStackService;

/**
 * Base Cinder Service Layer
 *
 * @author Jeremy Unruh
 */
public class BaseBlockStorageServices extends BaseOpenStackService {

    public BaseBlockStorageServices() {
        super(ServiceType.BLOCK_STORAGE);
    }

}
