package org.openstack4j.api.tacker;

import org.openstack4j.common.RestService;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 11, 2016
 */
public interface TackerService extends RestService {

    /**
     * @return the Vnfd Service API
     */
    VnfdService vnfd();

    /**
     * @return the Vnf Service API
     */
    VnfService vnf();

    /**
     * @return the Vim Service API
     */
    VimService vim();
}
