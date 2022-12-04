package org.openstack4j.api.exceptions;

/**
 * Base OpenStackj Exception
 *
 * @author Jeremy Unruh
 */
public class OS4JException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OS4JException(String message) {
        super(message);
    }

    public OS4JException(String message, Throwable cause) {
        super(message, cause);
    }

    public OS4JException(Throwable cause) {
        super(cause);
    }
}
