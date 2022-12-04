package org.openstack4j.core.transport;

import org.openstack4j.api.exceptions.ResponseException;

/**
 * Exception Handles for common Http messages and status codes
 *
 * @author Jeremy Unruh
 */
@Deprecated
public class HttpExceptionHandler {

    /**
     * @deprecated Use {@link ResponseException#mapException(String, int, Throwable)}
     */
    @Deprecated
    public static ResponseException mapException(String message, int status) {
        return ResponseException.mapException(message, status, null);
    }

    /**
     * @deprecated Use {@link ResponseException#mapException(String, int, Throwable)}
     */
    @Deprecated
    public static ResponseException mapException(String message, int status, Throwable cause) {
        return ResponseException.mapException(message, status, cause);
    }
}
