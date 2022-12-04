package org.openstack4j.model.identity;

import org.openstack4j.api.types.Facing;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.common.resolvers.LatestServiceVersionResolver;
import org.openstack4j.model.common.resolvers.ServiceVersionResolver;
import org.openstack4j.model.common.resolvers.StableServiceVersionResolver;
import org.openstack4j.model.identity.v2.Access;
import org.openstack4j.model.identity.v3.Token;

/**
 * Dynamic parameters used for URL resolution with Endpoints
 *
 * @author Jeremy Unruh
 */
public class URLResolverParams {

    public Token token;
    public ServiceType type;
    public String region;
    public Facing perspective;
    public Access access;
    private ServiceVersionResolver resolver;

    private URLResolverParams(Token token, ServiceType type) {
        this.token = token;
        this.type = (type == null) ? ServiceType.IDENTITY : type;
    }

    private URLResolverParams(Access access, ServiceType type) {
        this.access = access;
        this.type = (type == null) ? ServiceType.IDENTITY : type;
    }

    public static URLResolverParams create(Token token, ServiceType type) {
        return new URLResolverParams(token, type);
    }

    public static URLResolverParams create(Access access, ServiceType type) {
        return new URLResolverParams(access, type);
    }

    public URLResolverParams region(String region) {
        this.region = region;
        return this;
    }

    public URLResolverParams perspective(Facing perspective) {
        this.perspective = perspective;
        return this;
    }

    public URLResolverParams resolver(ServiceVersionResolver resolver) {
        this.resolver = resolver;
        return this;
    }

    public ServiceVersionResolver getResolver() {
        return (resolver != null) ? resolver : LatestServiceVersionResolver.INSTANCE;
    }

    public ServiceVersionResolver getV2Resolver() {
        return (resolver != null) ? resolver : StableServiceVersionResolver.INSTANCE;
    }

}
