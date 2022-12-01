package org.openstack4j.core.transport.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;


/**
 * Openstack API V2 has a few Services which return a 'True' as a boolean value.  Jackson typically will not realize that this is equivalent to 'true' and will throw an
 * error.  This Deserializer is a workaround to both problems
 *
 * @author Jeremy Unruh
 */
public class OSBadBooleanDeserializer extends JsonDeserializer<Boolean> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_TRUE) {
            return Boolean.TRUE;
        }
        if (t == JsonToken.VALUE_FALSE) {
            return Boolean.FALSE;
        }
        // [JACKSON-78]: should accept ints too, (0 == false, otherwise true)
        if (t == JsonToken.VALUE_NUMBER_INT) {
            // 11-Jan-2012, tatus: May be outside of int...
            if (jp.getNumberType() == NumberType.INT) {
                return (jp.getIntValue() == 0) ? Boolean.FALSE : Boolean.TRUE;
            }
            return Boolean.valueOf(_parseBooleanFromNumber(jp, ctxt));
        }
        if (t == JsonToken.VALUE_NULL) {
            return getNullValue(ctxt);
        }
        // And finally, let's allow Strings to be converted too
        if (t == JsonToken.VALUE_STRING) {
            String text = jp.getText().trim();
            if ("true".equalsIgnoreCase(text)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(text)) {
                return Boolean.FALSE;
            }
            if (text.length() == 0) {
                return getNullValue(ctxt);
            }
            throw ctxt.weirdStringException(text, Boolean.class, "only \"true\" or \"false\" recognized");
        }
        // Otherwise, no can do:
        throw ctxt.mappingException(Boolean.class, t);
    }

    /**
     * _parse boolean from number.
     *
     * @param jp   the jp
     * @param ctxt the ctxt
     * @return true, if successful
     * @throws IOException             Signals that an I/O exception has occurred.
     * @throws JsonProcessingException the json processing exception
     */
    protected final boolean _parseBooleanFromNumber(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        if (jp.getNumberType() == NumberType.LONG) {
            return (jp.getLongValue() == 0L) ? Boolean.FALSE : Boolean.TRUE;
        }
        // no really good logic; let's actually resort to textual comparison
        String str = jp.getText();
        if ("0.0".equals(str) || "0".equals(str)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


}
