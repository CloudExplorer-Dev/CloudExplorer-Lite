package org.openstack4j.openstack.storage.block.internal;

import org.openstack4j.api.storage.ext.BlockStorageServiceService;
import org.openstack4j.model.storage.block.ext.Service;
import org.openstack4j.openstack.storage.block.domain.ext.ExtService.Services;

import java.util.List;

/**
 * Block Storage Services service provides CRUD capabilities for Cinder service(s).
 *
 * @author Taemin
 */
public class BlockStorageServiceServiceImpl extends BaseBlockStorageServices implements BlockStorageServiceService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Service> list() {
        return get(Services.class, uri("/os-services")).execute().getList();
    }

}
