package org.openstack4j.api.sahara;

import org.openstack4j.common.RestService;

/**
 * (Sahara) Data Processing Operations API
 *
 * @author Ekasit Kijsipongse
 */
public interface SaharaService extends RestService {

    /**
     * Cluster Service API
     *
     * @return the cluster service
     */
    ClusterService clusters();

    /**
     * Node Group Template Service API
     *
     * @return the node group template service
     */
    NodeGroupTemplateService nodeGroupTemplates();

    /**
     * Cluster Template Service API
     *
     * @return the cluster template service
     */
    ClusterTemplateService clusterTemplates();

    /**
     * Image Service API
     *
     * @return the image service
     */
    SaharaImageService images();

    /**
     * Plugin Service API
     *
     * @return the plugin service
     */
    SaharaPluginService plugins();

    /**
     * DataSource Service API
     *
     * @return the datasource service
     */
    DataSourceService dataSources();

    /**
     * Job Binary Internal Service API
     *
     * @return the job binary internal service
     */
    JobBinaryInternalService jobBinaryInternals();

    /**
     * Job Binary Service API
     *
     * @return the job binary service
     */
    JobBinaryService jobBinaries();

    /**
     * Job Service API
     *
     * @return the job service
     */
    JobService jobs();


    /**
     * Job Execution Service API
     *
     * @return the job execution service
     */
    JobExecutionService jobExecutions();
}
