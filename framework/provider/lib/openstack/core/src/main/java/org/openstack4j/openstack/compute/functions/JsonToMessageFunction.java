package org.openstack4j.openstack.compute.functions;

import com.google.common.base.Function;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Attempts to extract an error message from a JSON payload.  If the message cannot be found then the original
 * JSON string is returned
 *
 * @author Jeremy Unruh
 */
public class JsonToMessageFunction implements Function<String, String> {

    public static final JsonToMessageFunction INSTANCE = new JsonToMessageFunction();
    private static final Pattern MESSAGE_PATTERN = Pattern.compile(".*message\\\":\\s\\\"([^\"]+)\\\".*");


    @Override
    public String apply(String json) {
        if (json != null && json.contains("message")) {
            Matcher m = MESSAGE_PATTERN.matcher(json);
            if (m.matches())
                return m.group(1);
        }
        return json;
    }

}
