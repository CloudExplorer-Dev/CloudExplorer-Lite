package org.openstack4j.openstack.sahara.builder;

import org.openstack4j.model.sahara.builder.*;
import org.openstack4j.openstack.sahara.domain.*;

/**
 * The Data Processing Builders
 */
public class SaharaBuilders implements DataProcessingBuilders {

    @Override
    public ClusterBuilder cluster() {
        return SaharaCluster.builder();
    }

    @Override
    public ClusterTemplateBuilder clusterTemplate() {
        return SaharaClusterTemplate.builder();
    }

    @Override
    public NodeGroupBuilder nodeGroup() {
        return SaharaNodeGroup.builder();
    }

    @Override
    public NodeGroupTemplateBuilder nodeGroupTemplate() {
        return SaharaNodeGroupTemplate.builder();
    }

    @Override
    public ServiceConfigBuilder serviceConfig() {
        return SaharaServiceConfig.builder();
    }

    @Override
    public DataSourceBuilder dataSource() {
        return SaharaDataSource.builder();
    }

    @Override
    public JobBinaryBuilder jobBinary() {
        return SaharaJobBinary.builder();
    }

    @Override
    public JobBuilder job() {
        return SaharaJob.builder();
    }

    @Override
    public JobConfigBuilder jobConfig() {
        return SaharaJobConfig.builder();
    }

    @Override
    public JobExecutionBuilder jobExecution() {
        return SaharaJobExecution.builder();
    }
}
