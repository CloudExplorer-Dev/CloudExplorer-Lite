package org.openstack4j.openstack.networking.domain.ext;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * HttpMethod
 *
 * @author liujunpeng
 */
public enum HttpMethod {
    HEAD(0), GET(1), POST(2), PUT(2), PATCH(4), DELETE(5), OPTIONS(6), TRACE(7);
    int code;

    private HttpMethod(int code) {
        this.code = code;
    }

    @JsonCreator
    public static HttpMethod valueOf(int value) {
        for (HttpMethod method : HttpMethod.values()) {
            if (method.code() == value) {
                return method;
            }
        }
        return GET;
    }

    @JsonValue
    public int code() {
        return code;
    }
}
