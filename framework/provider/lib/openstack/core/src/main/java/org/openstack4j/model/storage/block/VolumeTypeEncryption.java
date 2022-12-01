package org.openstack4j.model.storage.block;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.storage.block.builder.VolumeTypeEncryptionBuilder;

/**
 * The volume type encryption defines the characteristics of an encryption.
 *
 * @author Jyothi Saroja
 */
public interface VolumeTypeEncryption extends ModelEntity, Buildable<VolumeTypeEncryptionBuilder> {

    /**
     * @return the cipher of the encryption
     */
    String getCipher();

    /**
     * @return the control location of the encryption
     */
    String getControlLocation();

    /**
     * @return the identifier for the encryption
     */
    String getId();

    /**
     * @return the key size of the encryption in bits
     */
    Integer getKeySize();

    /**
     * @return the provider of the encryption
     */
    String getProvider();

    /**
     * @return the identifier for the associated volume type
     */
    String getVolumeTypeId();

}
