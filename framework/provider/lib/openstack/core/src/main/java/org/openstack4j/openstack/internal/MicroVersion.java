package org.openstack4j.openstack.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Common representation of an API micro version.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class MicroVersion implements Comparable<MicroVersion> {
    private final static Pattern VERSION_REGEX = Pattern.compile("^([1-9]\\d*)\\.([1-9]\\d*|0|)$");

    private final int majorVersion;
    private final int minorVersion;

    public MicroVersion(String version) {
        Matcher versionMatcher = VERSION_REGEX.matcher(version);

        if (!versionMatcher.matches()) {
            throw new IllegalArgumentException(String.format(
                    "Invalid version pattern %s, should be 'X.Y' (Major.Minor)", version));
        }

        majorVersion = Integer.valueOf(versionMatcher.group(1));
        minorVersion = Integer.valueOf(versionMatcher.group(2));
    }

    public MicroVersion(int majorVersion, int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    @Override
    public int compareTo(MicroVersion o) {
        int majorDifference = majorVersion - o.majorVersion;
        if (majorDifference != 0) {
            return majorDifference;
        }

        return minorVersion - o.minorVersion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MicroVersion) {
            MicroVersion v = (MicroVersion) obj;
            return compareTo(v) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%d.%d", majorVersion, minorVersion);
    }
}
