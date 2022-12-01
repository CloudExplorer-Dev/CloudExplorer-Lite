package org.openstack4j.model.identity.v3;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.identity.v3.builder.CredentialBuilder;

import java.util.Map;

/**
 * Domain model.
 *
 * @see <a href="http://developer.openstack.org/api-ref-identity-v3.html#domains-v3">API reference</a>
 */
public interface Credential extends ModelEntity, Buildable<CredentialBuilder> {

    /**
     * @return the id of the credential
     */
    String getId();

    /**
     * @return the id of the user who owns the credential
     */
    String getUserId();

    /**
     * @return the id of the associated project
     */
    String getProjectId();

    /**
     * @return the credential type such as 'ec2', 'cert'
     */
    String getType();

    /**
     * the credential itself as serialized blob
     *
     * @return the blob the credential
     */
    String getBlob();

    /**
     * @return the links for the credential resource
     */
    Map<String, String> getLinks();

}
