package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.QuotaSet;

/**
 * An OpenStack Quota-Set
 *
 * @author Jeremy Unruh
 */
@JsonRootName("quota_set")
public class NovaQuotaSet implements QuotaSet {

    private static final long serialVersionUID = 1L;

    private String id;

    @JsonProperty("metadata_items")
    private int metadataItems;

    @JsonProperty("injected_file_content_bytes")
    private int injectedFileContentBytes;

    @JsonProperty("injected_files")
    private int injectedFiles;

    private int gigabytes;
    private int ram;

    @JsonProperty("floating_ips")
    private int floatingIps;

    private int instances;

    private int volumes;

    private int cores;

    @JsonProperty("security_groups")
    private int securityGroups;

    @JsonProperty("security_group_rules")
    private int securityGroupRules;

    @JsonProperty("injected_file_path_bytes")
    private int injectedFilePathBytes;

    @JsonProperty("key_pairs")
    private int keyPairs;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMetadataItems() {
        return metadataItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInjectedFileContentBytes() {
        return injectedFileContentBytes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInjectedFiles() {
        return injectedFiles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getGigabytes() {
        return gigabytes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRam() {
        return ram;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFloatingIps() {
        return floatingIps;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInstances() {
        return instances;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVolumes() {
        return volumes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCores() {
        return cores;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSecurityGroups() {
        return securityGroups;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSecurityGroupRules() {
        return securityGroupRules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInjectedFilePathBytes() {
        return injectedFilePathBytes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getKeyPairs() {
        return keyPairs;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("metadataItems", metadataItems).add("injectedFileContentBytes", injectedFileContentBytes)
                .add("injectedFileContentBytes", injectedFileContentBytes).add("injectedFiles", injectedFiles).add("gigabytes", gigabytes)
                .add("ram", "ram").add("securityGroups", securityGroups).add("securityGroupRules", securityGroupRules)
                .add("cores", cores).add("floatingIps", floatingIps).add("instances", instances).add("volumes", volumes)
                .add("injectedFilePathBytes", injectedFilePathBytes).add("keyPairs", keyPairs)
                .toString();
    }

    @JsonRootName("quota_class_set")
    public static class NovaQuotaSetClass extends NovaQuotaSet {
        private static final long serialVersionUID = 1L;
    }
}
