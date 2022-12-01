package org.openstack4j.model.compute;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.compute.Server.DiskConfig;
import org.openstack4j.model.compute.builder.ServerCreateBuilder;

import java.util.List;
import java.util.Map;

/**
 * The Model used to create a new VM/Server
 *
 * @author Jeremy Unruh
 */
public interface ServerCreate extends ModelEntity, Buildable<ServerCreateBuilder> {

    /**
     * The descriptive name for the Server
     *
     * @return the name of the server
     */
    String getName();

    /**
     * @return the administrative password
     */
    String getAdminPass();

    /**
     * A reference to the boot Image
     *
     * @return the image identifier
     */
    String getImageRef();

    /**
     * The resources/flavor to be assigned
     *
     * @return the flavor identifier
     */
    String getFlavorRef();

    /**
     * @return the accessible IPV4 address
     */
    String getAccessIPv4();

    /**
     * @return the accessible IPV6 address
     */
    String getAccessIPv6();

    /**
     * @return the min
     */
    Integer getMin();

    /**
     * Gets the max.
     *
     * @return the max
     */
    Integer getMax();

    /**
     * Controls how the disk is partitioned
     *
     * @return the disk configuration
     */
    DiskConfig getDiskConfig();

    /**
     * name of keypair to inject into the instance
     *
     * @return the keypair name
     */
    String getKeyName();

    /**
     * Gets the user data.
     *
     * @return the user data
     */
    String getUserData();

    /**
     * The value for config drive
     *
     * @return true, if this is a config based drive
     */
    boolean isConfigDrive();

    /**
     * Gets the meta data.
     *
     * @return the meta data
     */
    Map<String, String> getMetaData();

    /**
     * Personality is a list of Files which will be injected onto the server.
     *
     * @return the personality
     * @see Personality
     */
    List<Personality> getPersonality();

    /**
     * Gets the security groups.
     *
     * @return the security groups
     */
    List<? extends SecurityGroup> getSecurityGroups();

    /**
     * Gets the availability zone.
     *
     * @return the availability zone
     */
    String getAvailabilityZone();

    /**
     * Gets the networks.
     *
     * @return the networks
     */
    List<? extends NetworkCreate> getNetworks();

    /**
     * A Map of Key and Value used for scheduler hints on bootup
     *
     * @return scheduler hints or null
     */
    Map<String, Object> getSchedulerHints();

    /**
     * Adds the personality.
     *
     * @param path     the path
     * @param contents the contents
     */
    void addPersonality(String path, String contents);

    /**
     * Adds the security group.
     *
     * @param name the name
     */
    void addSecurityGroup(String name);

    /**
     * Adds the network.
     *
     * @param id      the id
     * @param fixedIP the fixed ip
     */
    void addNetwork(String id, String fixedIP);

    /**
     * Adds network port
     *
     * @param id id of a premade neutron port
     */
    void addNetworkPort(String id);


}
