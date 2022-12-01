package org.openstack4j.model.compute.builder;

/**
 * The Compute Builders
 */
public interface ComputeBuilders {

    /**
     * The builder to create a Server
     *
     * @return the server create builder
     */
    public ServerCreateBuilder server();

    /**
     * The builder to create a Block Device Mapping
     *
     * @return the block device mapping builder
     */
    public BlockDeviceMappingBuilder blockDeviceMapping();

    /**
     * The builder to create a Flavor.
     *
     * @return the flavor builder
     */
    public FlavorBuilder flavor();

    /**
     * The builder to create a Compute/Nova Floating IP
     *
     * @return the floating ip builder
     * @deprecated Since these APIs are only implemented for nova-network, they are deprecated. These will fail with a 404 starting from microversion 2.36. They were removed in the 18.0.0 Rocky release.
     */
    @Deprecated
    public FloatingIPBuilder floatingIP();

    /**
     * This builder which creates a QuotaSet for updates
     *
     * @return the QuotaSet update builder
     */
    public QuotaSetUpdateBuilder quotaSet();
}
