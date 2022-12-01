package org.openstack4j.api.magnum;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.magnum.*;

import java.util.List;


/**
 * OpenStack Container Infrastructure Management service (Magnum) APIs
 *
 * @author Sohan Sangwan
 */
public interface MagnumService extends RestService {
    /**
     * Lists all Magnum Services: container infrastructure service status
     * details
     *
     * @return list of Magnum Services
     */
    List<? extends Mservice> listMservices();

    /**
     * Lists all Baymodels
     *
     * @return list of Baymodels
     */
    List<? extends Baymodel> listBaymodels();

    /**
     * Creates a new baymodel
     */
    Baymodel createBaymodel(Baymodel baymodel);

    /**
     * Deletes a baymodel
     */
    ActionResponse deleteBaymodel(String id);

    /**
     * Gets all information of a baymodel
     *
     * @return baymodel information
     */
    Baymodel showBaymodel(String id);

    /**
     * Updates baymodel attributes
     *
     * @return updated baymodel
     */
    Baymodel updateBaymodel(String id, String operations);

    /**
     * Lists all Bays/clusters
     *
     * @return list of Bays
     */
    List<? extends Bay> listBays();

    /**
     * Creates a new bay
     *
     * @return newly created bay
     */
    Bay createBay(Bay bay);

    /**
     * Deletes a bay
     */
    ActionResponse deleteBay(String id);

    /**
     * Show a bay
     *
     * @return bay
     */
    Bay showBay(String id);

    /**
     * Update bay
     */
    Bay updateBay(String id, String operations);

    // Containers

    /**
     * Lists all Containers
     *
     * @return list of Containers
     */
    List<? extends Container> listContainers();

    /**
     * Creates a new Container
     *
     * @return newly created Container
     */
    Container createContainer(Container container);

    /**
     * Deletes a Container
     *
     * @return success/failure response
     */
    ActionResponse deleteContainer(String uuid);

    /**
     * Execute command in a Container
     *
     * @return output of the command
     */
    String execCmdInContainer(String id, String cmd);

    /**
     * Get logs of a Container
     *
     * @return logs
     */
    String getContainerLogs(String id);

    /**
     * Pause a Container
     *
     * @return paused Container
     */
    Container pauseContainer(String id);

    /**
     * Unpause a Container
     *
     * @return paused Container
     */
    Container unpauseContainer(String id);

    /**
     * Reboot a Container
     *
     * @return rebooted Container
     */
    Container rebootContainer(String id);

    /**
     * Start a Container
     *
     * @return Container
     */
    Container startContainer(String id);

    /**
     * Stop a Container
     *
     * @return Container
     */
    Container stopContainer(String id);

    /**
     * Show a Container
     *
     * @return Container
     */
    Container showContainer(String id);

    /**
     * Update a container
     */
    Container updateContainer(String id, String operations);

    /**
     * Gets certificate
     *
     * @param uuid of a bay or cluster
     * @return certificate
     */
    Certificate getCertificate(String uuid);

    /**
     * Generates certificate
     *
     * @param ca request
     * @return certificate
     */
    Certificate signCertificate(Carequest ca);

    /**
     * Rotate the CA certificate for a bay/cluster and
     * invalidate all user certificates.
     *
     * @param uuid of a bay or cluster
     * @return status
     */
    ActionResponse rotateCertificate(String uuid);

    //New clusters APIs

    /**
     * Create new cluster based on cluster template
     */
    Cluster createCluster(Cluster cluster);

    /**
     * List all clusters
     */
    List<? extends Cluster> listClusters();

    /**
     * Get all information of a cluster in Magnum
     *
     * @param id of the cluster
     * @return cluster
     */
    Cluster showCluster(String id);

    /**
     * Delete a cluster.
     *
     * @param id of the cluster
     * @return success status
     */
    ActionResponse deleteCluster(String id);

    /**
     * Update information of one cluster attributes using operations
     * including: ``add``, ``replace`` or ``remove``.
     * The attributes to ``add`` and
     * ``replace`` in the form of ``key=value`` while ``remove`` only needs the keys.
     *
     * @return updated cluster
     */
    Cluster updateCluster(String id, String operations);

    //Cluster templates

    /**
     * Create new cluster template
     *
     * @return newly created cluster template
     */
    Clustertemplate createClustertemplate(Clustertemplate template);

    /**
     * List all cluster templates
     *
     * @return list of cluster templates
     */
    List<? extends Clustertemplate> listClustertemplate();

    /**
     * Delete a cluster template
     *
     * @param id of the clustertemplate
     * @return status
     */
    ActionResponse deleteClustertemplate(String id);

    /**
     * Update information of one cluster template attributes using operations
     * including: ``add``, ``replace`` or ``remove``. The attributes to ``add`` and
     * ``replace`` in the form of ``key=value`` while ``remove`` only needs the keys.
     *
     * @param cluster template
     * @return updated clustser template
     */
    Clustertemplate updateClustertemplate(String id, String operations);

    // Pod (group of containers) APIs

    /**
     * List all pods
     *
     * @return list of pods
     */
    List<? extends Pod> listPods(String bayUuid);

    /**
     * Create a pod
     *
     * @return pod
     */
    Pod createPod(Pod pod);

    /**
     * Delete a pod
     *
     * @return status
     */
    ActionResponse deletePod(String bayUuid, String id);

    /**
     * Show a pod
     *
     * @return pod
     */
    Pod showPod(String bayUuid, String id);

    /**
     * Update a pod
     *
     * @return pod
     */
    Pod updatePod(String bayUuid, String id, String operations);


}
