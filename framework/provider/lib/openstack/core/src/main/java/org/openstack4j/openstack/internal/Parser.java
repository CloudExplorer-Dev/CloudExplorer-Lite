package org.openstack4j.openstack.internal;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openstack4j.core.transport.ClientConstants.CONTENT_TYPE_TEXT;
import static org.openstack4j.core.transport.ClientConstants.CONTENT_TYPE_TEXT_HTML;

/**
 * Provides common parser routines when dealing with Headers or other non-json payloads
 *
 * @author Jeremy Unruh
 * @author Qin An - Added parser function for Neutron Agent List
 */
public final class Parser {

    private static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final SimpleDateFormat RFC822_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
    private static final SimpleDateFormat ISO8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
    private static final String TZ_REGEX = "([+-][0-9][0-9](:?[0-9][0-9])?|Z)";
    private static final Pattern TZ_PATTERN = Pattern.compile("(.*)" + TZ_REGEX + "$");
    private static final Pattern SECOND_PATTERN = Pattern.compile(".*[0-2][0-9]:00");
    private static SimpleDateFormat DF;

    static {
        ISO8601_FORMAT.setTimeZone(new SimpleTimeZone(0, "GMT"));
    }

    /**
     * Takes a String Numeric and returns null or the Long value
     *
     * @param number the number in string form
     * @return the Long or null
     */
    public static Long asLong(String number) {
        return asLong(number, null);
    }

    /**
     * Takes a String Numeric and returns null or the Long value
     *
     * @param number       the number in string form
     * @param defaultValue the default value if number is null
     * @return the Long or null
     */
    public static Long asLong(String number, Long defaultValue) {
        if (number == null) return defaultValue;
        return Long.parseLong(number);
    }

    /**
     * Takes a String Boolean and returns null or as Boolean value
     *
     * @param bool the boolean in String form
     * @return the Boolean or null
     */
    public static Boolean asBoolean(String bool) {
        if (bool == null) return null;
        return Boolean.parseBoolean(bool);
    }

    /**
     * Takes a String Date and decodes it into a date.  Returns null if the date is null
     *
     * @param date the date in String form
     * @return Date or null
     */
    public static Date asDate(String date) {
        try {
            if (date != null)
                return StdDateFormat.instance.parse(date);
        } catch (ParseException e) {
            LoggerFactory.getLogger(Parser.class).error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Parse a string with format "yyyy-MM-dd HH:mm:ss" into a Date
     * The string format is used in Neutron Agent-List
     *
     * @param date - string to be parsed
     * @return Date
     */
    public static Date parseSimpleDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (date != null) {
                return format.parse(date);
            }
        } catch (ParseException e) {
            LoggerFactory.getLogger(Parser.class).error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Takes a Date and returns it's equivalent in RFC 1123
     *
     * @param date the date to format
     * @return the formatted date string
     */
    public static String toRFC1123(Date date) {
        if (DF == null) {
            DF = new SimpleDateFormat(PATTERN_RFC1123, Locale.US);
            DF.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return DF.format(date);
    }

    /**
     * Parses a String in RFC 822 format into a Date object
     *
     * @param toParse the date to parse
     * @return the parsed date
     */
    public static Date toRFC822DateParse(String toParse) {
        if (toParse == null) {
            return null;
        }
        synchronized (RFC822_FORMAT) {
            try {
                return RFC822_FORMAT.parse(toParse);
            } catch (ParseException pe) {
                throw new IllegalArgumentException("Error parsing date at " + pe.getErrorOffset(), pe);
            }
        }
    }

    public static String toISO8601DateFormat(Date date) {
        synchronized (ISO8601_FORMAT) {
            String parsed = ISO8601_FORMAT.format(date);
            String tz = findTZ(parsed);
            if (tz.equals("+0000")) {
                parsed = trimTZ(parsed) + "Z";
            }
            return parsed;
        }
    }

    private static String findTZ(String toParse) {
        Matcher matcher = TZ_PATTERN.matcher(toParse);
        if (matcher.find()) {
            // Remove ':' from the TZ string, as SimpleDateFormat can't handle it
            String tz = matcher.group(2).replace(":", "");
            // Append '00; if we only have a two digit TZ, as SimpleDateFormat
            if (tz.length() == 2) tz += "00";
            // Replace Z with +0000
            if (tz.equals("Z")) return "+0000";
            return tz;
        } else {
            // Return +0000 if no time zone
            return "+0000";
        }
    }

    private static String trimTZ(String toParse) {
        Matcher matcher = TZ_PATTERN.matcher(toParse);
        if (matcher.find()) {
            toParse = matcher.group(1);
        }

        if (toParse.length() == 25 && SECOND_PATTERN.matcher(toParse).matches())
            toParse = toParse.substring(0, toParse.length() - 6);
        return toParse;
    }

    /**
     * Determines if the specified content type is text/plain or text/html.  If the contentType is null
     * then false is returned.
     *
     * @param contentType the content type
     * @return true if the contentType is text/plain or text/html
     */
    public static boolean isContentTypeText(String contentType) {
        if (contentType == null)
            return false;

        return (contentType.contains(CONTENT_TYPE_TEXT) || contentType.contains(CONTENT_TYPE_TEXT_HTML));
    }
}
