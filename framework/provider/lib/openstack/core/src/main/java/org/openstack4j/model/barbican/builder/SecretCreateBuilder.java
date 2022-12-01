package org.openstack4j.model.barbican.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.barbican.Secret;

import java.util.Date;

/**
 * Created by reneschollmeyer on 02.08.17.
 */
public interface SecretCreateBuilder extends Buildable.Builder<SecretCreateBuilder, Secret> {

    /**
     * @param name Human readable name for identifying your secret.
     */
    SecretCreateBuilder name(String name);

    /**
     * @param expiration UTC Timestamp. If set, the secret will not be available after this time.
     */
    SecretCreateBuilder expiration(Date expiration);

    /**
     * @param algorithm Metadata provided by a user or system for informational purposes.
     */
    SecretCreateBuilder algorithm(String algorithm);

    /**
     * @param bitLength Metadata provided by a user or system for informational purposes.
     *                  Value must be greater than zero.
     */
    SecretCreateBuilder bitLength(Integer bitLength);

    /**
     * @param mode Metadata provided by a user or system for informational purposes.
     */
    SecretCreateBuilder mode(String mode);

    /**
     * @param payload The secret’s data to be stored.
     */
    SecretCreateBuilder payload(String payload);

    /**
     * @param payloadContentType The media type for the content of the payload
     *                           (required if payload is included).
     */
    SecretCreateBuilder payloadContentType(String payloadContentType);

    /**
     * @param payloadContentEncoding The encoding used for the payload to be able to include
     *                               it in the JSON request (required if payload is encoded).
     */
    SecretCreateBuilder payloadContentEncoding(String payloadContentEncoding);

    /**
     * @param secretType Used to indicate the type of secret being stored.
     */
    SecretCreateBuilder secretType(String secretType);
}
