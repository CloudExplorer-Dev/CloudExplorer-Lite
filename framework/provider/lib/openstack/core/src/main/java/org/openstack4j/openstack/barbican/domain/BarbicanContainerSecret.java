package org.openstack4j.openstack.barbican.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.barbican.ContainerSecret;
import org.openstack4j.model.barbican.builder.ContainerSecretBuilder;

public class BarbicanContainerSecret implements ContainerSecret {
    private String name;
    @JsonProperty("secret_ref")
    private String reference;

    public static ContainerSecretBuilder builder() {
        return new SecretConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReference() {
        return reference;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name).add("reference", reference)
                .toString();
    }

    @Override
    public ContainerSecretBuilder toBuilder() {
        return new SecretConcreteBuilder();
    }

    public static class SecretConcreteBuilder implements ContainerSecretBuilder {
        private BarbicanContainerSecret internalSecret;

        public SecretConcreteBuilder() {
            this(new BarbicanContainerSecret());
        }

        public SecretConcreteBuilder(BarbicanContainerSecret secret) {
            this.internalSecret = secret;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ContainerSecret build() {
            return internalSecret;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ContainerSecretBuilder from(ContainerSecret in) {
            internalSecret = (BarbicanContainerSecret) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ContainerSecretBuilder name(String name) {
            internalSecret.name = name;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ContainerSecretBuilder reference(String ref) {
            internalSecret.reference = ref;
            return this;
        }
    }

}
