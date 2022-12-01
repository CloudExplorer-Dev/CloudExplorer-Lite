package org.openstack4j.model.compute.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.compute.*;

import java.util.List;
import java.util.Map;

/**
 * Builds a Server used for a Create Action
 *
 * @author Jeremy Unruh
 */
public interface ServerCreateBuilder extends Buildable.Builder<ServerCreateBuilder, ServerCreate> {

    /**
     * @see Server#getName()
     */
    ServerCreateBuilder name(String name);

    /**
     * @see Server#getFlavor()
     */
    ServerCreateBuilder flavor(String flavorId);

    /**
     * @see Server#getFlavor()
     */
    ServerCreateBuilder flavor(Flavor flavor);

    /**
     * @see Server#getImage()
     */
    ServerCreateBuilder image(String imageId);

    /**
     * @see Server#getImage()
     */
    ServerCreateBuilder image(Image image);

    /**
     * Adds list of networks (by id) to server
     *
     * @param networks list of network ids
     * @return this builder
     */
    ServerCreateBuilder networks(List<String> networks);

    /**
     * Adds a network port
     *
     * @param portId id of port
     * @return this builder
     */
    ServerCreateBuilder addNetworkPort(String portId);

    /**
     * Adds a Personality to the Server.  A personality is a path to a file and the contents to be replaced on the new
     * VM.
     *
     * @param path     the path (max is 255 bytes)
     * @param contents the contents of the file {@code path}
     * @return this builder
     */
    ServerCreateBuilder addPersonality(String path, String contents);

    /**
     * Adds the security group.
     *
     * @param name the name
     * @return this builder
     */
    ServerCreateBuilder addSecurityGroup(String name);

    /**
     * Adds a new metadata item to the Server
     *
     * @param key   the metadata key
     * @param value the metadata value
     * @return ServerCreateBuilder
     */
    ServerCreateBuilder addMetadataItem(String key, String value);

    /**
     * Replaces and add the specified {@code metadata}
     *
     * @param metadata the metadata for this server
     * @return this builder
     */
    ServerCreateBuilder addMetadata(Map<String, String> metadata);

    /**
     * Adds a scheduler hint used for boot up
     *
     * @param key   the scheduler hint key
     * @param value the scheduler hint value
     * @return this builder
     */
    ServerCreateBuilder addSchedulerHint(String key, String value);

    /**
     * Adds a scheduler hint (array based) used for boot up
     *
     * @param key   the scheduler hint key
     * @param value the scheduler hint value
     * @return this builder
     */
    ServerCreateBuilder addSchedulerHint(String key, List<String> value);

    /**
     * Adds/Replaces scheduler hints used for bootup
     *
     * @param schedulerHints map of key and value scheduler hints
     * @return this builder
     */
    ServerCreateBuilder addSchedulerHints(Map<String, Object> schedulerHints);

    /**
     * Associates this Server with a public keypair name
     *
     * @param name the name of the public keypair
     * @return this builder
     */
    ServerCreateBuilder keypairName(String name);

    ServerCreateBuilder blockDevice(BlockDeviceMappingCreate blockDevice);

    /**
     * The availability zone in which to launch the server.
     *
     * @param availabilityZone the availability zone
     * @return this builder
     */
    ServerCreateBuilder availabilityZone(String availabilityZone);

    /**
     * Cloud-init userdata
     *
     * @param userData a base64 encoded string containing the userdata
     * @return this userdata
     */
    ServerCreateBuilder userData(String userData);

    /**
     * Add admin password for launching the server.
     *
     * @param password the password
     * @return this builder
     */

    ServerCreateBuilder addAdminPass(String adminPass);

    /**
     * Sets the config drive parameter.
     *
     * @param configDrive the configDrive value
     * @return this builder
     */

    ServerCreateBuilder configDrive(boolean configDrive);

}
