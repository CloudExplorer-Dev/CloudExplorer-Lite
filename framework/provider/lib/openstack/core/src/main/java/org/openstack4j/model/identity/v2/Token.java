package org.openstack4j.model.identity.v2;

import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.identity.AuthVersion;

import java.util.Date;

/**
 * A token which is used during authentication allowing follow up calls to only supply the assigned token within the header avoiding re-authentication
 *
 * @author Jeremy Unruh
 * @see http://docs.openstack.org/api/openstack-identity-service/2.0/content/POST_admin-authenticate_v2.0_tokens_Token_Operations.html
 */
public interface Token extends ModelEntity {

    /**
     * The generated token ID created by the Identity Restful service
     *
     * @return the token identifier
     */
    String getId();

    /**
     * The expiring date/time of this token
     *
     * @return the expire date/time
     */
    Date getExpires();

    /**
     * @return the authentication version of this token
     */
    AuthVersion getVersion();
}
