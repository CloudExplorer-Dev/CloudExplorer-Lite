package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.openstack.common.GenericLink;

import java.util.List;

public interface ClustertemplateBuilder extends Builder<ClustertemplateBuilder, Clustertemplate> {
    /**
     * @see Clustertemplate#getInsecureRegistry
     */
    ClustertemplateBuilder insecureRegistry(String insecureRegistry);

    /**
     * @see Clustertemplate#getLinks
     */
    ClustertemplateBuilder links(List<GenericLink> links);

    /**
     * @see Clustertemplate#getHttpProxy
     */
    ClustertemplateBuilder httpProxy(String httpProxy);

    /**
     * @see Clustertemplate#getUpdatedAt
     */
    ClustertemplateBuilder updatedAt(String updatedAt);

    /**
     * @see Clustertemplate#getFloatingIpEnabled
     */
    ClustertemplateBuilder floatingIpEnabled(Boolean floatingIpEnabled);

    /**
     * @see Clustertemplate#getFixedSubnet
     */
    ClustertemplateBuilder fixedSubnet(String fixedSubnet);

    /**
     * @see Clustertemplate#getMasterFlavorId
     */
    ClustertemplateBuilder masterFlavorId(String masterFlavorId);

    /**
     * @see Clustertemplate#getUuid
     */
    ClustertemplateBuilder uuid(String uuid);

    /**
     * @see Clustertemplate#getNoProxy
     */
    ClustertemplateBuilder noProxy(String noProxy);

    /**
     * @see Clustertemplate#getHttpsProxy
     */
    ClustertemplateBuilder httpsProxy(String httpsProxy);

    /**
     * @see Clustertemplate#getTlsDisabled
     */
    ClustertemplateBuilder tlsDisabled(Boolean tlsDisabled);

    /**
     * @see Clustertemplate#getKeypairId
     */
    ClustertemplateBuilder keypairId(String keypairId);

    /**
     * @see Clustertemplate#getPublicTemplate
     */
    ClustertemplateBuilder publicTemplate(Boolean publicTemplate);

    /**
     * @see Clustertemplate#getLabels
     */
    ClustertemplateBuilder labels(Label labels);

    /**
     * @see Clustertemplate#getDockerVolumeSize
     */
    ClustertemplateBuilder dockerVolumeSize(Integer dockerVolumeSize);

    /**
     * @see Clustertemplate#getServerType
     */
    ClustertemplateBuilder serverType(String serverType);

    /**
     * @see Clustertemplate#getExternalNetworkId
     */
    ClustertemplateBuilder externalNetworkId(String externalNetworkId);

    /**
     * @see Clustertemplate#getClusterDistro
     */
    ClustertemplateBuilder clusterDistro(String clusterDistro);

    /**
     * @see Clustertemplate#getImageId
     */
    ClustertemplateBuilder imageId(String imageId);

    /**
     * @see Clustertemplate#getVolumeDriver
     */
    ClustertemplateBuilder volumeDriver(String volumeDriver);

    /**
     * @see Clustertemplate#getRegistryEnabled
     */
    ClustertemplateBuilder registryEnabled(Boolean registryEnabled);

    /**
     * @see Clustertemplate#getDockerStorageDriver
     */
    ClustertemplateBuilder dockerStorageDriver(String dockerStorageDriver);

    /**
     * @see Clustertemplate#getApiserverPort
     */
    ClustertemplateBuilder apiserverPort(String apiserverPort);

    /**
     * @see Clustertemplate#getName
     */
    ClustertemplateBuilder name(String name);

    /**
     * @see Clustertemplate#getCreatedAt
     */
    ClustertemplateBuilder createdAt(String createdAt);

    /**
     * @see Clustertemplate#getNetworkDriver
     */
    ClustertemplateBuilder networkDriver(String networkDriver);

    /**
     * @see Clustertemplate#getFixedNetwork
     */
    ClustertemplateBuilder fixedNetwork(String fixedNetwork);

    /**
     * @see Clustertemplate#getCoe
     */
    ClustertemplateBuilder coe(String coe);

    /**
     * @see Clustertemplate#getFlavorId
     */
    ClustertemplateBuilder flavorId(String flavorId);

    /**
     * @see Clustertemplate#getMasterLbEnabled
     */
    ClustertemplateBuilder masterLbEnabled(Boolean masterLbEnabled);

    /**
     * @see Clustertemplate#getDnsNameserver
     */
    ClustertemplateBuilder dnsNameserver(String dnsNameserver);

}
