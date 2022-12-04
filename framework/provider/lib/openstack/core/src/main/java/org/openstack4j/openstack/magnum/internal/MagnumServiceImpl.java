package org.openstack4j.openstack.magnum.internal;


import org.openstack4j.api.magnum.MagnumService;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.magnum.*;
import org.openstack4j.openstack.internal.BaseOpenStackService;
import org.openstack4j.openstack.magnum.*;
import org.openstack4j.openstack.magnum.MagnumBay.BayConcreteBuilder.Bays;
import org.openstack4j.openstack.magnum.MagnumBaymodel.Baymodels;
import org.openstack4j.openstack.magnum.MagnumCluster.Clusters;
import org.openstack4j.openstack.magnum.MagnumClustertemplate.Clustertemplates;
import org.openstack4j.openstack.magnum.MagnumContainer.Containers;
import org.openstack4j.openstack.magnum.MagnumMservice.Mservices;
import org.openstack4j.openstack.magnum.MagnumPod.Pods;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.openstack4j.core.transport.ClientConstants.*;


/**
 * OpenStack Container Infrastructure Management service (Magnum) APIs
 *
 * @author Sohan Sangwan
 */
public class MagnumServiceImpl extends BaseOpenStackService implements MagnumService {

    /**
     * Lists Magnum Services
     */
    @Override
    public List<? extends Mservice> listMservices() {
        return get(Mservices.class, uri(MAGNUM_MSERVICES)).serviceType(ServiceType.MAGNUM).execute().getList();
    }

    @Override
    public List<? extends Baymodel> listBaymodels() {
        return get(Baymodels.class, uri(MAGNUM_BAYMODELS)).serviceType(ServiceType.MAGNUM).execute().getList();
    }

    @Override
    public Baymodel createBaymodel(Baymodel baymodel) {
        checkNotNull(baymodel);
        return post(MagnumBaymodel.class, MAGNUM_BAYMODELS).serviceType(ServiceType.MAGNUM).entity(baymodel).execute();
    }

