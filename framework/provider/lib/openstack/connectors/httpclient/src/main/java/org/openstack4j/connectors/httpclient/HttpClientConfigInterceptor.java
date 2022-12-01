package org.openstack4j.connectors.httpclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openstack4j.core.transport.Config;

/**
 * Allows for interception during the creation of a new HttpClient.  To register a custom singleton interceptor you must invoke
 * {@link HttpClientFactory#registerInterceptor(HttpClientConfigInterceptor)}
 *
 * @author Jeremy Unruh
 */
public interface HttpClientConfigInterceptor {

    /**
     * This method is invoked prior to the HttpClientBuilder build is called allowing any overrides or custom configuration.
     *
     * @param client        the http client builder
     * @param requestConfig the request config builder
     * @param config        the openstack4j config
     */
    void onClientCreate(HttpClientBuilder client, RequestConfig.Builder requestConfig, Config config);
}
