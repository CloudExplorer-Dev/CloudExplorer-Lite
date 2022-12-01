package org.openstack4j.openstack.common;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * The date formats returned from the OpenStack telemetry APIs can be returned in multiple formats like:
 * "yyyy-MM-dd'T'HH:mm:ss" or "yyyy-MM-dd'T'HH:mm:ss.SSSSSS". This cannot be parsed correctly with a SimpleDateFormat,
 * so the formats must be normalized. This date deserializer normalizes the date into "yyyy-MM-dd'T'HH:mm:ss.SSS" and
 * then parses it into a Date.
 *
 * @author pdube
 */
public class TelemetryDateDeserializer extends StdDeserializer<Date> {
    private static final String MILLIS_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public TelemetryDateDeserializer() {
        this(null);
    }

    public TelemetryDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        String date = jsonParser.getText();
        SimpleDateFormat sdf = new SimpleDateFormat(MILLIS_DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(getParseableDate(date));
        } catch (ParseException e) {
            throw new JsonParseException(jsonParser, "Could not process telemetry date", e);
        }
    }

    /**
     * Modifies the date string to have the expected date format ("yyyy-MM-dd'T'HH:mm:ss.SSS")
     *
     * @return the date with the correct format
     */
    private String getParseableDate(String date) {
        // e.g. [ "2017-06-20 13:00:00" , "395000" ] or [] if no decimal
        String[] sdate = date.split("\\.");
        if (sdate.length > 1) {
            return sdate[0] + "." + getExpectedDecimals(sdate[1]);
        }
        return date + ".000";
    }

    private String getExpectedDecimals(String decimals) {
        return (decimals + "000").substring(0, 3);
    }

}
