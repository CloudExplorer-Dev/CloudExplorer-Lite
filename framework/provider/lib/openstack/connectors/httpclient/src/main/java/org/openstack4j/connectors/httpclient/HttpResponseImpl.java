package org.openstack4j.connectors.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.openstack4j.api.exceptions.ClientResponseException;
import org.openstack4j.core.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class HttpResponseImpl implements HttpResponse {

    private static final Logger LOG = LoggerFactory.getLogger(HttpResponseImpl.class);
    private CloseableHttpResponse response;

    private HttpResponseImpl(CloseableHttpResponse response) {
        this.response = response;
    }

    /**
     * Wrap the given Response
     *
     * @param response the response
     * @return the HttpResponse
     */
    public static HttpResponseImpl wrap(CloseableHttpResponse response) {
        return new HttpResponseImpl(response);
    }

    /**
     * Unwrap and return the original Response
     *
     * @return the response
     */
    public CloseableHttpResponse unwrap() {
        return response;
    }

    /**
     * Gets the entity and Maps any errors which will result in a ResponseException
     *
     * @param <T>        the generic type
     * @param returnType the return type
     * @return the entity
     */
    public <T> T getEntity(Class<T> returnType) {
        return getEntity(returnType, null);
    }

    /**
     * Gets the entity and Maps any errors which will result in a ResponseException
     *
     * @param <T>        the generic type
     * @param returnType the return type
     * @param options    execution options
     * @return the entity
     */
    @Override
    public <T> T getEntity(Class<T> returnType, ExecutionOptions<T> options) {
        return HttpEntityHandler.handle(this, returnType, options, Boolean.TRUE);
    }

    /**
     * Gets the status from the previous Request
     *
     * @return the status code
     */
    public int getStatus() {
        return response.getStatusLine().getStatusCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatusMessage() {
        return response.getStatusLine().getReasonPhrase();
    }

    /**
     * @return the input stream
     */
    public InputStream getInputStream() {
        HttpEntity entity = response.getEntity();
        try {
            if (entity != null)
                return entity.getContent();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Returns a Header value from the specified name key
     *
     * @param name the name of the header to query for
     * @return the header as a String or null if not found
     */
    public String header(String name) {
        Header header = response.getFirstHeader(name);
        return (header != null) ? header.getValue() : null;
    }

    /**
     * @return the a Map of Header Name to Header Value
     */
    public Map<String, String> headers() {
        Map<String, String> retHeaders = new HashMap<String, String>();
        Header[] headers = response.getAllHeaders();

        for (Header h : headers) {
            retHeaders.put(h.getName(), h.getValue());
        }
        return retHeaders;
    }

    @Override
    public <T> T readEntity(Class<T> typeToReadAs) {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            // Normal case if the response has no content, e.g. for a HEAD request
            return null;
        }
        try {
            InputStream content = checkNotNull(entity.getContent(), "Entity content should not be null.");
            return ObjectMapperSingleton.getContext(typeToReadAs).readerFor(typeToReadAs).readValue(content);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ClientResponseException(e.getMessage(), 0, e);
        }
    }

    @Override
    public void close() throws IOException {
        if (response != null)
            response.close();
    }

    @Override
    public String getContentType() {
        return header(ClientConstants.HEADER_CONTENT_TYPE);
    }
}
