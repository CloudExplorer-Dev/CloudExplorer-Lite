package org.openstack4j.core.transport;

/**
 * A Service which is responsible of execution an HttpRequest
 *
 * @author Jeremy Unruh
 */
public interface HttpExecutorService {

    /**
     * Executes the given request and returns the {@code HttpResponse} result from the server
     *
     * @param <R>     the underlying return entity type
     * @param request the request to execute
     * @return HttpResponse from the server
     */
    <R> HttpResponse execute(HttpRequest<R> request);

    /**
     * @return the executors display friendly name.  useful for logging or tests
     */
    String getExecutorDisplayName();
}
