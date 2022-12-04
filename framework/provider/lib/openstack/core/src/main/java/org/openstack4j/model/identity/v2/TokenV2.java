package org.openstack4j.model.identity.v2;

/**
 * A version 2 token which is used during authentication allowing follow up calls to only supply the assigned token within the header avoiding re-authentication
 *
 * @author Jeremy Unruh
 * @see http://docs.openstack.org/api/openstack-identity-service/2.0/content/POST_admin-authenticate_v2.0_tokens_Token_Operations.html
 */
public interface TokenV2 extends Token {

    /**
     * @return the tenant associated with the original authentication request
     */
    Tenant getTenant();

}
