package org.openstack4j.openstack.identity.functions;

import com.google.common.base.Function;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Functions to help resolve specific Services and Versions
 *
 * @author Jeremy Unruh
 */
public final class ServiceFunctions {

    private static final Pattern VERSION_PATTERN = Pattern.compile("(.*)v(\\d+)");

    /**
     * Takes a Service Type or Service Name and strips off any version (if applicable) and returns the
     * common name.  For example: {@code nova21} would be returned as {@code nova}.
     */
    public static final Function<String, String> TYPE_WITHOUT_VERSION = new Function<String, String>() {
        @Override
        public String apply(String serviceType) {
            return matchForVersion(serviceType, false);
        }
    };

    /**
     * Determines the version from a Service Type or Name.  If the current service does not match the pattern
     * then a version 1 is always returned.  For example: {@code nova21} would return {@code 21}, {@code compute} would
     * return {@code 1}
     */
    public static final Function<String, Integer> VERSION_FROM_TYPE = new Function<String, Integer>() {

        @Override
        public Integer apply(String serviceType) {
            return matchForVersion(serviceType, true);
        }
    };

    @SuppressWarnings("unchecked")
    private static <T> T matchForVersion(String service, boolean returnVersion) {
        Matcher m = VERSION_PATTERN.matcher(service);
        if (m.matches()) {
            if (returnVersion) {
                return (T) Integer.valueOf(m.group(2));
            }
            return (T) m.group(1);
        }
        if (returnVersion) {
            return (T) Integer.valueOf(1);
        }
        return (T) service;
    }

}