    @Override
    public ActionResponse deleteBaymodel(String id) {
        checkNotNull(id);
        return deleteWithResponse(MAGNUM_BAYMODELS, "/", id).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Baymodel showBaymodel(String id) {
        checkNotNull(id);
        return get(MagnumBaymodel.class, MAGNUM_BAYMODELS, "/", id).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Baymodel updateBaymodel(String id, String operations) {
        checkNotNull(id);
        checkNotNull(operations);
        return patch(MagnumBaymodel.class, MAGNUM_BAYMODELS, "/", id).serviceType(ServiceType.MAGNUM).json(operations).execute();
    }

    @Override
    public List<? extends Bay> listBays() {
        return get(Bays.class, uri(MAGNUM_BAYS)).serviceType(ServiceType.MAGNUM).execute().getList();
    }

    @Override
    public Bay createBay(Bay bay) {
        checkNotNull(bay);
        return post(MagnumBay.class, MAGNUM_BAYS).serviceType(ServiceType.MAGNUM).entity(bay).execute();
    }

    @Override
    public ActionResponse deleteBay(String id) {
        checkNotNull(id);
        return deleteWithResponse(MAGNUM_BAYS, "/", id).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Bay showBay(String id) {
        checkNotNull(id);
        return get(MagnumBay.class, MAGNUM_BAYS, "/", id).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Bay updateBay(String id, String operations) {
        checkNotNull(id);
        checkNotNull(operations);
        return patch(MagnumBay.class, MAGNUM_BAYS, "/", id).serviceType(ServiceType.MAGNUM).json(operations).execute();
    }

    //Containers
    @Override
    public List<? extends Container> listContainers() {
        return get(Containers.class, uri(MAGNUM_CONTAINERS)).serviceType(ServiceType.MAGNUM).execute().getList();
    }

    @Override
    public Container createContainer(Container container) {
        checkNotNull(container);
        return post(MagnumContainer.class, MAGNUM_CONTAINERS).serviceType(ServiceType.MAGNUM).entity(container).execute();
    }

    @Override
    public ActionResponse deleteContainer(String uuid) {
        checkNotNull(uuid);
        return deleteWithResponse(MAGNUM_CONTAINERS, "/", uuid).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public String execCmdInContainer(String id, String cmd) {
        checkNotNull(id);
        checkNotNull(cmd);
        ///v1/containers/%s/execute?command=ls
        return put(String.class, uri("%s/execute?command=%s", MAGNUM_CONTAINERS, id, cmd)).serviceType(ServiceType.MAGNUM).execute();

    }

    @Override
    public String getContainerLogs(String id) {
        checkNotNull(id);
        return get(String.class, uri("%s/%s/logs", MAGNUM_CONTAINERS, id)).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Container pauseContainer(String id) {
        checkNotNull(id);
        return put(MagnumContainer.class, uri("%s/%s/pause", MAGNUM_CONTAINERS, id)).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Container unpauseContainer(String id) {
        checkNotNull(id);
        return put(MagnumContainer.class, uri("%s/%s/unpause", MAGNUM_CONTAINERS, id)).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Container rebootContainer(String id) {
        checkNotNull(id);
        return put(MagnumContainer.class, uri("%s/%s/reboot", MAGNUM_CONTAINERS, id)).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Container startContainer(String id) {
        checkNotNull(id);
        return put(MagnumContainer.class, uri("%s/%s/start", MAGNUM_CONTAINERS, id)).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Container stopContainer(String id) {
        checkNotNull(id);
        return put(MagnumContainer.class, uri("%s/%s/stop", MAGNUM_CONTAINERS, id)).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Container showContainer(String id) {
        checkNotNull(id);
        return get(MagnumContainer.class, uri("%s/%s", MAGNUM_CONTAINERS, id)).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Container updateContainer(String id, String operations) {
        checkNotNull(id);
        checkNotNull(operations);
        return patch(MagnumContainer.class, MAGNUM_CONTAINERS, "/", id).serviceType(ServiceType.MAGNUM).json(operations).execute();

    }

    @Override
    public Certificate getCertificate(String uuid) {
        checkNotNull(uuid);
        return get(MagnumCertificate.class, uri("%s/%s", MAGNUM_CERTIFICATES, uuid)).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Certificate signCertificate(Carequest ca) {
        checkNotNull(ca);
        return post(MagnumCertificate.class, MAGNUM_CERTIFICATES).serviceType(ServiceType.MAGNUM).entity(ca).execute();
    }

    @Override
    public ActionResponse rotateCertificate(String uuid) {
        checkNotNull(uuid);
        return patch(ActionResponse.class, uri("%s/%s", MAGNUM_CERTIFICATES, uuid)).serviceType(ServiceType.MAGNUM).execute();
    }

    //Clusters

    @Override
    public Cluster createCluster(Cluster cluster) {
        checkNotNull(cluster);
        return post(MagnumCluster.class, MAGNUM_CLUSTERS).serviceType(ServiceType.MAGNUM).entity(cluster).execute();
    }

    @Override
    public List<? extends Cluster> listClusters() {
        return get(Clusters.class, uri(MAGNUM_CLUSTERS)).serviceType(ServiceType.MAGNUM).execute().getList();
    }

    @Override
    public Cluster showCluster(String id) {
        checkNotNull(id);
        return get(Cluster.class, uri("%s/%s", MAGNUM_CLUSTERS, id)).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public ActionResponse deleteCluster(String id) {
        checkNotNull(id);
        return deleteWithResponse(MAGNUM_CLUSTERS, "/", id).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Cluster updateCluster(String id, String operations) {
        checkNotNull(id);
        checkNotNull(operations);
        return patch(MagnumCluster.class, MAGNUM_CLUSTERS, "/", id).serviceType(ServiceType.MAGNUM).json(operations).execute();
    }

    //Cluster template APIs

    @Override
    public Clustertemplate createClustertemplate(Clustertemplate template) {
        checkNotNull(template);
        return post(MagnumClustertemplate.class, MAGNUM_CLUSTERTEMPLATES).serviceType(ServiceType.MAGNUM).entity(template).execute();
    }

    @Override
    public List<? extends Clustertemplate> listClustertemplate() {
        return get(Clustertemplates.class, uri(MAGNUM_CLUSTERTEMPLATES)).serviceType(ServiceType.MAGNUM).execute().getList();
    }

    @Override
    public ActionResponse deleteClustertemplate(String id) {
        checkNotNull(id);
        return deleteWithResponse(MAGNUM_CLUSTERTEMPLATES, "/", id).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Clustertemplate updateClustertemplate(String id, String operations) {
        checkNotNull(id);
        checkNotNull(operations);
        return patch(MagnumClustertemplate.class, MAGNUM_CLUSTERTEMPLATES, "/", id).serviceType(ServiceType.MAGNUM).json(operations).execute();
    }

    @Override
    public List<? extends Pod> listPods(String bayUuid) {
        checkNotNull(bayUuid);
        // url: '/v1/pods/?bay_ident=%s'
        return get(Pods.class, uri("%s/?bay_ident=%s", MAGNUM_PODS, bayUuid)).serviceType(ServiceType.MAGNUM).execute().getList();
    }

    @Override
    public Pod createPod(Pod pod) {
        checkNotNull(pod);
        return post(MagnumPod.class, MAGNUM_PODS).serviceType(ServiceType.MAGNUM).entity(pod).execute();
    }

    @Override
    public ActionResponse deletePod(String bayUuid, String id) {
        checkNotNull(bayUuid);
        checkNotNull(id);

        // Url: '/v1/pods/%s/?bay_ident=%s'
        return deleteWithResponse(uri("%s/%s/?bay_ident=%s", MAGNUM_PODS, id, bayUuid)).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Pod showPod(String bayUuid, String id) {
        checkNotNull(bayUuid);
        checkNotNull(id);

        // Url: '/v1/pods/%s/?bay_ident=%s'
        return get(MagnumPod.class, uri("%s/?bay_ident=%s", MAGNUM_PODS, bayUuid)).serviceType(ServiceType.MAGNUM).execute();
    }

    @Override
    public Pod updatePod(String bayUuid, String id, String operations) {
        checkNotNull(id);
        checkNotNull(operations);
        return patch(MagnumPod.class, MAGNUM_PODS, "/", id).serviceType(ServiceType.MAGNUM).json(operations).execute();
    }


}
