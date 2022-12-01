package org.openstack4j.api.storage.ext;

import org.openstack4j.model.storage.block.ext.Service;

import java.util.List;

/**
 * API which supports the "os-services" extension.
 *
 * @author Taemin
 */
public interface BlockStorageServiceService {

    /**
     * List services info
     * <p>
     * NOTE: This is an extension and not all deployments support os-services
     *
     * @return a list of block storage services
     */
    List<? extends Service> list();
}
