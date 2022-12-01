package org.openstack4j.openstack.tacker.builders;

import org.openstack4j.model.tacker.builder.*;
import org.openstack4j.openstack.tacker.domain.TackerVim;
import org.openstack4j.openstack.tacker.domain.TackerVnf;
import org.openstack4j.openstack.tacker.domain.TackerVnfUpdate;
import org.openstack4j.openstack.tacker.domain.TackerVnfd;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 11, 2016
 */
public class TackerBuilders implements NfvBuilders {

    /**
     * The builder to Create a vnf-d
     *
     * @return VnfdBuilder
     */
    @Override
    public VnfdBuilder vnfd() {
        return TackerVnfd.builder();
    }

    /**
     * The builder to Create a vnf-d
     *
     * @return VnfBuilder
     */
    @Override
    public VnfBuilder vnf() {
        return TackerVnf.builder();
    }

    /**
     * The builder to update a vnf
     *
     * @return VnfUpdateBuilder
     */
    public VnfUpdateBuilder vnfUpdate() {
        return TackerVnfUpdate.builder();
    }

    @Override
    public VimBuilder vim() {
        return TackerVim.builder();
    }

}
