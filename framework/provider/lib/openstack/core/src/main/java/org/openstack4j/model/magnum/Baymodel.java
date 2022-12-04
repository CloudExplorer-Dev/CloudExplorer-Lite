package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.openstack.common.GenericLink;

import java.util.List;

public interface Baymodel extends ModelEntity, Buildable<BaymodelBuilder> {
    /**
     * Insecure registry
     *
     * @return InsecureRegistry
     */
    String getInsecureRegistry();

    /**
     * All links
     *
     * @return links
     */
    List<GenericLink> getLinks();

    /**
     * Http proxy
     *
     * @return httpProxy
     */
    String getHttpProxy();

    /**
     * Updated time
     *
     * @return updatedAt
     */
    String getUpdatedAt();

    /**
     * Is Floating IP enabled
     *
     * @return floatingIpEnabled
     */
    Boolean isFloatingIpEnabled();

    /**
     * Fixed subnet
     *
     * @return fixedSubnet
     */
    String getFixedSubnet();

    /**
     * Master flavor id
     *
     * @return masterFlavorId
     */
    String getMasterFlavorId();

    /**
     * UUID
     *
     * @return uuid
     */
    String getUuid();

    /**
     * No proxy
     *
     * @return noProxy
     */
    String getNoProxy();

    /**
     * Http proxy
     *
     * @return http proxy
     */
    String getHttpsProxy();

    /**
     * Is TLS disabled
     *
     * @return tlsDisabled boolean value
     */
    Boolean isTlsDisabled();

    /**
     * Keypair id
     *
     * @return keypairId
     */
    String getKeypairId();

    /**
     * Is it public
     *
     * @return public boolean vallue
     */
    Boolean isPublicBaymodel();

    /**
     * Docker volume size
     *
     * @return dockerVolumeSize
     */
    String getDockerVolumeSize();

    /**
     * Server type
     *
     * @return serverType
     */
    String getServerType();

    /**
     * External network id
     *
     * @return externalNetworkId
     */
    String getExternalNetworkId();

    /**
     * Cluster distro
     *
     * @return clusterDistro
     */
    String getClusterDistro();

    /**
     * Imdage id
     *
     * @return imageId
     */
    String getImageId();

    /**
     * Volume driver
     *
     * @return volume driver
     */
    String getVolumeDriver();

    /**
     * Is registry enabled
     *
     * @return registryEnabled boolean value
     */
    Boolean isRegistryEnabled();

    /**
     * Docker storage driver
     *
     * @return dockerStorageDriver
     */
    String getDockerStorageDriver();

    /**
     * Api server port
     *
     * @return apiserverPort
     */
    String getApiserverPort();

    /**
     * Name
     *
     * @return name
     */
    String getName();

    /**
     * Date of creation
     *
     * @return createdAt
     */
    String getCreatedAt();

    /**
     * Network driver
     *
     * @return networkDriver
     */
    String getNetworkDriver();

    /**
     * Fixed network
     *
     * @return fixedNetwork
     */
    String getFixedNetwork();

    /**
     * Coe
     *
     * @return coe
     */
    String getCoe();

    /**
     * Flavor id
     *
     * @return flavorId
     */
    String getFlavorId();

    /**
     * Is master lb enabled
     *
     * @return masterLbEnabled boolean value
     */
    Boolean isMasterLbEnabled();

    /**
     * DNS name server
     *
     * @return dnsNameServer
     */
    String getDnsNameserver();
}
