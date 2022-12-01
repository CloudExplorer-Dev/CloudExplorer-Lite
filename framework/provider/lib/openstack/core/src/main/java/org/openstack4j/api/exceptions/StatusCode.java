package org.openstack4j.api.exceptions;

/**
 * Maps the documented API status codes into a StatusCode Enumeration Type.
 *
 * @author Jeremy Unruh
 */
public enum StatusCode {
    // Client Status Codes
    /**
     * The request cannot be fulfilled due to bad syntax.
     */
    BAD_REQUEST(400),
    /**
     * Similar to 403 Forbidden, but specifically for use when authentication is required and has failed or has not yet been provided
     */
    UNAUTHORIZED(401),
    /**
     * Reserved for future use.
     */
    PAYMENT_REQUIRED(402),
    /**
     * The request was a valid request, but the server is refusing to respond to it.
     */
    FORBIDDEN(403),
    /**
     * The requested resource could not be found but may be available again in the future
     */
    NOT_FOUND(404),
    /**
     * A request was made of a resource using a request method not supported by that resource
     */
    METHOD_NOT_ALLOWED(405),
    /**
     * The requested resource is only capable of generating content not acceptable according to the Accept headers sent in the request.
     */
    NOT_ACCEPTABLE(406),
    /**
     * The client must first authenticate itself with the proxy.
     */
    PROXY_AUTH_REQUIRED(407),
    /**
     * The server timed out waiting for the request.
     */
    REQUEST_TIMEOUT(408),
    /**
     * Indicates that the request could not be processed because of conflict in the request, such as an edit conflict.
     */
    CONFLICT(409),
    /**
     * Indicates that the resource requested is no longer available and will not be available again
     */
    GONE(410),
    /**
     * The request did not specify the length of its content, which is required by the requested resource
     */
    LENGTH_REQUIRED(411),
    /**
     * The server does not meet one of the preconditions that the requester put on the request
     */
    PRE_CONDITION_FAILED(412),
    /**
     * The request is larger than the server is willing or able to process.
     */
    REQUEST_ENTITY_TOO_LARGE(413),
    /**
     * The URI provided was too long for the server to process.
     */
    REQUEST_URI_TOO_LONG(414),
    /**
     * The request entity has a media type which the server or resource does not support
     */
    UNSUPPORTED_MEDIATYPE(415),
    /**
     * The client has asked for a portion of the file, but the server cannot supply that portion
     */
    REQUESTED_RANGE_NOT_SATISFIABLE(416),
    /**
     * The server cannot meet the requirements of the Expect request-header field.
     */
    EXPECTATION_FAILED(417),
    /**
     * The request was well-formed but was unable to be followed due to semantic errors
     */
    UNPROCESSABLE_ENTITY(422),

    //Server Status Code
    /**
     * A generic error message, given when no more specific message is suitable.
     */
    INTERNAL_SERVER_ERROR(500),
    /**
     * The server either does not recognize the request method, or it lacks the ability to fulfill the request
     */
    NOT_IMPLEMENTED(501),
    /**
     * The server was acting as a gateway or proxy and received an invalid response from the upstream server
     */
    BAD_GATEWAY(502),
    /**
     * The server is currently unavailable
     */
    SERVICE_UNAVAILABLE(503),
    /**
     * The server was acting as a gateway or proxy and did not receive a timely response from the upstream server.
     */
    GATEWAY_TIMEOUT(504),
    /**
     * The server does not support the HTTP protocol version used in the request.
     */
    VERSION_NOT_SUPPORTED(505),
    /**
     * Indicates the code we received is not found within this Enum
     */
    CODE_UNKNOWN(0);
    private final int code;

    private StatusCode(int code) {
        this.code = code;
    }

    public static StatusCode fromCode(int code) {
        for (StatusCode sc : StatusCode.values()) {
            if (sc.getCode() == code)
                return sc;
        }
        return CODE_UNKNOWN;
    }

    public int getCode() {
        return code;
    }
}
