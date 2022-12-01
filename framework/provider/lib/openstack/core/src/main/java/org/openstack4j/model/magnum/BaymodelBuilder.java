package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.openstack.common.GenericLink;

import java.util.List;

public interface BaymodelBuilder extends Builder<BaymodelBuilder, Baymodel> {
    /**
     * @see Baymodel#getInsecureRegistry()
     */
    BaymodelBuilder insecureRegistry(String insecureRegistry);

    /**
     * @see Baymodel#getLinks()
     */
    BaymodelBuilder links(List<GenericLink> links);

    /**
     * @see Baymodel#getHttpProxy()
     */
    BaymodelBuilder httpProxy(String httpProxy);

    /**
     * @see Baymodel#getUpdatedAt()
     */
    BaymodelBuilder updatedAt(String updatedAt);

    /**
     * @see Baymodel#getisFloatingIpEnabled()
     */
    BaymodelBuilder isFloatingIpEnabled(Boolean floatingIpEnabled);

    /**
     * @see Baymodel#getFixedSubnet()
     */
    BaymodelBuilder fixedSubnet(String fixedSubnet);

    /**
     * @see Baymodel#getMasterFlavorId()
     */
    BaymodelBuilder masterFlavorId(String masterFlavorId);

    /**
     * @see Baymodel#getUuid()
     */
    BaymodelBuilder uuid(String uuid);

    /**
     * @see Baymodel#getNoProxy()
     */
    BaymodelBuilder noProxy(String noProxy);

    /**
     * @see Baymodel#getHttpsProxy()
     */
    BaymodelBuilder httpsProxy(String httpsProxy);

    /**
     * @see Baymodel#getTlsDisabled()
     */
    BaymodelBuilder tlsDisabled(Boolean tlsDisabled);

    /**
     * @see Baymodel#getKeypairId()
     */
    BaymodelBuilder keypairId(String keypairId);

    /**
     * @see Baymodel#getPublicBaymodel()
     */
    BaymodelBuilder publicBaymodel(Boolean publicBaymodel);

    /**
     * @see Baymodel#getDockerVolumeSize()
     */
    BaymodelBuilder dockerVolumeSize(String dockerVolumeSize);

    /**
     * @see Baymodel#getServerType()
     */
    BaymodelBuilder serverType(String serverType);

    /**
     * @see Baymodel#getExternalNetworkId()
     */
    BaymodelBuilder externalNetworkId(String externalNetworkId);

    /**
     * @see Baymodel#getClusterDistro()
     */
    BaymodelBuilder clusterDistro(String clusterDistro);

    /**
     * @see Baymodel#getImageId()
     */
    BaymodelBuilder imageId(String imageId);

    /**
     * @see Baymodel#getVolumeDriver()
     */
    BaymodelBuilder volumeDriver(String volumeDriver);

    /**
     * @see Baymodel#getRegistryEnabled()
     */
    BaymodelBuilder registryEnabled(Boolean registryEnabled);

    /**
     * @see Baymodel#getDockerStorageDriver()
     */
    BaymodelBuilder dockerStorageDriver(String dockerStorageDriver);

    /**
     * @see Baymodel#getApiserverPort()
     */
    BaymodelBuilder apiserverPort(String apiserverPort);

    /**
     * @see Baymodel#getName()
     */
    BaymodelBuilder name(String name);

    /**
     * @see Baymodel#getCreatedAt()
     */
    BaymodelBuilder createdAt(String createdAt);

    /**
     * @see Baymodel#getNetworkDriver()
     */
    BaymodelBuilder networkDriver(String networkDriver);

    /**
     * @see Baymodel#getFixedNetwork()
     */
    BaymodelBuilder fixedNetwork(String fixedNetwork);

    /**
     * @see Baymodel#getCoe()
     */
    BaymodelBuilder coe(String coe);

    /**
     * @see Baymodel#getFlavorId()
     */
    BaymodelBuilder flavorId(String flavorId);

    /**
     * @see Baymodel#getMasterLbEnabled()
     */
    BaymodelBuilder masterLbEnabled(Boolean masterLbEnabled);

    /**
     * @see Baymodel#getDnsNameserver()
     */
    BaymodelBuilder dnsNameserver(String dnsNameserver);

}
