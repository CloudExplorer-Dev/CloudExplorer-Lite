package org.openstack4j.model.compute.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.compute.Flavor;

/**
 * Builder for a Flavor model class
 *
 * @author Jeremy Unruh
 */
public interface FlavorBuilder extends Builder<FlavorBuilder, Flavor> {

    /**
     * @see Flavor#getName()
     */
    FlavorBuilder name(String name);

    /**
     * @see Flavor#getRam()
     */
    FlavorBuilder ram(int ram);

    /**
     * @see Flavor#getVcpus()
     */
    FlavorBuilder vcpus(int vcpus);

    /**
     * @see Flavor#getDisk()
     */
    FlavorBuilder disk(int disk);

    /**
     * @see Flavor#getSwap()
     */
    FlavorBuilder swap(int swap);

    /**
     * @see Flavor#getRxtxFactor();
     */
    FlavorBuilder rxtxFactor(float rxtxFactor);

    /**
     * @see Flavor#isPublic()
     */
    FlavorBuilder isPublic(boolean isPublic);

    /**
     * @see Flavor#getEphemeral()
     */
    FlavorBuilder ephemeral(int ephemeral);

    /**
     * @see Flavor#getId()
     */
    FlavorBuilder id(String id);
}
