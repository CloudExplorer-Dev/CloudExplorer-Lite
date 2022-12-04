package org.openstack4j.openstack.magnum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.magnum.Clustertemplate;
import org.openstack4j.model.magnum.ClustertemplateBuilder;
import org.openstack4j.model.magnum.Label;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnumClustertemplate implements Clustertemplate {
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
    @JsonProperty("public_template")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean publicTemplate;
    @JsonProperty("labels")
    private Label labels;
    @JsonProperty("docker_volume_size")
    private Integer dockerVolumeSize;
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


    public static ClustertemplateBuilder builder() {
        return new ClustertemplateConcreteBuilder();
    }

    @Override
    public ClustertemplateBuilder toBuilder() {
        return new ClustertemplateConcreteBuilder(this);
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

    public Boolean getFloatingIpEnabled() {
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

    public Boolean getTlsDisabled() {
        return tlsDisabled;
    }

    public String getKeypairId() {
        return keypairId;
    }

    public Boolean getPublicTemplate() {
        return publicTemplate;
    }

    public Label getLabels() {
        return labels;
    }

    public Integer getDockerVolumeSize() {
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

    public Boolean getRegistryEnabled() {
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

    public Boolean getMasterLbEnabled() {
        return masterLbEnabled;
    }

    public String getDnsNameserver() {
        return dnsNameserver;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("insecureRegistry", insecureRegistry)
                .add("links", links).add("httpProxy", httpProxy).add("updatedAt", updatedAt)
                .add("floatingIpEnabled", floatingIpEnabled).add("fixedSubnet", fixedSubnet)
                .add("masterFlavorId", masterFlavorId).add("uuid", uuid).add("noProxy", noProxy)
                .add("httpsProxy", httpsProxy).add("tlsDisabled", tlsDisabled).add("keypairId", keypairId)
                .add("publicTemplate", publicTemplate).add("labels", labels).add("dockerVolumeSize", dockerVolumeSize)
                .add("serverType", serverType).add("externalNetworkId", externalNetworkId)
                .add("clusterDistro", clusterDistro).add("imageId", imageId).add("volumeDriver", volumeDriver)
                .add("registryEnabled", registryEnabled).add("dockerStorageDriver", dockerStorageDriver)
                .add("apiserverPort", apiserverPort).add("name", name).add("createdAt", createdAt)
                .add("networkDriver", networkDriver).add("fixedNetwork", fixedNetwork).add("coe", coe)
                .add("flavorId", flavorId).add("masterLbEnabled", masterLbEnabled).add("dnsNameserver", dnsNameserver)
                .toString();
    }

    /**
     * Concrete builder containing MagnumClustertemplate as model
     */
    public static class ClustertemplateConcreteBuilder implements ClustertemplateBuilder {
        MagnumClustertemplate model;

        public ClustertemplateConcreteBuilder() {
            this(new MagnumClustertemplate());
        }

        public ClustertemplateConcreteBuilder(MagnumClustertemplate model) {
            this.model = model;
        }

        @Override
        public Clustertemplate build() {
            return model;
        }

        @Override
        public ClustertemplateBuilder from(Clustertemplate in) {
            if (in != null)
                this.model = (MagnumClustertemplate) in;
            return this;
        }

        @Override
        public ClustertemplateBuilder insecureRegistry(String insecureRegistry) {
            model.insecureRegistry = insecureRegistry;
            return this;
        }

        @Override
        public ClustertemplateBuilder links(List<GenericLink> links) {
            model.links = links;
            return this;
        }

        @Override
        public ClustertemplateBuilder httpProxy(String httpProxy) {
            model.httpProxy = httpProxy;
            return this;
        }

        @Override
        public ClustertemplateBuilder updatedAt(String updatedAt) {
            model.updatedAt = updatedAt;
            return this;
        }

        @Override
        public ClustertemplateBuilder floatingIpEnabled(Boolean floatingIpEnabled) {
            model.floatingIpEnabled = floatingIpEnabled;
            return this;
        }

        @Override
        public ClustertemplateBuilder fixedSubnet(String fixedSubnet) {
            model.fixedSubnet = fixedSubnet;
            return this;
        }

        @Override
        public ClustertemplateBuilder masterFlavorId(String masterFlavorId) {
            model.masterFlavorId = masterFlavorId;
            return this;
        }

        @Override
        public ClustertemplateBuilder uuid(String uuid) {
            model.uuid = uuid;
            return this;
        }

        @Override
        public ClustertemplateBuilder noProxy(String noProxy) {
            model.noProxy = noProxy;
            return this;
        }

        @Override
        public ClustertemplateBuilder httpsProxy(String httpsProxy) {
            model.httpsProxy = httpsProxy;
            return this;
        }

        @Override
        public ClustertemplateBuilder tlsDisabled(Boolean tlsDisabled) {
            model.tlsDisabled = tlsDisabled;
            return this;
        }

        @Override
        public ClustertemplateBuilder keypairId(String keypairId) {
            model.keypairId = keypairId;
            return this;
        }

        @Override
        public ClustertemplateBuilder publicTemplate(Boolean publicTemplate) {
            model.publicTemplate = publicTemplate;
            return this;
        }

        @Override
        public ClustertemplateBuilder labels(Label labels) {
            model.labels = labels;
            return this;
        }

        @Override
        public ClustertemplateBuilder dockerVolumeSize(Integer dockerVolumeSize) {
            model.dockerVolumeSize = dockerVolumeSize;
            return this;
        }

        @Override
        public ClustertemplateBuilder serverType(String serverType) {
            model.serverType = serverType;
            return this;
        }

        @Override
        public ClustertemplateBuilder externalNetworkId(String externalNetworkId) {
            model.externalNetworkId = externalNetworkId;
            return this;
        }

        @Override
        public ClustertemplateBuilder clusterDistro(String clusterDistro) {
            model.clusterDistro = clusterDistro;
            return this;
        }

        @Override
        public ClustertemplateBuilder imageId(String imageId) {
            model.imageId = imageId;
            return this;
        }

        @Override
        public ClustertemplateBuilder volumeDriver(String volumeDriver) {
            model.volumeDriver = volumeDriver;
            return this;
        }

        @Override
        public ClustertemplateBuilder registryEnabled(Boolean registryEnabled) {
            model.registryEnabled = registryEnabled;
            return this;
        }

        @Override
        public ClustertemplateBuilder dockerStorageDriver(String dockerStorageDriver) {
            model.dockerStorageDriver = dockerStorageDriver;
            return this;
        }

        @Override
        public ClustertemplateBuilder apiserverPort(String apiserverPort) {
            model.apiserverPort = apiserverPort;
            return this;
        }

        @Override
        public ClustertemplateBuilder name(String name) {
            model.name = name;
            return this;
        }

        @Override
        public ClustertemplateBuilder createdAt(String createdAt) {
            model.createdAt = createdAt;
            return this;
        }

        @Override
        public ClustertemplateBuilder networkDriver(String networkDriver) {
            model.networkDriver = networkDriver;
            return this;
        }

        @Override
        public ClustertemplateBuilder fixedNetwork(String fixedNetwork) {
            model.fixedNetwork = fixedNetwork;
            return this;
        }

        @Override
        public ClustertemplateBuilder coe(String coe) {
            model.coe = coe;
            return this;
        }

        @Override
        public ClustertemplateBuilder flavorId(String flavorId) {
            model.flavorId = flavorId;
            return this;
        }

        @Override
        public ClustertemplateBuilder masterLbEnabled(Boolean masterLbEnabled) {
            model.masterLbEnabled = masterLbEnabled;
            return this;
        }

        @Override
        public ClustertemplateBuilder dnsNameserver(String dnsNameserver) {
            model.dnsNameserver = dnsNameserver;
            return this;
        }
    }

    public static class Clustertemplates extends ListResult<MagnumClustertemplate> {
        private static final long serialVersionUID = 1L;
        @JsonProperty("clustertemplates")
        private List<MagnumClustertemplate> list;

        @Override
        public List<MagnumClustertemplate> value() {
            return list;
        }
    }
}
