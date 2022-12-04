package org.openstack4j.model.storage.block.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.storage.block.VolumeTypeEncryption;

public interface VolumeTypeEncryptionBuilder extends Builder<VolumeTypeEncryptionBuilder, VolumeTypeEncryption> {

    /**
     * See {@link VolumeTypeEncryption#getCipher()} <b>Optional</b>
     *
     * @param cipher Defining cipher for the encryption type
     * @return VolumeTypeEncryptionBuilder
     */
    VolumeTypeEncryptionBuilder cipher(String cipher);

    /**
     * See {@link VolumeTypeEncryption#getControlLocation()} <b>Optional</b>
     *
     * @param controlLocation Defining control location for the encryption type
     * @return VolumeTypeEncryptionBuilder
     */
    VolumeTypeEncryptionBuilder controlLocation(String controlLocation);

    /**
     * See {@link VolumeTypeEncryption#getId()} <b>Optional</b>
     *
     * @param id The identifier for the encryption type.
     * @return VolumeTypeEncryptionBuilder
     */
    VolumeTypeEncryptionBuilder id(String id);

    /**
     * See {@link VolumeTypeEncryption#getKeySize()} <b>Optional</b>
     *
     * @param keySize Defining key size for the encryption type
     * @return VolumeTypeEncryptionBuilder
     */
    VolumeTypeEncryptionBuilder keySize(Integer keySize);

    /**
     * See {@link VolumeTypeEncryption#getProvider()}
     *
     * @param provider Defining provider for the encryption type
     * @return VolumeTypeEncryptionBuilder
     */
    VolumeTypeEncryptionBuilder provider(String provider);

    /**
     * See {@link VolumeTypeEncryption#getVolumeTypeId()}
     *
     * @param volumeTypeId Defining the identifier for the volume type for which encryption type is to be created
     * @return VolumeTypeEncryptionBuilder
     */
    VolumeTypeEncryptionBuilder volumeTypeId(String volumeTypeId);

}
