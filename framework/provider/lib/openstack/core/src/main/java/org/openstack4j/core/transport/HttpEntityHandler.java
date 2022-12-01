package org.openstack4j.core.transport;

import org.openstack4j.api.exceptions.ResponseException;
import org.openstack4j.core.transport.functions.ResponseToActionResponse;
import org.openstack4j.model.common.ActionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Handles retrieving an Entity from an HttpResponse while validating resulting
 * status codes.
 *
 * @author Jeremy Unruh
 */
public class HttpEntityHandler {

    private static final Logger LOG = LoggerFactory.getLogger(HttpEntityHandler.class);

    public static <T> T handle(HttpResponse response, Class<T> returnType, ExecutionOptions<T> options) {
        return handle(response, returnType, options, Boolean.FALSE);
    }

    @SuppressWarnings("unchecked")
    public static <T> T handle(HttpResponse response, Class<T> returnType, ExecutionOptions<T> options,
                               boolean requiresVoidBodyHandling) {
        try {
            Handle<T> handle = Handle.create(response, returnType, options, requiresVoidBodyHandling);

            if (response.getStatus() >= 400) {

                if (requiresVoidBodyHandling && ActionResponse.class == returnType) {
                    return (T) ResponseToActionResponse.INSTANCE.apply(response);
                }

                if (options != null) {
                    options.propagate(response);
                }

                if (handle404(handle).isComplete()) {
                    return handle.getReturnObject();
                }

                if (handleLessThan500(handle).isComplete()) {
                    return handle.getReturnObject();
                }

                throw ResponseException.mapException(response);
            }

            if (options != null && options.hasParser()) {
                return options.getParser().apply(response);
            }

            if (returnType == Void.class) {
                return null;
            }

            if (returnType == ActionResponse.class) {
                return (T) ActionResponse.actionSuccess(response.getStatus());
            }

            return response.readEntity(returnType);
        } finally {
            closeQuietly(response);
        }
    }

    private static <T> Handle<T> handle404(Handle<T> handle) {
        if (handle.getResponse().getStatus() == 404) {

            if (ListType.class.isAssignableFrom(handle.getReturnType())) {
                try {
                    return handle.complete(handle.getReturnType().newInstance());
                } catch (InstantiationException e) {
                    LOG.error(e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    LOG.error(e.getMessage(), e);
                }
            }

            if (handle.getReturnType() != ActionResponse.class) {
                return handle.complete(null);
            }
        }

        return handle.continueHandling();
    }

    @SuppressWarnings("unchecked")
    private static <T> Handle<T> handleLessThan500(Handle<T> handle) {
        if (handle.getResponse().getStatus() < 500) {
            try {
                ActionResponse ar = ResponseToActionResponse.INSTANCE.apply(handle.getResponse());
                if (handle.getReturnType() == ActionResponse.class) {
                    return handle.complete((T) ar);
                }
                throw ResponseException.mapException(handle.getResponse(), ar.getFault());
            } catch (ResponseException re) {
                throw re;
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return handle.continueHandling();
    }

    /**
     * Closes an HttpResponse ignoring a possible IOException
     *
     * @param response the http response
     */
    public static void closeQuietly(HttpResponse response) {
        try {
            response.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Returns the status code and closes the response
     *
     * @param response the http response
     * @return the status code
     */
    public static int statusAndClose(HttpResponse response) {
        int status = response.getStatus();
        closeQuietly(response);
        return status;
    }
}
