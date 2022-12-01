package org.openstack4j.openstack.identity.v3.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Represents a Token class used for V3 token authentication
 *
 * @author Jeremy Unruh
 */
@JsonRootName("auth")
public class TokenAuth extends Auth {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private Token token;

    public TokenAuth(String tokenId, String projectName, String projectId) {
        super(Type.TOKEN);
        this.token = new Token(tokenId);
        setProjectName(projectName);
        setProjectId(projectId);
    }

    public TokenAuth(String tokenId, String projectName, String projectId, String domainName, String domainId) {
        super(Type.TOKEN);
        this.token = new Token(tokenId);
        setProjectName(projectName);
        setProjectId(projectId);
        setDomainName(domainName);
        setDomainId(domainId);
    }

    protected static class Token {

        @JsonProperty
        protected String id;

        protected Token(String id) {
            this.id = id;
        }

    }

}
