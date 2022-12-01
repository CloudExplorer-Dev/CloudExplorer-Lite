package org.openstack4j.core.transport;

/**
 * Allows for propagation depending on the state of a response.  If applied to an HttpExecution the execution will
 * call the {@link #propagate(HttpResponse)} method.  The method will either throw a ClientResponseException variant
 * or do nothing letting the execution code handle like normal
 *
 * @author Jeremy Unruh
 */
public interface PropagateResponse {

    /**
     * Called to allow for optional exception propagation depending on the HttpResponse state
     *
     * @param response the http response
     * @throws ClientResponseException variant if execution deemed a failure
     */
    void propagate(HttpResponse response);

}
