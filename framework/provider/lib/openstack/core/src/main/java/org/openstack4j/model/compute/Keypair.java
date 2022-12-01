package org.openstack4j.model.compute;

import org.openstack4j.model.ModelEntity;

import java.util.Date;

/**
 * An OpenStack Keypair is an SSH Key
 *
 * @author Jeremy Unruh,whaon
 */
public interface Keypair extends ModelEntity {

    /**
     * The name associated with the keypair
     *
     * @return the name of the keypair
     */
    String getName();

    /**
     * The public SSH key
     *
     * @return the public key
     */
    String getPublicKey();

    /**
     * The private key associated with this keypair.  Only populated on create when a public key is not specified and is auto-generated
     * by the server
     *
     * @return the private key
     */
    String getPrivateKey();

    /**
     * @return the server fingerprint
     */
    String getFingerprint();

    /**
     * @return the user_id for a keypair.
     */
    String getUserId();

    /**
     * @return is deleted
     */
    Boolean getDeleted();

    /**
     * time created
     */
    Date getCreatedAt();

    /**
     * time updated
     */
    Date getUpdatedAt();

    /**
     * time deleted
     */
    Date getDeletedAt();

    /**
     * @return id of the keypair
     */
    Integer getId();

}
