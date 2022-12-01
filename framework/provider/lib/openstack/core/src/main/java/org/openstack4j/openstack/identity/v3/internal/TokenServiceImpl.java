package org.openstack4j.openstack.identity.v3.internal;

import org.openstack4j.api.identity.v3.TokenService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.identity.v3.Service;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.identity.v3.domain.KeystoneDomain.Domains;
import org.openstack4j.openstack.identity.v3.domain.KeystoneProject.Projects;
import org.openstack4j.openstack.identity.v3.domain.KeystoneService.Catalog;
import org.openstack4j.openstack.identity.v3.domain.KeystoneToken;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.openstack4j.core.transport.ClientConstants.*;

public class TokenServiceImpl extends BaseIdentityServices implements TokenService {

    @Override
    public Token get(String tokenId) {
        checkNotNull(tokenId);
        return get(KeystoneToken.class, PATH_TOKENS).header(HEADER_X_SUBJECT_TOKEN, tokenId).execute();
    }

    @Override
    public ActionResponse check(String tokenId) {
        checkNotNull(tokenId);
        return head(ActionResponse.class, PATH_TOKENS).header(HEADER_X_SUBJECT_TOKEN, tokenId).execute();
    }

    @Override
    public ActionResponse delete(String tokenId) {
        checkNotNull(tokenId);
        return deleteWithResponse(PATH_TOKENS).header(HEADER_X_SUBJECT_TOKEN, tokenId).execute();
    }

    @Override
    public List<? extends Service> getServiceCatalog(String tokenId) {
        checkNotNull(tokenId);
        return get(Catalog.class, uri(PATH_SERVICE_CATALOGS)).header(HEADER_X_SUBJECT_TOKEN, tokenId).execute().getList();
    }

    @Override
    public List<? extends Project> getProjectScopes(String tokenId) {
        checkNotNull(tokenId);
        return get(Projects.class, uri(PATH_PROJECT_SCOPES)).header(HEADER_X_SUBJECT_TOKEN, tokenId).execute().getList();
    }

    @Override
    public List<? extends Domain> getDomainScopes(String tokenId) {
        checkNotNull(tokenId);
        return get(Domains.class, uri(PATH_DOMAIN_SCOPES)).header(HEADER_X_SUBJECT_TOKEN, tokenId).execute().getList();
    }

}
