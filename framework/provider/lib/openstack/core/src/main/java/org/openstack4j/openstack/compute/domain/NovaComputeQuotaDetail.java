package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.common.QuotaDetails;
import org.openstack4j.model.compute.ComputeQuotaDetail;
import org.openstack4j.openstack.common.QuotaDetailsEntity;

/**
 * The detail of quota for a project or a project and a user.
 *
 * @author Mingshan
 */
@JsonRootName("quota_set")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NovaComputeQuotaDetail implements ComputeQuotaDetail {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String id;
    @JsonProperty
    private QuotaDetailsEntity cores;
    @JsonProperty
    private QuotaDetailsEntity instances;
    @JsonProperty("key_pairs")
    private QuotaDetailsEntity keyPairs;
    @JsonProperty("metadata_items")
    private QuotaDetailsEntity metadataItems;
    private QuotaDetailsEntity ram;
    @JsonProperty("server_groups")
    private QuotaDetailsEntity serverGroups;
    @JsonProperty("server_group_members")
    private QuotaDetailsEntity serverGroupMembers;

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
    public QuotaDetails getCores() {
        return cores;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuotaDetails getInstances() {
        return instances;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuotaDetails getRam() {
        return ram;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuotaDetails getKeyPairs() {
        return keyPairs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuotaDetails getMetadataItems() {
        return metadataItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuotaDetails getServerGroupMembers() {
        return serverGroupMembers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QuotaDetails getServerGroups() {
        return serverGroups;
    }

    @Override
    public String toString() {
        return "NovaComputeQuotaUsage{" +
                "id='" + id + '\'' +
                ", cores=" + cores +
                ", instances=" + instances +
                ", keyPairs=" + keyPairs +
                ", metadataItems=" + metadataItems +
                ", ram=" + ram +
                ", serverGroups=" + serverGroups +
                ", serverGroupMembers=" + serverGroupMembers +
                '}';
    }
}
