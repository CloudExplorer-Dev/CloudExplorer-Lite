package org.openstack4j.api.tacker;

import org.openstack4j.api.Apis;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 11, 2016
 */
public class TackerServiceImpl implements TackerService {

    @Override
    public VnfdService vnfd() {
        return Apis.get(VnfdService.class);
    }

    @Override
    public VnfService vnf() {
        return Apis.get(VnfService.class);
    }

    @Override
    public VimService vim() {
        return Apis.get(VimService.class);
    }
}
