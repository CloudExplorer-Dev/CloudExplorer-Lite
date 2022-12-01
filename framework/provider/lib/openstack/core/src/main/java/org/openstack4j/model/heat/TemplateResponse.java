package org.openstack4j.model.heat;

import com.google.common.base.MoreObjects;

/**
 * Response returned during Template validation
 *
 * @author Jeremy Unruh
 */
public final class TemplateResponse {

    private String message;

    private TemplateResponse() {
    }

    private TemplateResponse(String message) {
        this.message = message;
    }

    public static TemplateResponse success() {
        return new TemplateResponse();
    }

    public static TemplateResponse fail(String message) {
        return new TemplateResponse(message);
    }

    /**
     * True if the template validation was successful
     *
     * @return true if successful
     */
    public boolean isValid() {
        return message == null;
    }

    /**
     * The Error message that occurred during validation.
     *
     * @return the error message, will be null if {@link #isValid()} is true
     */
    public String getMessage() {
        return message;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("valid", message == null).add("message", message).toString();
    }
}
