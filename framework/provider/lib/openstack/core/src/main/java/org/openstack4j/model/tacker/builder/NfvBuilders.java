package org.openstack4j.model.tacker.builder;

/**
 * NFV Builders..
 *
 * @author Vishvesh Deshmukh
 * @date Aug 11, 2016
 */
public interface NfvBuilders {

    /**
     * The builder to create a Vnfd
     *
     * @return the vnfd builder
     */
    VnfdBuilder vnfd();

    /**
     * The builder to create a Vnf
     *
     * @return the vnf builder
     */
    VnfBuilder vnf();

    /**
     * The builder to update a vnf
     *
     * @return VnfUpdateBuilder
     */
    VnfUpdateBuilder vnfUpdate();

    /**
     * The builder to create a Vim
     *
     * @return the vim builder
     */
    VimBuilder vim();
}
