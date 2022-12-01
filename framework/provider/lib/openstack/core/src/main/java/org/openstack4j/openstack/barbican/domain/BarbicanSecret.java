package org.openstack4j.openstack.barbican.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.barbican.Secret;
import org.openstack4j.model.barbican.builder.SecretCreateBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by reneschollmeyer on 02.08.17.
 */
public class BarbicanSecret implements Secret {

    private String name;
    @JsonProperty("algorithm")
    private String algorithm;
    @JsonProperty("bit_length")
    private Integer bitLength;
    @JsonProperty("content_types")
    private Map<String, String> contentTypes;
    @JsonProperty("created")
    private Date createTime;
    @JsonProperty("updated")
    private Date updateTime;
    @JsonProperty("creator_id")
    private String creatorId;
    @JsonProperty("expiration")
    private Date expiration;
    @JsonProperty("mode")
    private String mode;
    @JsonProperty("secret_ref")
    private String secretReference;
    @JsonProperty("secret_type")
    private String secretType;
    @JsonProperty("status")
    private String status;
    @JsonProperty("payload")
    private String payload;
    @JsonProperty("payload_content_type")
    private String payloadContentType;
    @JsonProperty("payload_content_encoding")
    private String payloadContentEncoding;

    public static SecretCreateBuilder builder() {
        return new SecretCreateConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getBitLength() {
        return bitLength;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getContentTypes() {
        return contentTypes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCreatorId() {
        return creatorId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getExpiration() {
        return expiration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMode() {
        return mode;
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
    public String getSecretReference() {
        return secretReference;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSecretType() {
        return secretType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPayload() {
        return payload;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPayloadContentType() {
        return payloadContentType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPayloadContentEncoding() {
        return payloadContentEncoding;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("algorithm", algorithm).add("bit_length", bitLength)
                .add("content_types", contentTypes).add("created", createTime)
                .add("creator_id", creatorId).add("expiration", expiration)
                .add("mode", mode).add("name", name)
                .add("secret_ref", secretReference).add("secret_type", secretType)
                .add("status", status).add("updated", updateTime).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SecretCreateBuilder toBuilder() {
        return new SecretCreateConcreteBuilder();
    }

    public static class Secrets extends ListResult<BarbicanSecret> {

        @JsonProperty("total")
        private int count;

        @JsonProperty("secrets")
        private List<BarbicanSecret> list;

        protected List<BarbicanSecret> value() {
            return list;
        }

        public int getCount() {
            return count;
        }

    }

    /**
     * {@inheritDoc}
     */
    public static class SecretCreateConcreteBuilder implements SecretCreateBuilder {

        private BarbicanSecret internalSecret;

        public SecretCreateConcreteBuilder() {
            this(new BarbicanSecret());
        }

        public SecretCreateConcreteBuilder(BarbicanSecret secret) {
            this.internalSecret = secret;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SecretCreateBuilder name(String name) {
            internalSecret.name = name;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SecretCreateBuilder expiration(Date expiration) {
            internalSecret.expiration = expiration;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SecretCreateBuilder algorithm(String algorithm) {
            internalSecret.algorithm = algorithm;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SecretCreateBuilder bitLength(Integer bitLength) {
            internalSecret.bitLength = bitLength;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SecretCreateBuilder mode(String mode) {
            internalSecret.mode = mode;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SecretCreateBuilder payload(String payload) {
            internalSecret.payload = payload;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SecretCreateBuilder payloadContentType(String payloadContentType) {
            internalSecret.payloadContentType = payloadContentType;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SecretCreateBuilder payloadContentEncoding(String payloadContentEncoding) {
            internalSecret.payloadContentEncoding = payloadContentEncoding;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SecretCreateBuilder secretType(String secretType) {
            internalSecret.secretType = secretType;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Secret build() {
            return internalSecret;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SecretCreateBuilder from(Secret in) {
            internalSecret = (BarbicanSecret) in;
            return this;
        }
    }
}
