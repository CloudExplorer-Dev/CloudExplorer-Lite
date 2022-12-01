package org.openstack4j.openstack.storage.object.domain;

import org.openstack4j.core.transport.HttpRequest;

import java.util.Map;

/**
 * Simple Tuple object used to hold onto an in-bound header map and request object
 *
 * @author Jeremy Unruh
 */
public final class MetaHeaderRequestWrapper<R> {

    private final String prefix;
    private final HttpRequest<R> request;
    private final Map<String, String> metadata;

    private MetaHeaderRequestWrapper(String prefix, Map<String, String> metadata, HttpRequest<R> request) {
        this.prefix = prefix;
        this.metadata = metadata;
        this.request = request;
    }

    public static <R> MetaHeaderRequestWrapper<R> of(String prefix, Map<String, String> metadata, HttpRequest<R> request) {
        return new MetaHeaderRequestWrapper<R>(prefix, metadata, request);
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public HttpRequest<R> getRequest() {
        return request;
    }

    public String getPrefix() {
        return prefix;
    }

}
