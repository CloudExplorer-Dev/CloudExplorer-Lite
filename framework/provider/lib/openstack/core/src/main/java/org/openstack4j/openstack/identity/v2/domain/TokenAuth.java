package org.openstack4j.openstack.identity.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Represents a Token class used for V2 token authentication
 *
 * @author Jeremy Unruh
 */
@JsonRootName("auth")
public class TokenAuth extends Auth {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private Token token;

    public TokenAuth(String tokenId, String tenantName, String tenantId) {
        super(Type.TOKEN);
        this.token = new Token(tokenId);
        setTenantName(tenantName);
        setTenantId(tenantId);
    }

    protected static class Token {

        @JsonProperty
        protected String id;

        protected Token(String id) {
            this.id = id;
        }

    }


}
