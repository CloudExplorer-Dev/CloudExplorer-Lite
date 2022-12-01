package org.openstack4j.core.transport.functions;

import com.google.common.base.Function;
import org.openstack4j.core.transport.HttpRequest;

import static org.openstack4j.core.transport.ClientConstants.URI_SEP;

/**
 * Builds a URI comprising of Endpoint and Path from a HttpRequest object
 *
 * @author Jeremy Unruh
 */
public class EndpointURIFromRequestFunction implements Function<HttpRequest<?>, String> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(HttpRequest<?> request) {
        if (request.getEndpoint().endsWith(URI_SEP) || request.getPath().startsWith(URI_SEP))
            return escape(request.getEndpoint() + request.getPath());

        return escape(request.getEndpoint() + URI_SEP + request.getPath());
    }

    private String escape(String uri) {
        return uri.replaceAll(" ", "%20");
    }

}
