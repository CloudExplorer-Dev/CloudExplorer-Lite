package org.openstack4j.model.sahara.builder;

/**
 * The Data Processing (Sahara) builders
 */
public interface DataProcessingBuilders {

    /**
     * The builder to create a sahara cluster
     *
     * @return the cluster builder
     */
    public ClusterBuilder cluster();

    /**
     * The builder to create a sahara cluster template
     *
     * @return the cluster template builder
     */
    public ClusterTemplateBuilder clusterTemplate();

    /**
     * The builder to create a sahara node group
     *
     * @return the node group builder
     */
    public NodeGroupBuilder nodeGroup();

    /**
     * The builder to create a sahara node group template
     *
     * @return the node group template builder
     */
    public NodeGroupTemplateBuilder nodeGroupTemplate();

    /**
     * The builder to create a sahara service configuration
     *
     * @return the service configuration builder
     */
    public ServiceConfigBuilder serviceConfig();

    /**
     * The builder which creates a sahara Data Source
     *
     * @return the data source builder
     */
    public DataSourceBuilder dataSource();

    /**
     * The builder which creates a sahara Job Binary
     *
     * @return the job binary builder
     */
    public JobBinaryBuilder jobBinary();

    /**
     * The builder which creates a sahara Job
     *
     * @return the job builder
     */
    public JobBuilder job();

    /**
     * The builder which creates a job configuration for sahara job execution
     *
     * @return the job config builder
     */
    public JobConfigBuilder jobConfig();

    /**
     * The builder which creates a sahara job execution
     *
     * @return the job execution builder
     */
    public JobExecutionBuilder jobExecution();


}
