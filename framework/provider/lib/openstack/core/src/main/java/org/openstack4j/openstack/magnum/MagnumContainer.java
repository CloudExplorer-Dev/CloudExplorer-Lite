package org.openstack4j.openstack.magnum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.magnum.Container;
import org.openstack4j.model.magnum.ContainerBuilder;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnumContainer implements Container {
    private static final long serialVersionUID = 1L;
    @JsonProperty("status")
    private String status;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("links")
    private List<GenericLink> links;
    @JsonProperty("image")
    private String image;
    @JsonProperty("environment")
    private MagnumEnvironment environment;
    @JsonProperty("command")
    private String command;
    @JsonProperty("memory")
    private String memory;
    @JsonProperty("bay_uuid")
    private String bayUuid;
    @JsonProperty("name")
    private String name;

    public static ContainerBuilder builder() {
        return new ContainerConcreteBuilder();
    }

    @Override
    public ContainerBuilder toBuilder() {
        return new ContainerConcreteBuilder(this);
    }

    public String getStatus() {
        return status;
    }

    public String getUuid() {
        return uuid;
    }

    public List<GenericLink> getLinks() {
        return links;
    }

    public String getImage() {
        return image;
    }

    public MagnumEnvironment getEnvironment() {
        return environment;
    }

    public String getCommand() {
        return command;
    }

    public String getMemory() {
        return memory;
    }

    public String getBayUuid() {
        return bayUuid;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("status", status).add("uuid", uuid).add("links", links)
                .add("image", image).add("environment", environment).add("command", command).add("memory", memory)
                .add("bayUuid", bayUuid).add("name", name).toString();
    }

    /**
     * Concrete builder containing MagnumContainer as model
     */
    public static class ContainerConcreteBuilder implements ContainerBuilder {
        MagnumContainer model;

        public ContainerConcreteBuilder() {
            this(new MagnumContainer());
        }

        public ContainerConcreteBuilder(MagnumContainer model) {
            this.model = model;
        }

        @Override
        public Container build() {
            return model;
        }

        @Override
        public ContainerBuilder from(Container in) {
            if (in != null)
                this.model = (MagnumContainer) in;
            return this;
        }

        @Override
        public ContainerBuilder status(String status) {
            model.status = status;
            return this;
        }

        @Override
        public ContainerBuilder uuid(String uuid) {
            model.uuid = uuid;
            return this;
        }

        @Override
        public ContainerBuilder links(List<GenericLink> links) {
            model.links = links;
            return this;
        }

        @Override
        public ContainerBuilder image(String image) {
            model.image = image;
            return this;
        }

        @Override
        public ContainerBuilder environment(MagnumEnvironment environment) {
            model.environment = environment;
            return this;
        }

        @Override
        public ContainerBuilder command(String command) {
            model.command = command;
            return this;
        }

        @Override
        public ContainerBuilder memory(String memory) {
            model.memory = memory;
            return this;
        }

        @Override
        public ContainerBuilder bayUuid(String bayUuid) {
            model.bayUuid = bayUuid;
            return this;
        }

        @Override
        public ContainerBuilder name(String name) {
            model.name = name;
            return this;
        }
    }

    public static class Containers extends ListResult<MagnumContainer> {
        private static final long serialVersionUID = 1L;
        @JsonProperty("containers")
        private List<MagnumContainer> list;

        @Override
        public List<MagnumContainer> value() {
            return list;
        }
    }
}
