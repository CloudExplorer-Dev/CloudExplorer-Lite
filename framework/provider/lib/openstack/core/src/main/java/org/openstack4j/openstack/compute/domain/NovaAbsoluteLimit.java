package org.openstack4j.openstack.compute.domain;

import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.AbsoluteLimit;

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
public class NovaAbsoluteLimit implements AbsoluteLimit {

    private static final long serialVersionUID = 1L;

    private Integer maxServerMeta;
    private Integer serverMetaUsed;
    private Integer maxPersonality;
    private Integer personalityUsed;
    private Integer maxImageMeta;
    private Integer imageMetaUsed;
    private Integer maxPersonalitySize;
    private Integer personalitySizeUsed;
    private Integer maxTotalCores;
    private Integer totalCoresUsed;
    private Integer maxTotalInstances;
    private Integer totalInstancesUsed;
    private Integer maxTotalRAMSize;
    private Integer totalRAMUsed;
    private Integer maxSecurityGroupRules;
    private Integer securityGroupRulesUsed;
    private Integer maxTotalKeypairs;
    private Integer totalKeyPairsUsed;
    private Integer maxTotalVolumes;
    private Integer totalVolumesUsed;
    private Integer maxSecurityGroups;
    private Integer totalSecurityGroupsUsed;
    private Integer maxTotalFloatingIps;
    private Integer totalFloatingIpsUsed;
    private Integer maxTotalVolumeGigabytes;
    private Integer totalVolumeGigabytesUsed;
    private Integer maxServerGroups;
    private Integer maxServerGroupMembers;

    /**
     * Gets the max server meta.
     *
     * @return the max server meta
     */
    public int getMaxServerMeta() {
        return wrap(maxServerMeta);
    }

    /**
     * Gets the server meta used.
     *
     * @return the server meta used
     */
    public int getServerMetaUsed() {
        return wrap(serverMetaUsed);
    }

    /**
     * Gets the max personality.
     *
     * @return the max personality
     */
    public int getMaxPersonality() {
        return wrap(maxPersonality);
    }

    /**
     * Gets the personality used.
     *
     * @return the personality used
     */
    public int getPersonalityUsed() {
        return wrap(personalityUsed);
    }

    /**
     * Gets the max image meta.
     *
     * @return the max image meta
     */
    public int getMaxImageMeta() {
        return wrap(maxImageMeta);
    }

    /**
     * Gets the image meta used.
     *
     * @return the image meta used
     */
    public int getImageMetaUsed() {
        return wrap(imageMetaUsed);
    }

    /**
     * Gets the max personality size.
     *
     * @return the max personality size
     */
    public int getMaxPersonalitySize() {
        return wrap(maxPersonalitySize);
    }

    /**
     * Gets the personality size used.
     *
     * @return the personality size used
     */
    public int getPersonalitySizeUsed() {
        return wrap(personalitySizeUsed);
    }

    /**
     * Gets the max total cores.
     *
     * @return the max total cores
     */
    public int getMaxTotalCores() {
        return wrap(maxTotalCores);
    }

    /**
     * Gets the total cores used.
     *
     * @return the total cores used
     */
    public int getTotalCoresUsed() {
        return wrap(totalCoresUsed);
    }

    /**
     * Gets the max total instances.
     *
     * @return the max total instances
     */
    public int getMaxTotalInstances() {
        return wrap(maxTotalInstances);
    }

    /**
     * Gets the total instances used.
     *
     * @return the total instances used
     */
    public int getTotalInstancesUsed() {
        return wrap(totalInstancesUsed);
    }

    /**
     * Gets the max total ram size.
     *
     * @return the max total ram size
     */
    public int getMaxTotalRAMSize() {
        return wrap(maxTotalRAMSize);
    }

    /**
     * Gets the total ram used.
     *
     * @return the total ram used
     */
    public int getTotalRAMUsed() {
        return wrap(totalRAMUsed);
    }

    /**
     * Gets the max security group rules.
     *
     * @return the max security group rules
     */
    public int getMaxSecurityGroupRules() {
        return wrap(maxSecurityGroupRules);
    }

