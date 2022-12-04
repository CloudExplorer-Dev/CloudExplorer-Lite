package org.openstack4j.openstack.magnum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.magnum.Baymodel;
import org.openstack4j.model.magnum.BaymodelBuilder;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnumBaymodel implements Baymodel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @JsonProperty("insecure_registry")
    private String insecureRegistry;
    @JsonProperty("links")
    private List<GenericLink> links;
    @JsonProperty("http_proxy")
    private String httpProxy;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("floating_ip_enabled")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean floatingIpEnabled;
    @JsonProperty("fixed_subnet")
    private String fixedSubnet;
    @JsonProperty("master_flavor_id")
    private String masterFlavorId;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("no_proxy")
    private String noProxy;
    @JsonProperty("https_proxy")
    private String httpsProxy;
    @JsonProperty("tls_disabled")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean tlsDisabled;
    @JsonProperty("keypair_id")
    private String keypairId;
    @JsonProperty("public")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean publicBaymodel;
    //@JsonProperty("labels")
    @JsonProperty("docker_volume_size")
    private String dockerVolumeSize;
    @JsonProperty("server_type")
    private String serverType;
    @JsonProperty("external_network_id")
    private String externalNetworkId;
    @JsonProperty("cluster_distro")
    private String clusterDistro;
    @JsonProperty("image_id")
    private String imageId;
    @JsonProperty("volume_driver")
    private String volumeDriver;
    @JsonProperty("registry_enabled")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean registryEnabled;
    @JsonProperty("docker_storage_driver")
    private String dockerStorageDriver;
    @JsonProperty("apiserver_port")
    private String apiserverPort;
    @JsonProperty("name")
    private String name;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("network_driver")
    private String networkDriver;
    @JsonProperty("fixed_network")
    private String fixedNetwork;
    @JsonProperty("coe")
    private String coe;
    @JsonProperty("flavor_id")
    private String flavorId;
    @JsonProperty("master_lb_enabled")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean masterLbEnabled;
    @JsonProperty("dns_nameserver")
    private String dnsNameserver;

    public static BaymodelBuilder builder() {
        return new BaymodelConcreteBuilder();
    }

    @Override
    public BaymodelBuilder toBuilder() {

        return new BaymodelConcreteBuilder(this);
    }

    public String getInsecureRegistry() {
        return insecureRegistry;
    }

    public List<GenericLink> getLinks() {
        return links;
    }

    public String getHttpProxy() {
        return httpProxy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Boolean isFloatingIpEnabled() {
        return floatingIpEnabled;
    }

    public String getFixedSubnet() {
        return fixedSubnet;
    }

    public String getMasterFlavorId() {
        return masterFlavorId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getNoProxy() {
        return noProxy;
    }

    public String getHttpsProxy() {
        return httpsProxy;
    }

    public Boolean isTlsDisabled() {
        return tlsDisabled;
    }

    public String getKeypairId() {
        return keypairId;
    }

    public Boolean isPublicBaymodel() {
        return publicBaymodel;
    }

    public String getDockerVolumeSize() {
        return dockerVolumeSize;
    }

    public String getServerType() {
        return serverType;
    }

    public String getExternalNetworkId() {
        return externalNetworkId;
    }

    public String getClusterDistro() {
        return clusterDistro;
    }

    public String getImageId() {
        return imageId;
    }

    public String getVolumeDriver() {
        return volumeDriver;
    }

    public Boolean isRegistryEnabled() {
        return registryEnabled;
    }

    public String getDockerStorageDriver() {
        return dockerStorageDriver;
    }

    public String getApiserverPort() {
        return apiserverPort;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getNetworkDriver() {
        return networkDriver;
    }

    public String getFixedNetwork() {
        return fixedNetwork;
    }

    public String getCoe() {
        return coe;
    }

    public String getFlavorId() {
        return flavorId;
    }

    public Boolean isMasterLbEnabled() {
        return masterLbEnabled;
    }

    public String getDnsNameserver() {
        return dnsNameserver;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("insecureRegistry", insecureRegistry)
                .add("links", links)
                .add("httpProxy", httpProxy)
                .add("updatedAt", updatedAt)
                .add("floatingIpEnabled", floatingIpEnabled)
                .add("fixedSubnet", fixedSubnet)
                .add("masterFlavorId", masterFlavorId)
                .add("uuid", uuid)
                .add("noProxy", noProxy)
                .add("httpsProxy", httpsProxy)
                .add("tlsDisabled", tlsDisabled)
                .add("keypairId", keypairId)
                .add("publicBaymodel", publicBaymodel)
                .add("dockerVolumeSize", dockerVolumeSize)
                .add("serverType", serverType)
                .add("externalNetworkId", externalNetworkId)
                .add("clusterDistro", clusterDistro)
                .add("imageId", imageId)
                .add("volumeDriver", volumeDriver)
                .add("registryEnabled", registryEnabled)
                .add("dockerStorageDriver", dockerStorageDriver)
                .add("apiserverPort", apiserverPort)
                .add("name", name)
                .add("createdAt", createdAt)
                .add("networkDriver", networkDriver)
                .add("fixedNetwork", fixedNetwork)
                .add("coe", coe)
                .add("flavorId", flavorId)
                .add("masterLbEnabled", masterLbEnabled)
                .add("dnsNameserver", dnsNameserver)
                .toString();
    }

    /**
     * Concrete builder containing MagnumBaymodel as model
     */
    public static class BaymodelConcreteBuilder implements BaymodelBuilder {

        MagnumBaymodel model;

        public BaymodelConcreteBuilder() {
            this(new MagnumBaymodel());
        }

        public BaymodelConcreteBuilder(MagnumBaymodel model) {
            this.model = model;
        }

        @Override
        public Baymodel build() {
            return model;
        }

        @Override
        public BaymodelBuilder from(Baymodel in) {
            if (in != null)
                this.model = (MagnumBaymodel) in;
            return this;
        }

        @Override
        public BaymodelBuilder insecureRegistry(String insecureRegistry) {
            model.insecureRegistry = insecureRegistry;
            return this;
        }

        @Override
        public BaymodelBuilder links(List<GenericLink> links) {
            model.links = links;
            return this;
        }

        @Override
        public BaymodelBuilder httpProxy(String httpProxy) {
            model.httpProxy = httpProxy;
            return this;
        }

        @Override
        public BaymodelBuilder updatedAt(String updatedAt) {
            model.updatedAt = updatedAt;
            return this;
        }

        @Override
        public BaymodelBuilder isFloatingIpEnabled(Boolean floatingIpEnabled) {
            model.floatingIpEnabled = floatingIpEnabled;
            return this;
        }

        @Override
        public BaymodelBuilder fixedSubnet(String fixedSubnet) {
            model.fixedSubnet = fixedSubnet;
            return this;
        }

        @Override
        public BaymodelBuilder masterFlavorId(String masterFlavorId) {
            model.masterFlavorId = masterFlavorId;
            return this;
        }

        @Override
        public BaymodelBuilder uuid(String uuid) {
            model.uuid = uuid;
            return this;
        }

        @Override
        public BaymodelBuilder noProxy(String noProxy) {
            model.noProxy = noProxy;
            return this;
        }

        @Override
        public BaymodelBuilder httpsProxy(String httpsProxy) {
            model.httpsProxy = httpsProxy;
            return this;
        }

        @Override
        public BaymodelBuilder tlsDisabled(Boolean tlsDisabled) {
            model.tlsDisabled = tlsDisabled;
            return this;
        }

        @Override
        public BaymodelBuilder keypairId(String keypairId) {
            model.keypairId = keypairId;
            return this;
        }

        @Override
        public BaymodelBuilder publicBaymodel(Boolean publicBaymodel) {
            model.publicBaymodel = publicBaymodel;
            return this;
        }

        @Override
        public BaymodelBuilder dockerVolumeSize(String dockerVolumeSize) {
            model.dockerVolumeSize = dockerVolumeSize;
            return this;
        }

        @Override
        public BaymodelBuilder serverType(String serverType) {
            model.serverType = serverType;
            return this;
        }

        @Override
        public BaymodelBuilder externalNetworkId(String externalNetworkId) {
            model.externalNetworkId = externalNetworkId;
            return this;
        }

        @Override
        public BaymodelBuilder clusterDistro(String clusterDistro) {
            model.clusterDistro = clusterDistro;
            return this;
        }

        @Override
        public BaymodelBuilder imageId(String imageId) {
            model.imageId = imageId;
            return this;
        }

        @Override
        public BaymodelBuilder volumeDriver(String volumeDriver) {
            model.volumeDriver = volumeDriver;
            return this;
        }

        @Override
        public BaymodelBuilder registryEnabled(Boolean registryEnabled) {
            model.registryEnabled = registryEnabled;
            return this;
        }

        @Override
        public BaymodelBuilder dockerStorageDriver(String dockerStorageDriver) {
            model.dockerStorageDriver = dockerStorageDriver;
            return this;
        }

        @Override
        public BaymodelBuilder apiserverPort(String apiserverPort) {
            model.apiserverPort = apiserverPort;
            return this;
        }

        @Override
        public BaymodelBuilder name(String name) {
            model.name = name;
            return this;
        }

        @Override
        public BaymodelBuilder createdAt(String createdAt) {
            model.createdAt = createdAt;
            return this;
        }

        @Override
        public BaymodelBuilder networkDriver(String networkDriver) {
            model.networkDriver = networkDriver;
            return this;
        }

        @Override
        public BaymodelBuilder fixedNetwork(String fixedNetwork) {
            model.fixedNetwork = fixedNetwork;
            return this;
        }

        @Override
        public BaymodelBuilder coe(String coe) {
            model.coe = coe;
            return this;
        }

        @Override
        public BaymodelBuilder flavorId(String flavorId) {
            model.flavorId = flavorId;
            return this;
        }

        @Override
        public BaymodelBuilder masterLbEnabled(Boolean masterLbEnabled) {
            model.masterLbEnabled = masterLbEnabled;
            return this;
        }

        @Override
        public BaymodelBuilder dnsNameserver(String dnsNameserver) {
            model.dnsNameserver = dnsNameserver;
            return this;
        }

    }

    /**
     * list of baymodels
     */
    public static class Baymodels extends ListResult<MagnumBaymodel> {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        @JsonProperty("baymodels")
        private List<MagnumBaymodel> list;

        @Override
        public List<MagnumBaymodel> value() {
            return list;
        }

    }

}
