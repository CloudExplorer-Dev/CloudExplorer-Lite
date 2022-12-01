package org.openstack4j.openstack.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

/**
 * @author esommar on 5/10/2017.
 */
public class CustomEpochToDateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        String dateEpochSecondsAsString = jsonparser.getText();
        Double rawDateEpochSeconds = Double.parseDouble(dateEpochSecondsAsString);
        Double rawDateEpochMilliSeconds = rawDateEpochSeconds * 1000;
        Date date = new Date(rawDateEpochMilliSeconds.longValue());
        return date;
    }
}