    /**
     * Gets the security group rules used.
     *
     * @return the security group rules used
     */
    public int getSecurityGroupRulesUsed() {
        return wrap(securityGroupRulesUsed);
    }

    /**
     * Gets the max total keypairs.
     *
     * @return the max total keypairs
     */
    public int getMaxTotalKeypairs() {
        return wrap(maxTotalKeypairs);
    }

    /**
     * Gets the total key pairs used.
     *
     * @return the total key pairs used
     */
    public int getTotalKeyPairsUsed() {
        return wrap(totalKeyPairsUsed);
    }

    /**
     * Gets the max total volumes.
     *
     * @return the max total volumes
     */
    public int getMaxTotalVolumes() {
        return wrap(maxTotalVolumes);
    }

    /**
     * Gets the total volumes used.
     *
     * @return the total volumes used
     */
    public int getTotalVolumesUsed() {
        return wrap(totalVolumesUsed);
    }

    /**
     * Gets the max security groups.
     *
     * @return the max security groups
     */
    public int getMaxSecurityGroups() {
        return wrap(maxSecurityGroups);
    }

    /**
     * Gets the total security groups used.
     *
     * @return the total security groups used
     */
    public int getTotalSecurityGroupsUsed() {
        return wrap(totalSecurityGroupsUsed);
    }

    /**
     * Gets the max total floating ips.
     *
     * @return the max total floating ips
     */
    public int getMaxTotalFloatingIps() {
        return wrap(maxTotalFloatingIps);
    }

    /**
     * Gets the total floating ips used.
     *
     * @return the total floating ips used
     */
    public int getTotalFloatingIpsUsed() {
        return wrap(totalFloatingIpsUsed);
    }

    /**
     * Gets the max total volume gigabytes.
     *
     * @return the max total volume gigabytes
     */
    public int getMaxTotalVolumeGigabytes() {
        return wrap(maxTotalVolumeGigabytes);
    }

    /**
     * Gets the total volume gigabytes used.
     *
     * @return the total volume gigabytes used
     */
    public int getTotalVolumeGigabytesUsed() {
        return wrap(totalVolumeGigabytesUsed);
    }

    @Override
    public int getMaxServerGroupMembers() {
        return wrap(maxServerGroupMembers);
    }

    @Override
    public int getMaxServerGroups() {
        return wrap(maxServerGroups);
    }

    /**
     * Wrap.
     *
     * @param value the value
     * @return the int
     */
    private int wrap(Integer value) {
        return value != null ? value : -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("maxServerMeta", maxServerMeta).add("serverMetaUsed", serverMetaUsed).add("maxPersonality", maxPersonality)
                .add("personalityUsed", personalityUsed).add("maxImageMeta", maxImageMeta).add("imageMetaUsed", imageMetaUsed)
                .add("maxPersonalitySize", maxPersonalitySize).add("personalitySizeUsed", personalitySizeUsed).add("maxTotalCores", maxTotalCores)
                .add("totalCoresUsed", totalCoresUsed).add("maxTotalInstances", maxTotalInstances).add("totalInstancesUsed", totalInstancesUsed)
                .add("maxTotalRAMSize", maxTotalRAMSize).add("totalRAMUsed", totalRAMUsed).add("maxSecurityGroupRules", maxSecurityGroupRules)
                .add("securityGroupRulesUsed", securityGroupRulesUsed).add("maxTotalKeypairs", maxTotalKeypairs).add("totalKeyPairsUsed", totalKeyPairsUsed)
                .add("maxTotalVolumes", maxTotalVolumes).add("totalVolumesUsed", totalVolumesUsed).add("maxSecurityGroups", maxSecurityGroups)
                .add("totalSecurityGroupsUsed", totalSecurityGroupsUsed).add("maxTotalFloatingIps", maxTotalFloatingIps).add("totalFloatingIpsUsed", totalFloatingIpsUsed)
                .add("maxTotalVolumeGigabytes", maxTotalVolumeGigabytes).add("totalVolumeGigabytesUsed", totalVolumeGigabytesUsed)
                .add("maxServerGroups", maxSecurityGroups).add("maxServerGroupMembers", maxServerGroupMembers)
                .toString();
    }
}
