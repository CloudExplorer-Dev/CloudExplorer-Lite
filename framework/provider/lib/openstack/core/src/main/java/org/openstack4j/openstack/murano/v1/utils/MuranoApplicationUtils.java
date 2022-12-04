package org.openstack4j.openstack.murano.v1.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.openstack4j.openstack.murano.v1.domain.MuranoApplication;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author Nikolay Mahotkin.
 */
public class MuranoApplicationUtils {
    /**
     * Murano API has some methods which can return both item and List<item>.
     * Need to cast it to one type which can be used further for convenience work.
     *
     * @param json JsonNode
     * @return a List of Applications
     */
    public static MuranoApplication.ApplicationList toApplications(JsonNode json) {
        ObjectMapper mapper = new ObjectMapper();
        MuranoApplication.ApplicationList list = new MuranoApplication.ApplicationList();

        if (json == null) {
            return list;
        }

        if (json.getNodeType() == JsonNodeType.ARRAY) {
            Iterator<JsonNode> i = json.elements();

            while (i.hasNext()) {
                list.add(mapper.convertValue(i.next(), MuranoApplication.class));
            }
        } else if (json.getNodeType() == JsonNodeType.OBJECT) {
            list.add(mapper.convertValue(json, MuranoApplication.class));
        }

        return list;
    }

    /**
     * It is done due to the bug in restEasy connector: it can't send json
     * as String whereas all other connectors work fine.
     *
     * @param jsonString json-encoded String
     * @return ApplicationList representing the Applications given in @jsonString
     */
    public static MuranoApplication.ApplicationList toApplications(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;

        try {
            node = mapper.readValue(jsonString, JsonNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return toApplications(node);
    }
}
