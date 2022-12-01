package org.openstack4j.connectors.httpclient;

import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.openstack4j.api.exceptions.ConnectionException;
import org.openstack4j.core.transport.HttpRequest;
import org.openstack4j.core.transport.ObjectMapperSingleton;
import org.openstack4j.core.transport.functions.EndpointURIFromRequestFunction;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * HttpCommand is responsible for executing the actual request driven by the
 * HttpExecutor.
 */
public final class HttpCommand<R> {

    HttpUriRequest clientReq;
    private HttpRequest<R> request;
    private CloseableHttpClient client;
    private int retries;

    private HttpCommand(HttpRequest<R> request) {
        this.request = request;
    }

    /**
     * Creates a new HttpCommand from the given request
     *
     * @param request the request
     * @return the command
     */
    public static <R> HttpCommand<R> create(HttpRequest<R> request) {
        HttpCommand<R> command = new HttpCommand<R>(request);
        command.initialize();
        return command;
    }

    private void initialize() {
        URI url = null;
        try {
            url = populateQueryParams(request);
        } catch (URISyntaxException e) {
            throw new ConnectionException(e.getMessage(), e.getIndex(), e);
        }
        client = HttpClientFactory.INSTANCE.getClient(request.getConfig());

        switch (request.getMethod()) {
            case POST:
                clientReq = new HttpPost(url);
                break;
            case PUT:
                clientReq = new HttpPut(url);
                break;
            case DELETE:
                clientReq = new HttpDelete(url);
                break;
            case HEAD:
                clientReq = new HttpHead(url);
                break;
            case PATCH:
                clientReq = new HttpPatch(url);
                break;
            case GET:
                clientReq = new HttpGet(url);
                break;
            default:
                throw new IllegalArgumentException("Unsupported http method: " + request.getMethod());
        }
        clientReq.setHeader("Accept", "application/json");
        populateHeaders(request);
    }

    /**
     * Executes the command and returns the Response
     *
     * @return the response
     */
    public CloseableHttpResponse execute() throws Exception {

        EntityBuilder builder = null;

        if (request.getEntity() != null) {
            if (InputStream.class.isAssignableFrom(request.getEntity().getClass())) {
                InputStreamEntity ise = new InputStreamEntity((InputStream) request.getEntity(),
                        ContentType.create(request.getContentType()));
                ((HttpEntityEnclosingRequestBase) clientReq).setEntity(ise);
            } else {
                builder = EntityBuilder.create().setContentType(ContentType.create(request.getContentType(), "UTF-8"))
                        .setText(ObjectMapperSingleton.getContext(request.getEntity().getClass()).writer()
                                .writeValueAsString(request.getEntity()));
            }
        } else if (request.hasJson()) {
            builder = EntityBuilder.create().setContentType(ContentType.APPLICATION_JSON).setText(request.getJson());
        }
        if (builder != null && clientReq instanceof HttpEntityEnclosingRequestBase)
            ((HttpEntityEnclosingRequestBase) clientReq).setEntity(builder.build());

        return client.execute(clientReq);
    }

    /**
     * @return true if a request entity has been set
     */
    public boolean hasEntity() {
        return request.getEntity() != null;
    }

    /**
     * @return current retry execution count for this command
     */
    public int getRetries() {
        return retries;
    }

    /**
     * @return incremement's the retry count and returns self
     */
    public HttpCommand<R> incrementRetriesAndReturn() {
        initialize();
        retries++;
        return this;
    }

    public HttpRequest<R> getRequest() {
        return request;
    }

    private URI populateQueryParams(HttpRequest<R> request) throws URISyntaxException {

        URIBuilder uri = new URIBuilder(new EndpointURIFromRequestFunction().apply(request));

        if (!request.hasQueryParams())
            return uri.build();

        for (Map.Entry<String, List<Object>> entry : request.getQueryParams().entrySet()) {
            for (Object o : entry.getValue()) {
                uri.addParameter(entry.getKey(), String.valueOf(o));
            }
        }
        return uri.build();
    }

    private void populateHeaders(HttpRequest<R> request) {

        if (!request.hasHeaders())
            return;

        for (Map.Entry<String, Object> h : request.getHeaders().entrySet()) {
            clientReq.addHeader(h.getKey(), String.valueOf(h.getValue()));
        }
    }
}
