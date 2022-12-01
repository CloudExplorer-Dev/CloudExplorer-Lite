package org.openstack4j.openstack.storage.object.internal;

import org.openstack4j.api.Apis;
import org.openstack4j.api.storage.ObjectStorageAccountService;
import org.openstack4j.api.storage.ObjectStorageContainerService;
import org.openstack4j.api.storage.ObjectStorageObjectService;
import org.openstack4j.api.storage.ObjectStorageService;

/**
 * OpenStack Object Storage service implementation
 *
 * @author Jeremy Unruh
 */
public class ObjectStorageServiceImpl implements ObjectStorageService {

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectStorageAccountService account() {
        return Apis.get(ObjectStorageAccountService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectStorageContainerService containers() {
        return Apis.get(ObjectStorageContainerService.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectStorageObjectService objects() {
        return Apis.get(ObjectStorageObjectService.class);
    }

}
