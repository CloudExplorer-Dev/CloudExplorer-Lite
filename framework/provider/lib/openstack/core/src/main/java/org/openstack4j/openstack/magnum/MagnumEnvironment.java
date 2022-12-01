package org.openstack4j.openstack.magnum;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.magnum.Environment;
import org.openstack4j.model.magnum.EnvironmentBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnumEnvironment implements Environment {
    private static final long serialVersionUID = 1L;
    @JsonProperty("PATH")
    private String path;
    @JsonProperty("LD_LIBRARY_PATH")
    private String ldLibraryPath;

    public static EnvironmentBuilder builder() {
        return new EnvironmentConcreteBuilder();
    }

    @Override
    public EnvironmentBuilder toBuilder() {
        return new EnvironmentConcreteBuilder(this);
    }

    public String getPath() {
        return path;
    }

    public String getLdLibraryPath() {
        return ldLibraryPath;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("path", path).add("ldLibraryPath", ldLibraryPath)
                .toString();
    }

    /**
     * Concrete builder containing MagnumEnvironment as model
     */
    public static class EnvironmentConcreteBuilder implements EnvironmentBuilder {
        MagnumEnvironment model;

        public EnvironmentConcreteBuilder() {
            this(new MagnumEnvironment());
        }

        public EnvironmentConcreteBuilder(MagnumEnvironment model) {
            this.model = model;
        }

        @Override
        public Environment build() {
            return model;
        }

        @Override
        public EnvironmentBuilder from(Environment in) {
            if (in != null)
                this.model = (MagnumEnvironment) in;
            return this;
        }

        @Override
        public EnvironmentBuilder path(String path) {
            model.path = path;
            return this;
        }

        @Override
        public EnvironmentBuilder ldLibraryPath(String ldLibraryPath) {
            model.ldLibraryPath = ldLibraryPath;
            return this;
        }
    }

}
