package org.openstack4j.openstack.murano.v1.domain;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.murano.v1.domain.Application;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This is needed to serialize only property "data" without key "data" itself.
 * I.e. get output:
 * {
 * "instance": {
 * ...
 * },
 * "name": "MyApp",
 * ...
 * }
 * <p>
 * instead of:
 * <p>
 * {
 * "data": {
 * "instance": {
 * ...
 * },
 * "name": "MyApp",
 * ...
 * }
 * }
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "data")
@JsonIdentityReference(alwaysAsId = true)
public class MuranoApplication implements Application {

    private static final long serialVersionUID = 1L;

    private Map<String, Object> data;

    private MuranoServiceInfo service;

    /**
     * All except for internal service info will be added to data.
     */
    @JsonAnySetter
    public void appendData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }

        if (key.equals("?")) {
            ObjectMapper mapper = new ObjectMapper();
            this.service = mapper.convertValue(value, MuranoServiceInfo.class);
        } else {
            this.data.put(key, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        try {
            mapper.writeValue(writer, this.data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getData() {
        return this.data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MuranoServiceInfo getService() {
        return this.service;
    }

    /**
     * This class differs from similar classes for other services due to the
     * output format difference.
     * It wrappers an ArrayList<MuranoApplication> class to serialize without
     * the list key itself:
     * [
     * {"key": "value"},
     * {"key": "value2"}
     * ]
     * <p>
     * instead of:
     * {
     * "list_key": [
     * {"key": "value"},
     * {"key": "value2"}
     * ]
     * }
     */
    public static class ApplicationList extends ArrayList<MuranoApplication> implements ModelEntity {

    }
}
