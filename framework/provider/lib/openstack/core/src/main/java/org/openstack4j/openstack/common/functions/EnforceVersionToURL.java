package org.openstack4j.openstack.common.functions;

import com.google.common.base.Function;

/**
 * Removes a trailing version and appends the specified version to an endpoint URL
 *
 * @author Jeremy Unruh
 */
public class EnforceVersionToURL implements Function<String, String> {

    private static final String VALIDATE = "-VALID-";

    private final String version;
    private boolean onlyIfAbsent;

    private EnforceVersionToURL(String version) {
        this(version, false);
    }

    private EnforceVersionToURL(String version, boolean onlyIfAbsent) {
        this.version = version;
        this.onlyIfAbsent = onlyIfAbsent;
    }

    public static EnforceVersionToURL instance(String version) {
        return new EnforceVersionToURL(version);
    }

    public static EnforceVersionToURL instance(String version, boolean onlyIfAbsent) {
        return new EnforceVersionToURL(version, onlyIfAbsent);
    }


    @Override
    public String apply(String input) {
        if (onlyIfAbsent && input.replaceFirst(RemoveVersionFromURL.VERSION_REGEX, VALIDATE).contains(VALIDATE))
            return input;
        return RemoveVersionFromURL.INSTANCE.apply(input).concat(version);
    }
}
