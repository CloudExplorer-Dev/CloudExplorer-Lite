package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;

/**
 * Absolute limits are specified as name/value pairs. The name of the absolute limit uniquely identifies the limit within a deployment.
 * Please consult your provider for an exhaustive list of absolute value names. An absolute limit value is always specified as an integer.
 * The name of the absolute limit determines the unit type of the integer value.
 * <p>
 * For example, the name maxServerMeta implies that the value is in terms of server metadata items.
 * <p>
 * Any limit which returns -1 indicates no value/no data/not supported by vendor
 *
 * @author Jeremy Unruh
 */
public interface AbsoluteLimit extends ModelEntity {

    /**
     * Gets the max server meta.
     *
     * @return the maxServerMeta (-1 indicates no value)
     */
    int getMaxServerMeta();

    /**
     * Gets the max personality.
     *
     * @return the maxPersonality (-1 indicates no value)
     */
    int getMaxPersonality();

    /**
     * Gets the max image meta.
     *
     * @return the maxImageMeta (-1 indicates no value)
     */
    int getMaxImageMeta();

    /**
     * Gets the max personality size.
     *
     * @return the maxPersonalitySize (-1 indicates no value)
     */
    int getMaxPersonalitySize();

    /**
     * Gets the max total cores.
     *
     * @return the maxTotalCores (-1 indicates no value)
     */
    int getMaxTotalCores();

    /**
     * Gets the max total instances.
     *
     * @return the maxTotalInstances (-1 indicates no value)
     */
    int getMaxTotalInstances();

    /**
     * Gets the max total ram size.
     *
     * @return the maxTotalRAMSize (-1 indicates no value)
     */
    int getMaxTotalRAMSize();

    /**
     * Gets the total volumes used.
     *
     * @return the totalVolumesUsed (-1 indicates no value)
     */
    int getTotalVolumesUsed();

    /**
     * Gets the max security group rules.
     *
     * @return the maxSecurityGroupRules (-1 indicates no value)
     */
    int getMaxSecurityGroupRules();

    /**
     * Gets the max total keypairs.
     *
     * @return the maxTotalKeypairs (-1 indicates no value)
     */
    int getMaxTotalKeypairs();

    /**
     * Gets the total cores used.
     *
     * @return the totalCoresUsed (-1 indicates no value)
     */
    int getTotalCoresUsed();

    /**
     * Gets the max total volumes.
     *
     * @return the maxTotalVolumes (-1 indicates no value)
     */
    int getMaxTotalVolumes();

    /**
     * Gets the total ram used.
     *
     * @return the totalRAMUsed (-1 indicates no value)
     */
    int getTotalRAMUsed();

    /**
     * Gets the total instances used.
     *
     * @return the totalInstancesUsed (-1 indicates no value)
     */
    int getTotalInstancesUsed();

    /**
     * Gets the max security groups.
     *
     * @return the maxSecurityGroups (-1 indicates no value)
     */
    int getMaxSecurityGroups();

    /**
     * Gets the total volume gigabytes used.
     *
     * @return the totalVolumeGigabytesUsed (-1 indicates no value)
     */
    int getTotalVolumeGigabytesUsed();

    /**
     * Gets the total security groups used.
     *
     * @return the totalSecurityGroupsUsed (-1 indicates no value)
     */
    int getTotalSecurityGroupsUsed();

    /**
     * Gets the max total floating ips.
     *
     * @return the maxTotalFloatingIps (-1 indicates no value)
     */
    int getMaxTotalFloatingIps();

    /**
     * Gets the total key pairs used.
     *
     * @return the totalKeyPairsUsed (-1 indicates no value)
     */
    int getTotalKeyPairsUsed();

    /**
     * Gets the max total volume gigabytes.
     *
     * @return the maxTotalVolumeGigabytes (-1 indicates no value)
     */
    int getMaxTotalVolumeGigabytes();

    /**
     * Gets the server meta used.
     *
     * @return the serverMetaUsed (-1 indicates no value)
     */
    int getServerMetaUsed();

    /**
     * Gets the personality used.
     *
     * @return the personalityUsed (-1 indicates no value)
     */
    int getPersonalityUsed();

    /**
     * Gets the image meta used.
     *
     * @return the imageMetaUsed (-1 indicates no value)
     */
    int getImageMetaUsed();

    /**
     * Gets the personality size used.
     *
     * @return the personalitySizeUsed (-1 indicates no value)
     */
    int getPersonalitySizeUsed();

    /**
     * Gets the security group rules used.
     *
     * @return the securityGroupRulesUsed (-1 indicates no value)
     */
    int getSecurityGroupRulesUsed();

    /**
     * Gets the total floating ips used.
     *
     * @return the totalFloatingIpsUsed (-1 indicates no value)
     */
    int getTotalFloatingIpsUsed();

    /**
     * Gets the max server group members
     *
     * @return the max server group members (-1 indicates no value)
     */
    int getMaxServerGroupMembers();

    /**
     * Gets the max server groups
     *
     * @return the max server groups (-1 indicates no value)
     */
    int getMaxServerGroups();
}
