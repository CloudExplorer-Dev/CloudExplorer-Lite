package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.storage.block.VolumeTypeEncryption;
import org.openstack4j.model.storage.block.builder.VolumeTypeEncryptionBuilder;

/**
 * A volume type encryption
 * <p>
 * This is the parent class for {@link CinderVolumeTypeEncryption} and
 * {@link CinderVolumeTypeEncryptionFetch} CinderVolumeTypeEncryption has the json root name encryption whereas
 * CinderVolumeTypeEncryptionFetch doesn't as the API doesn't expect it
 *
 * @author Jyothi Saroja
 */
public class AbstractCinderVolumeTypeEncryption implements VolumeTypeEncryption {

    private static final long serialVersionUID = 1L;

    @JsonProperty("encryption_id")
    protected String id;
    @JsonProperty("provider")
    protected String provider;
    @JsonProperty("control_location")
    protected String controlLocation;
    @JsonProperty("cipher")
    protected String cipher;
    @JsonProperty("key_size")
    protected Integer keySize;
    @JsonProperty("volume_type_id")
    protected String volumeTypeId;

    /**
     * @return the Encryption Type Builder
     */
    public static VolumeTypeEncryptionBuilder builder() {
        return new VolumeTypeEncryptionConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProvider() {
        return provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVolumeTypeId() {
        return volumeTypeId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCipher() {
        return cipher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getKeySize() {
        return keySize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getControlLocation() {
        return controlLocation;
    }

    @Override
    public VolumeTypeEncryptionBuilder toBuilder() {
        return new VolumeTypeEncryptionConcreteBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("volumeTypeId", volumeTypeId)
                .add("provider", provider).add("cipher", cipher).add("keySize", keySize)
                .add("controlLocation", controlLocation).toString();
    }

    public static class VolumeTypeEncryptionConcreteBuilder implements VolumeTypeEncryptionBuilder {

        private CinderVolumeTypeEncryption cinderVolumeTypeEncryption;

        VolumeTypeEncryptionConcreteBuilder() {
            this(new CinderVolumeTypeEncryption());
        }

        VolumeTypeEncryptionConcreteBuilder(VolumeTypeEncryption volumeTypeEncryption) {
            this.cinderVolumeTypeEncryption = (CinderVolumeTypeEncryption) volumeTypeEncryption;
        }

        @Override
        public VolumeTypeEncryption build() {
            return cinderVolumeTypeEncryption;
        }

        @Override
        public VolumeTypeEncryptionBuilder from(VolumeTypeEncryption in) {
            cinderVolumeTypeEncryption = (CinderVolumeTypeEncryption) in;
            return this;
        }

        @Override
        public VolumeTypeEncryptionBuilder provider(String provider) {
            cinderVolumeTypeEncryption.provider = provider;
            return this;
        }

        @Override
        public VolumeTypeEncryptionBuilder id(String id) {
            cinderVolumeTypeEncryption.id = id;
            return this;
        }

        @Override
        public VolumeTypeEncryptionBuilder volumeTypeId(String volTypeId) {
            cinderVolumeTypeEncryption.volumeTypeId = volTypeId;
            return this;
        }

        @Override
        public VolumeTypeEncryptionBuilder cipher(String cipher) {
            cinderVolumeTypeEncryption.cipher = cipher;
            return this;
        }

        @Override
        public VolumeTypeEncryptionBuilder keySize(Integer keySize) {
            cinderVolumeTypeEncryption.keySize = keySize;
            return this;
        }

        @Override
        public VolumeTypeEncryptionBuilder controlLocation(String controlLocation) {
            cinderVolumeTypeEncryption.controlLocation = controlLocation;
            return this;
        }
    }

}
