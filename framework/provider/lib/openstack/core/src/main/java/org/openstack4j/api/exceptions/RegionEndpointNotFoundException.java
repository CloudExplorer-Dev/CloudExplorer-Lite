package org.openstack4j.api.exceptions;

/**
 * Exception that is thrown when a Service Endpoint cannot be found for the user specified Region
 *
 * @author Jeremy Unruh
 */
public class RegionEndpointNotFoundException extends OS4JException {

    private static final long serialVersionUID = 1L;
    private static final String EXCEPT_FORMAT = "Endpoint for '%s' service could not be found for region: '%s'";

    public RegionEndpointNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegionEndpointNotFoundException(String message) {
        super(message);
    }

    public static RegionEndpointNotFoundException create(String region, String serviceName) {
        return new RegionEndpointNotFoundException(String.format(EXCEPT_FORMAT, serviceName, region));
    }

}
