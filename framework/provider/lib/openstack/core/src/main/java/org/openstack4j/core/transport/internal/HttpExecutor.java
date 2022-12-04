package org.openstack4j.core.transport.internal;

import org.openstack4j.api.exceptions.ConnectorNotFoundException;
import org.openstack4j.api.exceptions.ResponseException;
import org.openstack4j.core.transport.HttpExecutorService;
import org.openstack4j.core.transport.HttpRequest;
import org.openstack4j.core.transport.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * HttpExecutor is a delegate to the underline connector associated to OpenStack4j.
 *
 * @author Jeremy Unruh
 */
public class HttpExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(HttpExecutor.class);
    private static final HttpExecutor INSTANCE = new HttpExecutor();
    private HttpExecutorService service;

    private HttpExecutor() {
    }

    public static HttpExecutor create() {
        return INSTANCE;
    }

    private HttpExecutorService service() {
        if (service != null) return service;

        Iterator<HttpExecutorService> it = ServiceLoader.load(HttpExecutorService.class, getClass().getClassLoader()).iterator();
        if (!it.hasNext()) {
            LOG.error("No OpenStack4j connector found in classpath");
            throw new ConnectorNotFoundException("No OpenStack4j connector found in classpath");
        }
        return service = it.next();
    }

    public String getExecutorName() {
        return service().getExecutorDisplayName();
    }

    /**
     * Delegate to {@link HttpExecutorService#execute(HttpRequest)}
     */
    public <R> HttpResponse execute(HttpRequest<R> request) {

        LOG.debug("Executing Request: {} {}", request.getMethod(), request.getUrl());
        try {
            return service().execute(request);
        } catch (ResponseException ex) {
            ex.setRequestInfo(request);
            throw ex;
        }
    }
}
