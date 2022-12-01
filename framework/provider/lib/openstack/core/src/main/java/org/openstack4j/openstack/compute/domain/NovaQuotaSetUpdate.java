package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.compute.QuotaSetUpdate;
import org.openstack4j.model.compute.builder.QuotaSetUpdateBuilder;

public class NovaQuotaSetUpdate implements QuotaSetUpdate {

    private static final long serialVersionUID = 1L;

    @JsonProperty("metadata_items")
    private Integer metadataItems;

    @JsonProperty("injected_file_content_bytes")
    private Integer injectedFileContentBytes;

    @JsonProperty("injected_files")
    private Integer injectedFiles;

    @JsonProperty
    private Integer ram;

    @JsonProperty("floating_ips")
    private Integer floatingIps;

    @JsonProperty
    private Integer instances;

    @JsonProperty
    private Integer cores;

    @JsonProperty("security_groups")
    private Integer securityGroups;

    @JsonProperty("security_group_rules")
    private Integer securityGroupRules;

    @JsonProperty("injected_file_path_bytes")
    private Integer injectedFilePathBytes;

    @JsonProperty("key_pairs")
    private Integer keyPairs;

    public NovaQuotaSetUpdate() {
        super();
    }


    protected NovaQuotaSetUpdate(Integer metadataItems,
                                 Integer injectedFileContentBytes, Integer injectedFiles,
                                 Integer ram, Integer floatingIps, Integer instances,
                                 Integer cores, Integer securityGroups,
                                 Integer securityGroupRules, Integer injectedFilePathBytes,
                                 Integer keyPairs) {
        super();
        this.metadataItems = metadataItems;
        this.injectedFileContentBytes = injectedFileContentBytes;
        this.injectedFiles = injectedFiles;
        this.ram = ram;
        this.floatingIps = floatingIps;
        this.instances = instances;
        this.cores = cores;
        this.securityGroups = securityGroups;
        this.securityGroupRules = securityGroupRules;
        this.injectedFilePathBytes = injectedFilePathBytes;
        this.keyPairs = keyPairs;
    }

    public static QuotaSetUpdateBuilder builder() {
        return new QuotaSetUpdateConcreteBuilder();
    }

    @Override
    public QuotaSetUpdateBuilder toBuilder() {
        return new QuotaSetUpdateConcreteBuilder(this);
    }

    public static class QuotaSetUpdateConcreteBuilder implements QuotaSetUpdateBuilder {

        private NovaQuotaSetUpdate model;

        public QuotaSetUpdateConcreteBuilder() {
            this.model = new NovaQuotaSetUpdate();
        }

        public QuotaSetUpdateConcreteBuilder(QuotaSetUpdate model) {
            this.model = (NovaQuotaSetUpdate) model;
        }

        @Override
        public QuotaSetUpdate build() {
            return model;
        }

        @Override
        public QuotaSetUpdateBuilder from(QuotaSetUpdate in) {
            return new QuotaSetUpdateConcreteBuilder(in);
        }

        @Override
        public QuotaSetUpdateBuilder metadataItems(int metadataitems) {
            model.metadataItems = metadataitems;
            return this;
        }

        @Override
        public QuotaSetUpdateBuilder injectedFileContentBytes(int injectedFileContentBytes) {
            model.injectedFileContentBytes = injectedFileContentBytes;
            return this;
        }

        @Override
        public QuotaSetUpdateBuilder injectedFiles(int injectedFiles) {
            model.injectedFiles = injectedFiles;
            return this;
        }

        @Override
        public QuotaSetUpdateBuilder ram(int ram) {
            model.ram = ram;
            return this;
        }

        @Override
        public QuotaSetUpdateBuilder floatingIps(int floatingIps) {
            model.floatingIps = floatingIps;
            return this;
        }

        @Override
        public QuotaSetUpdateBuilder instances(int instances) {
            model.instances = instances;
            return this;
        }

        @Override
        public QuotaSetUpdateBuilder cores(int cores) {
            model.cores = cores;
            return this;
        }

        @Override
        public QuotaSetUpdateBuilder securityGroups(int securityGroups) {
            model.securityGroups = securityGroups;
            return this;
        }

        @Override
        public QuotaSetUpdateBuilder securityGroupRules(int securityGroupRules) {
            model.securityGroupRules = securityGroupRules;
            return this;
        }

        @Override
        public QuotaSetUpdateBuilder injectedFilePathBytes(int injectedFilePathBytes) {
            model.injectedFilePathBytes = injectedFilePathBytes;
            return this;
        }

        @Override
        public QuotaSetUpdateBuilder keyPairs(int keyPairs) {
            model.keyPairs = keyPairs;
            return this;
        }
    }

    @JsonRootName("quota_set")
    public static class NovaQuotaSetUpdateTenant extends NovaQuotaSetUpdate {

        private static final long serialVersionUID = 1L;

        public NovaQuotaSetUpdateTenant(Integer metadataItems,
                                        Integer injectedFileContentBytes, Integer injectedFiles,
                                        Integer ram, Integer floatingIps,
                                        Integer instances, Integer cores,
                                        Integer securityGroups, Integer securityGroupRules,
                                        Integer injectedFilePathBytes, Integer keyPairs) {
            super(metadataItems, injectedFileContentBytes, injectedFiles, ram,
                    floatingIps, instances, cores, securityGroups, securityGroupRules,
                    injectedFilePathBytes, keyPairs);
        }

        public static NovaQuotaSetUpdateTenant from(QuotaSetUpdate qs) {
            NovaQuotaSetUpdate nqs = (NovaQuotaSetUpdate) qs;
            return new NovaQuotaSetUpdateTenant(nqs.metadataItems, nqs.injectedFileContentBytes, nqs.injectedFiles,
                    nqs.ram, nqs.floatingIps, nqs.instances, nqs.cores, nqs.securityGroups,
                    nqs.securityGroupRules, nqs.injectedFilePathBytes, nqs.keyPairs);
        }
    }

    @JsonRootName("quota_class_set")
    public static class NovaQuotaSetUpdateClass extends NovaQuotaSetUpdate {

        private static final long serialVersionUID = 1L;

        public NovaQuotaSetUpdateClass(Integer metadataItems,
                                       Integer injectedFileContentBytes, Integer injectedFiles,
                                       Integer ram, Integer floatingIps,
                                       Integer instances, Integer cores,
                                       Integer securityGroups, Integer securityGroupRules,
                                       Integer injectedFilePathBytes, Integer keyPairs) {
            super(metadataItems, injectedFileContentBytes, injectedFiles, ram,
                    floatingIps, instances, cores, securityGroups, securityGroupRules,
                    injectedFilePathBytes, keyPairs);
        }

        public static NovaQuotaSetUpdateClass from(QuotaSetUpdate qs) {
            NovaQuotaSetUpdate nqs = (NovaQuotaSetUpdate) qs;
            return new NovaQuotaSetUpdateClass(nqs.metadataItems, nqs.injectedFileContentBytes, nqs.injectedFiles,
                    nqs.ram, nqs.floatingIps, nqs.instances, nqs.cores, nqs.securityGroups,
                    nqs.securityGroupRules, nqs.injectedFilePathBytes, nqs.keyPairs);
        }
    }

}
