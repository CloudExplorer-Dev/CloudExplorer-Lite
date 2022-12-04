package org.openstack4j.model.common.header;

import org.openstack4j.openstack.internal.Parser;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A delegate class which contains calls to create various conditional header options
 *
 * @author Jeremy Unruh
 */
public class IfCondition implements HeaderOption {

    private String name;
    private Object value;

    protected IfCondition(String name) {
        this.name = name;
    }

    protected IfCondition(String name, Object value) {
        this(name);
        this.value = value;
    }

    /**
     * Determines if the remote data has been modified since the specified date
     *
     * @param milliseconds the date in milliseconds
     * @return if modified since condition
     */
    public static IfModifiedSince modifiedSince(long milliseconds) {
        return new IfModifiedSince(new Date(milliseconds));
    }

    /**
     * Determines if the remote data has been un-modified since the specified date
     *
     * @param milliseconds the date in milliseconds
     * @return if un-modified since condition
     */
    public static IfUnModifiedSince unModifiedSince(long milliseconds) {
        return new IfUnModifiedSince(new Date(milliseconds));
    }

    /**
     * Determines if the remote data has been un-modified since the specified date
     *
     * @param date the date since un-modified
     * @return if un-modified since condition
     */
    public static IfUnModifiedSince unModifiedSince(Date date) {
        return new IfUnModifiedSince(date);
    }

    /**
     * Determines if the remote data has been modified since the specified date
     *
     * @param date the date condition
     * @return if modified since condition
     */
    public static IfModifiedSince modifiedSince(Date date) {
        checkNotNull(date, "Date must not be null");
        return new IfModifiedSince(date);
    }

    /**
     * Compares the ETag as a condition to determine if the remote data does not match the checksum
     *
     * @param matchCondition the match condition (ETag)
     * @return the if none match condition
     * @see http://www.ietf.org/rfc/rfc2616.txt
     */
    public static IfNoneMatch noneMatch(String matchCondition) {
        checkNotNull(matchCondition, "Match condition must not be null");
        return new IfNoneMatch(matchCondition);
    }

    /**
     * Compares the ETag as a condition to determine if the remote data matches the checksum
     *
     * @param matchValue the match value (ETag)
     * @return the if match condition
     * @see http://www.ietf.org/rfc/rfc2616.txt
     */
    public static IfMatch match(String matchValue) {
        checkNotNull(matchValue, "Match value must not be null");
        return new IfMatch(matchValue);
    }

    @Override
    public HeaderNameValue toHeader() {
        return new HeaderNameValue(name, value);
    }

    public static class IfModifiedSince extends IfCondition {

        private static final String IF_MODIFIED_SINCE = "If-Modified-Since";

        IfModifiedSince(Date date) {
            super(IF_MODIFIED_SINCE, Parser.toRFC1123(date));
        }
    }

    public static class IfUnModifiedSince extends IfCondition {

        private static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

        IfUnModifiedSince(Date date) {
            super(IF_UNMODIFIED_SINCE, Parser.toRFC1123(date));
        }
    }

    public static class IfNoneMatch extends IfCondition {

        private static final String IF_NONE_MATCH = "If-None-Match";

        IfNoneMatch(String condition) {
            super(IF_NONE_MATCH, condition);
        }
    }

    public static class IfMatch extends IfCondition {

        private static final String IF_MATCH = "If-Match";

        IfMatch(String matchValue) {
            super(IF_MATCH, matchValue);
        }
    }

}
